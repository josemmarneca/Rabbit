package com.example.rabbit.services;

import com.example.rabbit.callbacks.RabbitPeeker;
import com.example.rabbit.objects.MessageObject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.rabbit.constants.RabbitConstant.*;


@Service
public class RabbitMQService {

    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private Logger logger = LoggerFactory.getLogger(RabbitMQService.class);


    public RabbitMQService(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(MessageObject company) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, PRIMARY_ROUTING_KEY, company);
        System.out.println("Send msg = " + company);

    }

    public MessageObject getMessageConvertedFromQueue() {
        return (MessageObject) rabbitTemplate.receiveAndConvert(PRIMARY_QUEUE);
    }

    public Message getMessageFromQueue() {
        return rabbitTemplate.receive(PRIMARY_QUEUE);
    }

    public Message treatedMessagesInQueue() {
        Message messageFromQueue = getMessageFromQueue();
        logger.info("Message read from workerQueue: " + messageFromQueue);
        if (hasExceededRetryCount(messageFromQueue)) {
            putIntoParkingLot(messageFromQueue);

        }
        return messageFromQueue;
    }

    public Message lookMessage() {
        RabbitPeeker peeker = new RabbitPeeker(PRIMARY_QUEUE);
        Message peek = this.rabbitTemplate.execute(peeker);
        logger.info("message peeker: " + peek);
        return peek;
    }

    @RabbitListener(queues = WAIT_QUEUE)
    public void primary(Message in) throws Exception {
        logger.info("Message read from workerQueue: " + in);
        if (hasExceededRetryCount(in)) {
            putIntoParkingLot(in);
            return;
        }
        Gson gson = new Gson(); // Or use new GsonBuilder().create();

        String jsonString = new String(in.getBody());
        MessageObject target2 = gson.fromJson(jsonString, MessageObject.class);
        if (target2 != null && target2.getSender().equalsIgnoreCase("ERROR")) {
            throw new Exception("There was an error");
        }
    }

    private boolean hasExceededRetryCount(Message in) {
        List<Map<String, ?>> xDeathHeader = in.getMessageProperties().getXDeathHeader();
        if (xDeathHeader != null && xDeathHeader.size() >= 1) {
            Long count = (Long) xDeathHeader.get(0).get("count");
            return count >= 3;
        }

        return false;
    }

    private void putIntoParkingLot(Message failedMessage) {
        logger.info("Retries exeeded putting into parking lot");
        this.rabbitTemplate.send(PRIMARY_QUEUE + ".parkingLot", failedMessage);
    }
}