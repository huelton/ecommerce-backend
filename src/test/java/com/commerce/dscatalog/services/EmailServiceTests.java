package com.commerce.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.commerce.dscatalog.entities.Order;
import com.commerce.dscatalog.entities.User;
import com.commerce.dscatalog.services.exceptions.EmailException;
import com.commerce.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class EmailServiceTests {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
    	 emailSender = mock(JavaMailSender.class);

        MockitoAnnotations.openMocks(this);
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));     
    }

    @Test
    public void shouldTestSendEmailWithSuccess() {

        String to = "receiver@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        emailService.sendEmail(to, subject, body);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void shouldTestSendEmailFailureThrowsEmailException() {
        String to = "receiver@example.com";
        String subject = "Test Subject";
        String body = "Test Body";
        
        doThrow(MailParseException.class).when(emailSender).send(any(SimpleMailMessage.class));
        assertThrows(EmailException.class, () -> emailService.sendEmail(to, subject, body));

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    public void shouldTestSendEmailFailureThrowsMailParseException() {
        String to = "recipient@example.com";
        String subject = "Test Email";
        String body = "This is a test email";
        
        doThrow(MailParseException.class).when(emailSender).send(any(SimpleMailMessage.class));

        assertThrows(EmailException.class, () -> {
            emailService.sendEmail(to, subject, body);
        });
    }

    @Test
    public void testSendOrderConfirmationEmailSuccess() {

    	User user = Factory.createUser();
        Order order = Factory.createOrder(); 
        
        order.setUser(user);

        SimpleMailMessage sentMessage = spy(new SimpleMailMessage(Factory.createSimpleMailMessage()));
        doNothing().when(emailSender).send(sentMessage);

        emailService.sendOrderConfirmationEmail(order);

        assertEquals(order.getUser().getEmail(), sentMessage.getTo()[0]);
        assertEquals("Pedido Confirmado! Código: " + order.getId(), sentMessage.getSubject());
    }
}
