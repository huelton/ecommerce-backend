package com.commerce.dscatalog.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.dscatalog.dto.OrderDTO;
import com.commerce.dscatalog.dto.OrderInsertDTO;
import com.commerce.dscatalog.dto.OrderItemDTO;
import com.commerce.dscatalog.entities.DeliveryAddress;
import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.OrderItem;
import com.commerce.dscatalog.entities.Product;
import com.commerce.dscatalog.entities.Status;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.repositories.DeliveryAddressRepository;
import com.commerce.dscatalog.repositories.OrderItemRepository;
import com.commerce.dscatalog.repositories.OrderRepository;
import com.commerce.dscatalog.repositories.ProductRepository;
import com.commerce.dscatalog.repositories.StatusRepository;
import com.commerce.dscatalog.repositories.UserRepository;
import com.commerce.dscatalog.services.exceptions.ResourceNotFoundException;
import com.commerce.dscatalog.utils.Constants;

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
	private AuthService authService;

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
			orderSaved.addtems(oi);
		}

		orderSaved = orderRepository.save(orderSaved);

		return new OrderDTO(orderSaved, orderSaved.getItems());
	}

	private void copyDtoToEntity(OrderDTO dto, Order entity, DeliveryAddress da, User user, Status status) {

		entity.setCreateDate(dto.getCreateDate());
		entity.setDeliveryAddress(da);
		entity.setUser(user);
		entity.setStatus(status);
		entity.getItems().clear();

	}

}
