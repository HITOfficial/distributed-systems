import com.rabbitmq.client.*;

import java.io.IOException;

public class Z1_Consumer_Topic {

    public static void main(String[] argv) throws Exception {

        // info
        System.out.println("Z1 CONSUMER");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // queue
        String QUEUE_NAME = "queue1";
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        String EXCHANGE_NAME = "TOPIC";
        String KEY_NAME = "#";

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, KEY_NAME);

        // consumer (handle msg)
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                channel.basicConsume(QUEUE_NAME,true, this);
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicConsume(QUEUE_NAME,false, this);
                channel.basicAck(deliveryTag, false);
            }
        };

        // start listening
        System.out.println("Waiting for messages...");
        channel.basicConsume(QUEUE_NAME, true, consumer);

        // close
//        channel.close();
//        connection.close();
    }
}
