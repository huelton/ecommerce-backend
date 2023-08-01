package com.commerce.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.services.exceptions.EmailException;

@Service
public class EmailService {

	@Value("${spring.mail.username}")
	private String emailFrom;

	@Value("${spring.mail.default.sender}")
	private String sender;

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(String to, String subject, String body) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(emailFrom);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			emailSender.send(message);

		} catch (MailParseException e) {
			throw new EmailException("Failed to send email");
		}
	}

	public void sendOrderConfirmationEmail(Order obj) {
		prepareSimpleMailMessageFromPedido(obj);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Order obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getUser().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido Confirmado! Código: " + obj.getId());
		sm.setText(obj.toString());
		emailSender.send(sm);
		return sm;
	}

}
