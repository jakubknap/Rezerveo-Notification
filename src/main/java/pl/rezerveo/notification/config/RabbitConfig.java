package pl.rezerveo.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BOOKING_EXCHANGE = "booking.exchange";
    public static final String BOOKING_EVENT_QUEUE = "booking.notification.queue";
    public static final String MAIL_EVENT_QUEUE = "mail.notification.queue";

    public static final String BOOKING_EVENT_ROUTING_KEY = "booking.event";
    public static final String MAIL_EVENT_ROUTING_KEY = "mail.event";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public Queue bookingEventQueue() {
        return new Queue(BOOKING_EVENT_QUEUE, true);
    }

    @Bean
    public Queue mailEventQueue() {
        return new Queue(MAIL_EVENT_QUEUE, true);
    }

    @Bean
    public Binding bookingBinding(Queue bookingEventQueue, TopicExchange bookingExchange) {
        return BindingBuilder.bind(bookingEventQueue)
                             .to(bookingExchange)
                             .with(BOOKING_EVENT_ROUTING_KEY);
    }

    @Bean
    public Binding mailBinding(Queue mailEventQueue, TopicExchange bookingExchange) {
        return BindingBuilder.bind(mailEventQueue)
                             .to(bookingExchange)
                             .with(MAIL_EVENT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}