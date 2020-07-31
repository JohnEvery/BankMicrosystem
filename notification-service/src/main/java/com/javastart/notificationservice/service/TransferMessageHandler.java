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
public class TransferMessageHandler {

    public final JavaMailSender javaMailSender;

    @Autowired
    public TransferMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TRANSFER)
    public void receive(Message message) {
        System.out.println(message);
        byte[] body = message.getBody();
        String jsonBody = new String(body);
        ObjectMapper objectMapper = new ObjectMapper();
        TransferResponseDTO transferResponseDTO = null;
        try {
            transferResponseDTO = objectMapper.readValue(jsonBody, TransferResponseDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(transferResponseDTO);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(transferResponseDTO.getAccountEmailFrom());

        msg.setSubject("Transfer");
        msg.setText("Make transfer to: " + transferResponseDTO.getAccountEmailTo()
                + "\nAmount: " + transferResponseDTO.getAmount());

        javaMailSender.send(msg);

        msg.setTo(transferResponseDTO.getAccountEmailTo());

        msg.setSubject("Transfer");
        msg.setText("Make transfer from: " + transferResponseDTO.getAccountEmailFrom()
                + "\nAmount: " + transferResponseDTO.getAmount());

        javaMailSender.send(msg);
    }
}
