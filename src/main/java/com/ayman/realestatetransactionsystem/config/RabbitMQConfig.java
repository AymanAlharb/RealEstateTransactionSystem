package com.ayman.realestatetransactionsystem.config;

import com.ayman.realestatetransactionsystem.properties.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {
    private final RabbitMQProperties rabbitMQProperties;
    @Bean
    public Queue emailQueue() {
        return new Queue(rabbitMQProperties.getEmailQueue().getQueueName());
    }

    @Bean
    public Queue propertyQueue() {
        return new Queue(rabbitMQProperties.getPropertyQueue().getQueueName());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(rabbitMQProperties.getExchangeName());
    }


    @Bean
    public Binding emailQueueBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(exchange())
                .with(rabbitMQProperties.getEmailQueue().getRoutingKeyName());
    }

    @Bean
    public Binding propertyBinding() {
        return BindingBuilder
                .bind(propertyQueue())
                .to(exchange())
                .with(rabbitMQProperties.getPropertyQueue().getRoutingKeyName());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }


}
