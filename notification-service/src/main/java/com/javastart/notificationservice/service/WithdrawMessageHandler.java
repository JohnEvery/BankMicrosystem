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
public class WithdrawMessageHandler {

    private final JavaMailSender javaMailSender;

    @Autowired
    public WithdrawMessageHandler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_WITHDRAW)
    public void receive(Message message) {
        System.out.println(message);
        byte[] body = message.getBody();
        String jsonBody = new String(body);
        ObjectMapper objectMapper = new ObjectMapper();
        WithdrawResponseDTO withdrawResponseDTO = null;
        try {
            withdrawResponseDTO = objectMapper.readValue(jsonBody, WithdrawResponseDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(withdrawResponseDTO);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(withdrawResponseDTO.getAccountEmail());
        msg.setFrom("lori@xyz.com");

        msg.setSubject("Withdraw");
        msg.setText("Make withdraw to bill: " + withdrawResponseDTO.getBillId()
                + "\nAmount: " + withdrawResponseDTO.getAmount() + "\nBalance: " + withdrawResponseDTO.getBalance());

        javaMailSender.send(msg);
    }
}
