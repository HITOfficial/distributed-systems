import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;


public class Agency extends RabbitMQClient {
    public Agency() throws IOException, TimeoutException {
        super();
    }

    @Override
    protected void processMessage(String message, ServiceType serviceType, Delivery delivery) throws IOException {
        System.out.println("Received: " + message + " for service: " + serviceType.getName());
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

    }


    public static void main(String[] args) throws IOException, TimeoutException {
        Agency agency = new Agency();
        agency.declareQueues(ServiceType.values());
        agency.handleAdminMessages();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String message = br.readLine();
            if ("q".equals(message)) {
                break;
            }
            switch (message) {
                case "1":
                    agency.channel.basicPublish("", ServiceType.PASSER_TRANSPORT.getName(), null, "message".getBytes("UTF-8"));
                    break;
                case "2":
                    agency.channel.basicPublish("", ServiceType.CARGO_TRANSPORT.getName(), null, "message".getBytes("UTF-8"));
                    break;
                case "3":
                    agency.channel.basicPublish("", ServiceType.SATELLITE_DEPLOYED.getName(), null, "message".getBytes("UTF-8"));
                    break;
                default:
                    System.out.println("Wrong service type");
                    continue;
            }

            System.out.println("Sent: " + message);
        }

        agency.close();
    }
}
