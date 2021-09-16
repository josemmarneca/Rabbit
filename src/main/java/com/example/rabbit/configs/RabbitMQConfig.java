package com.example.rabbit.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.rabbit.constants.RabbitConstant.*;

@Configuration
public class RabbitMQConfig {


	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.port}")
	private Integer port;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;






	@Bean
    ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setUsername(username);
		cachingConnectionFactory.setPassword(password);
		cachingConnectionFactory.setHost(host);
		cachingConnectionFactory.setPort(port);
		return cachingConnectionFactory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}


	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
/*
	@Bean
    Queue queue() {
		return new Queue(PRIMARY_QUEUE, false);
	}

	@Bean
    DirectExchange EXCHANGE_NAME() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	@Bean
    Binding binding(Queue queue, DirectExchange EXCHANGE_NAME) {
		return BindingBuilder.bind(queue).to(EXCHANGE_NAME).with(PRIMARY_ROUTING_KEY);
	}*/

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	@Bean
	Queue primaryQueue() {
		return QueueBuilder.durable(PRIMARY_QUEUE)
				.deadLetterExchange(EXCHANGE_NAME)
				.deadLetterRoutingKey(PRIMARY_QUEUE + ".wait")
				.ttl(10000)
				.build();
	}

	@Bean
	Queue waitQueue() {
		return QueueBuilder.durable(PRIMARY_QUEUE + ".wait")
				.deadLetterExchange(EXCHANGE_NAME)
				.deadLetterRoutingKey(PRIMARY_ROUTING_KEY)
				.build();
	}

	@Bean
	Queue parkinglotQueue() {
		return new Queue(PRIMARY_QUEUE + ".parkingLot");
	}

	@Bean
	Binding primaryBinding(Queue primaryQueue, DirectExchange exchange) {
		return BindingBuilder.bind(primaryQueue).to(exchange).with(PRIMARY_ROUTING_KEY);
	}

	@Bean
	Binding waitBinding(Queue waitQueue, DirectExchange exchange){
		return BindingBuilder.bind(waitQueue).to(exchange).with(PRIMARY_QUEUE + ".wait");
	}

	@Bean
	Binding parkingBinding(Queue parkinglotQueue, DirectExchange exchange) {
		return BindingBuilder.bind(parkinglotQueue).to(exchange).with(PRIMARY_QUEUE + ".parkingLot");
	}



}
