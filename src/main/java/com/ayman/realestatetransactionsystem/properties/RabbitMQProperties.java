package com.ayman.realestatetransactionsystem.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "rabbitmq")
@Validated
@Getter
@Setter
public class RabbitMQProperties {

    private String queueName;
    private String exchangeName;
    private String routingKeyName;
    private EmailQueue emailQueue;
    private PropertyQueue propertyQueue;

    @Getter
    @Setter
    public static class EmailQueue {
        private String queueName;
        private String routingKeyName;
    }

    @Getter
    @Setter
    public static class PropertyQueue {
        private String queueName;
        private String routingKeyName;
    }


}
