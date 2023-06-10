import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public abstract class RabbitMQClient {
    protected Channel channel;
    protected Connection connection;

    public RabbitMQClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void declareQueues(ServiceType... serviceTypes) throws IOException {
        for (ServiceType serviceType : serviceTypes) {
            channel.queueDeclare(serviceType.getName(), true, false, false, null);
        }
    }

    public void handleAdminMessages(String topic) throws IOException {
        String adminQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(adminQueueName, "admin", topic);

        DeliverCallback adminDeliveryCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received admin message: " + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(adminQueueName, false, adminDeliveryCallback, consumerTag -> {});
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    protected abstract void processMessage(String message, ServiceType serviceType,  Delivery delivery) throws IOException;
}