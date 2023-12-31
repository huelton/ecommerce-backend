package com.commerce.dscatalog.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.dscatalog.dto.OrderDTO;
import com.commerce.dscatalog.dto.OrderInsertDTO;
import com.commerce.dscatalog.dto.OrderItemDTO;
import com.commerce.dscatalog.dto.OrderRemoveItemDTO;
import com.commerce.dscatalog.dto.OrderUpdateDTO;
import com.commerce.dscatalog.entities.DeliveryAddress;
import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.OrderItem;
import com.commerce.dscatalog.entities.OrderItemPK;
import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.entities.Status;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.repositories.DeliveryAddressRepository;
import com.commerce.dscatalog.repositories.OrderItemRepository;
import com.commerce.dscatalog.repositories.OrderRepository;
import com.commerce.dscatalog.repositories.ProductRepository;
import com.commerce.dscatalog.repositories.StatusRepository;
import com.commerce.dscatalog.repositories.UserRepository;
import com.commerce.dscatalog.services.exceptions.DatabaseException;
import com.commerce.dscatalog.services.exceptions.OrderAuthorizationException;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;
import com.commerce.dscatalog.utils.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private DeliveryAddressRepository deliveryAddressRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Transactional(readOnly = true)
	public Page<OrderDTO> findPageOrder(Pageable pageable) {
		Page<Order> list = orderRepository.findAll(pageable);
		return list.map(x -> new OrderDTO(x, x.getItems()));
	}

	@Transactional(readOnly = true)
	public OrderDTO findOrder(Long id) {
		Optional<Order> obj = orderRepository.findById(id);
		Order entity = obj.orElseThrow(() -> new ResourceNotFoundException("Order not Found! id: " + id));

		return new OrderDTO(entity, entity.getItems());
	}

	@Transactional
	public OrderDTO insertOrder(OrderInsertDTO obj) {
		Order order = new Order();
		obj.setCreateDate(Instant.now());
		obj.setStatusOrder(Constants.INICIAL_STATUS_TYPE);
		OrderDTO dto = new OrderDTO(obj);

		Optional<Status> optStatus = statusRepository.findByStatusType(obj.getStatusOrder());
		Status entityStatus = optStatus
				.orElseThrow(() -> new ResourceNotFoundException("Status not Found! name: " + obj.getStatusOrder()));
		Optional<User> optUser = userRepository.findById(obj.getUserId());
		User entityUser = optUser
				.orElseThrow(() -> new ResourceNotFoundException("User not Found! id: " + obj.getUserId()));
		Optional<DeliveryAddress> optDA = deliveryAddressRepository.findAddressFromUserId(obj.getUserId());
		DeliveryAddress entityAddress = optDA.orElseThrow(
				() -> new ResourceNotFoundException("Address not Found! id from user: " + obj.getUserId()));

		copyDtoToEntity(dto, order, entityAddress, entityUser, entityStatus);
		Order orderSaved = orderRepository.save(order);

		for (OrderItemDTO orderItem : obj.getItems()) {
			Optional<Product> optProduct = productRepository.findById(orderItem.getProductId());
			Product entityProduct = optProduct.orElseThrow(
					() -> new ResourceNotFoundException("Product not Found! id: " + orderItem.getProductId()));
			OrderItem oi = new OrderItem(order, entityProduct, orderItem.getDiscount(), orderItem.getQuantity(),
					orderItem.getPrice());
			orderItemRepository.save(oi);
			orderSaved.addItems(oi);
		}

		orderSaved = orderRepository.save(orderSaved);
		emailService.sendOrderConfirmationEmail(orderSaved);

		return new OrderDTO(orderSaved, orderSaved.getItems());
	}

	@SuppressWarnings("unlikely-arg-type")
	@Transactional
	public void removeItemsOrder(Long id, OrderRemoveItemDTO obj) {
		try {
			Order entity = orderRepository.getReferenceById(id);
			if (entity.getStatus().equals(Constants.CANCEL_STATUS_TYPE)) {
				throw new OrderAuthorizationException(
						"Order is cancelled no Update! name: " + entity.getStatus().getStatusType());
			}

			copyDtoToEntityUpdateRemoveItem(obj, entity);

			for (OrderItemDTO orderItem : obj.getItems()) {
				Optional<Product> optProduct = productRepository.findById(orderItem.getProductId());
				Product entityProduct = optProduct.orElseThrow(
						() -> new ResourceNotFoundException("Product not Found! id: " + orderItem.getProductId()));
				OrderItemPK pk = new OrderItemPK(entity, entityProduct);
				Optional<OrderItem> optOrderItem = orderItemRepository.findById(pk);
				OrderItem entityOrderItem = optOrderItem.orElseThrow(() -> new ResourceNotFoundException(
						"Order Item not Found Product! id: " + orderItem.getProductId()));

				if ((entityProduct.getId() == orderItem.getProductId())
						&& (entityOrderItem.getId().getOrder().getId() == orderItem.getOrderId())) {
					entity.removeItems(entityOrderItem);
					orderItemRepository.deleteById(pk);
				}
			}
			orderRepository.save(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Order Id not found: " + id);
		}
	}

	@Transactional
	public OrderDTO update(Long id, OrderUpdateDTO obj) {
		try {
			Order entity = orderRepository.getReferenceById(id);
			Optional<Status> optStatus = statusRepository.findByStatusType(entity.getStatus().getStatusType());
			if (optStatus.get().getStatusType().equals(Constants.CANCEL_STATUS_TYPE)) {
				throw new OrderAuthorizationException(
						"Order is cancelled no Update! name: " + entity.getStatus().getStatusType());
			}

			obj.setStatusOrder(obj.getStatusOrder());
			obj.setUpdateDate(obj.getUpdateDate());
			OrderDTO dto = new OrderDTO(obj);

			Status entityStatus = optStatus.orElseThrow(
					() -> new ResourceNotFoundException("Status not Found! name: " + obj.getStatusOrder()));

			copyDtoToEntityUpdate(dto, entity, entityStatus);
			Order orderSaved = orderRepository.save(entity);

			for (OrderItemDTO orderItem : obj.getItems()) {
				Optional<Product> optProduct = productRepository.findById(orderItem.getProductId());
				Product entityProduct = optProduct.orElseThrow(
						() -> new ResourceNotFoundException("Product not Found! id: " + orderItem.getProductId()));
				OrderItem oi = new OrderItem(entity, entityProduct, orderItem.getDiscount(), orderItem.getQuantity(),
						orderItem.getPrice());
				orderItemRepository.save(oi);
				orderSaved.addItems(oi);
			}

			orderSaved = orderRepository.save(orderSaved);
			return new OrderDTO(orderSaved, orderSaved.getItems());

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Order Id not found: " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!orderRepository.existsById(id)) {
			throw new ResourceNotFoundException("Order Id not found " + id);
		}
		try {
			Optional<Order> optOrder = orderRepository.findById(id);
			Order entityOrder = optOrder.orElseThrow(() -> new ResourceNotFoundException("Order not Found! id: " + id));

			Optional<Status> optStatus = statusRepository.findByStatusType(Constants.CANCEL_STATUS_TYPE);
			entityOrder.setStatus(optStatus.get());
			orderRepository.save(entityOrder);
			entityOrder.getItems().forEach(x -> {
				x.getId();
				orderItemRepository.deleteById(x.getId());
			});
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(OrderDTO dto, Order entity, DeliveryAddress da, User user, Status status) {
		entity.setCreateDate(dto.getCreateDate());
		entity.setDeliveryAddress(da);
		entity.setUser(user);
		entity.setStatus(status);
	}

	private void copyDtoToEntityUpdate(OrderDTO dto, Order entity, Status status) {
		entity.setUpdateDate(dto.getUpdateDate());
		entity.setStatus(status);
		entity.getItems().clear();
	}

	private void copyDtoToEntityUpdateRemoveItem(OrderRemoveItemDTO dto, Order entity) {
		entity.setUpdateDate(dto.getUpdateDate());
	}

	public void accessMethod(OrderDTO dto, Order entity, DeliveryAddress da, User user, Status status) {
		copyDtoToEntity(dto, entity, da, user, status);
	}

	public void accessMethodToUodate(OrderDTO dto, Order entity, Status status) {
		copyDtoToEntityUpdate(dto, entity, status);
	}

	public void accessMethodToUpdateRemoveItem(OrderRemoveItemDTO dto, Order entity) {
		copyDtoToEntityUpdateRemoveItem(dto, entity);
	}

}
