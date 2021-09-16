package com.example.rabbit.callbacks;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class RabbitPeeker implements ChannelCallback<Message> {

    final MessagePropertiesConverter propertiesConverter = new DefaultMessagePropertiesConverter();

    String peekerQueue;

    public RabbitPeeker(String peekerQueue) {
        this.peekerQueue = peekerQueue;
    }

    @Override
    public Message doInRabbit(Channel channel) throws Exception {
        GetResponse result = channel.basicGet(peekerQueue, false);
        if (result == null) {
            return null;
        }
        channel.basicReject(result.getEnvelope().getDeliveryTag(), true);
        return new Message(result.getBody(), propertiesConverter.toMessageProperties(
                result.getProps(), result.getEnvelope(), "UTF-8"));
    }
}