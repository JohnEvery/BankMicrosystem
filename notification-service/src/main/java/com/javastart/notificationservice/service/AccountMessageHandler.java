package com.javastart.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.notificationservice.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AccountMessageHandler {

    private final JavaMailSender javaMailSender;

    @Autowired
    public AccountMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ACCOUNT)
    public void receive(Message message) {
        System.out.println(message);
        byte[] body = message.getBody();
        String jsonBody = new String(body);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountResponseDTO accountResponseDTO = null;
        try {
            accountResponseDTO = objectMapper.readValue(jsonBody, AccountResponseDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(accountResponseDTO);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(accountResponseDTO.getEmail());

        msg.setSubject("Greetings!");
        msg.setText("Hello, " + accountResponseDTO.getName() + ", thanks for register!");

        javaMailSender.send(msg);
    }
}
