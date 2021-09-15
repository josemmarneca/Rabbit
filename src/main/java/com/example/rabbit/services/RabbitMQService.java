package com.example.rabbit.services;

import com.example.rabbit.objects.MessageObject;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {


	private final RabbitAdmin rabbitAdmin;
	private final RabbitTemplate rabbitTemplate;
	
	@Value("${javainuse.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${javainuse.rabbitmq.routingkey}")
	private String routingkey;

	@Value("${javainuse.rabbitmq.queue}")
	String queueName;
	String kafkaTopic = "java_in_use_topic";

	public RabbitMQService(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
		this.rabbitAdmin = rabbitAdmin;
		this.rabbitTemplate = rabbitTemplate;
	}

	public void send(MessageObject company) {
		rabbitTemplate.convertAndSend(exchange, routingkey, company);
		System.out.println("Send msg = " + company);
	    
	}

	public MessageObject getMessageFromQueue(){
		return (MessageObject) rabbitTemplate.receiveAndConvert(queueName);
	}
}