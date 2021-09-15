package com.example.rabbit.controllers;

import com.example.rabbit.objects.MessageObject;
import com.example.rabbit.services.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rabbitmq/")
public class RabbitMQWebController {

    @Autowired
    RabbitMQService rabbitMQService;

    @PostMapping(value = "/producer")
    public MessageObject producer(@RequestBody MessageObject messageObject) {

        rabbitMQService.send(messageObject);
        return messageObject;
    }

    @GetMapping(value = "/consume")
    public MessageObject consume() {
        MessageObject message = rabbitMQService.getMessageFromQueue();
        return message;
    }

}

