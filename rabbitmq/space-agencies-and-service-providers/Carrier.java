import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Carrier {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        // not to give more than one message to a worker at a time
        channel.basicQos(1);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ServiceType firstService= null;
        ServiceType secondService= null;

        System.out.println("Enter service type to execute, you can choice only two different services");
        System.out.println("1- Passer Transport");
        System.out.println("2- Cargo Transport");
        System.out.println("3- Satellite Deployed");

        while (firstService == null || secondService == null) {
            String serviceChoice = br.readLine();

            try {
                ServiceType selectedService = null;
                switch (serviceChoice) {
                    case "1":
                        selectedService = ServiceType.PASSER_TRANSPORT;
                        break;
                    case "2":
                        selectedService = ServiceType.CARGO_TRANSPORT;
                        break;
                    case "3":
                        selectedService = ServiceType.SATELLITE_DEPLOYED;
                        break;
                    default:
                        System.out.println("Wrong service type");
                        break;
                }

                if (selectedService != null) {
                    if (firstService == null) {
                        firstService = selectedService;
                    } else if (secondService == null && !selectedService.equals(firstService)) {
                        secondService = selectedService;
                    } else {
                        System.out.println("Service already selected, select another one");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }

        for (ServiceType serviceType : ServiceType.values()) {
            if(!serviceType.equals(ServiceType.SATELLITE_DEPLOYED)) {
                channel.queueDeclare(serviceType.getName(), true, false, false, null);
            }
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            ServiceType serviceType = null;

            try {
                serviceType = ServiceType.valueOf(routingKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid service type: " + routingKey);
            }

            if (serviceType != null) {
                System.out.println("Received: " + message + " for service: " + serviceType.getName());
                try {
                    doWork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Finished: " + message + " for service: " + serviceType.getName());
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        };
        for (ServiceType serviceType : ServiceType.values()) {
            if(!serviceType.equals(ServiceType.SATELLITE_DEPLOYED)) {
                channel.basicConsume(serviceType.getName(), false, deliverCallback, consumerTag -> {
                });
            }
        }
    }

    private static void  doWork() throws InterruptedException {
        Thread.sleep((int) (Math.random() * 5 * 1000));
    }
}
