package com.javastart.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@EnableRabbit
@Configuration
@PropertySources({
        @PropertySource("classpath:application.yaml")
})
public class RabbitMQConfig {

    public static final String QUEUE_DEPOSIT = "js.deposit.notify";
    private static final String TOPIC_EXCHANGE_DEPOSIT = "js.deposit.notify.exchange";
    private static final String ROUTING_KEY_DEPOSIT = "js.deposit";

    public static final String QUEUE_WITHDRAW = "js.withdraw.notify";
    private static final String TOPIC_EXCHANGE_WITHDRAW = "js.withdraw.notify.exchange";
    private static final String ROUTING_KEY_WITHDRAW = "js.withdraw";

    public static final String QUEUE_TRANSFER = "js.transfer.notify";
    private static final String TOPIC_EXCHANGE_TRANSFER = "js.transfer.notify.exchange";
    private static final String ROUTING_KEY_TRANSFER = "js.transfer";

    public static final String QUEUE_ACCOUNT = "js.account.notify";
    private static final String TOPIC_EXCHANGE_ACCOUNT = "js.account.notify.exchange";
    private static final String ROUTING_KEY_ACCOUNT = "js.account";

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Bean
    public TopicExchange depositExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_DEPOSIT);
    }

    @Bean
    public Queue queueDeposit() {
        return new Queue(QUEUE_DEPOSIT);
    }

    @Bean
    public Binding depositBinding() {
        return BindingBuilder.bind(queueDeposit())
                .to(depositExchange())
                .with(ROUTING_KEY_DEPOSIT);
    }

    @Bean
    public TopicExchange withdrawExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_WITHDRAW);
    }

    @Bean
    public Queue queueWithdraw() {
        return new Queue(QUEUE_WITHDRAW);
    }

    @Bean
    public Binding withdrawBinding() {
        return BindingBuilder.bind(queueWithdraw())
                .to(withdrawExchange())
                .with(ROUTING_KEY_WITHDRAW);
    }

    @Bean
    public TopicExchange transferExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_TRANSFER);
    }

    @Bean
    public Queue queueTransfer() {
        return new Queue(QUEUE_TRANSFER);
    }

    @Bean
    public Binding transferBinding() {
        return BindingBuilder.bind(queueTransfer())
                .to(transferExchange())
                .with(ROUTING_KEY_TRANSFER);
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_ACCOUNT);
    }

    @Bean
    public Queue queueAccount() {
        return new Queue(QUEUE_ACCOUNT);
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder.bind(queueAccount())
                .to(accountExchange())
                .with(ROUTING_KEY_ACCOUNT);
    }
}
