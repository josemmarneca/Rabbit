package com.example.rabbit.constants;

public class RabbitConstant {


    public static final String PRIMARY_QUEUE = "test_v3.queue";

    public static final String EXCHANGE_NAME = "test_v3.exchange";

    public static final String PRIMARY_ROUTING_KEY = "test_v3.routingkey";

    public static final String WAIT_QUEUE = PRIMARY_QUEUE + ".wait";

    public static final String PARKINGLOT_QUEUE = PRIMARY_QUEUE + ".parkingLot";
}
