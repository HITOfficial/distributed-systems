import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Carrier extends RabbitMQClient {
    private ServiceType firstService;
    private ServiceType secondService;

    public Carrier() throws IOException, TimeoutException {
        super();
        firstService = null;
        secondService = null;
    }

    @Override
    protected void processMessage(String message, ServiceType serviceType, Delivery delivery) throws IOException {
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

    private void doWork() throws InterruptedException {
        Thread.sleep((int) (Math.random() * 5 * 1000));
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Carrier carrier = new Carrier();
        carrier.declareQueues(ServiceType.PASSER_TRANSPORT, ServiceType.CARGO_TRANSPORT);
        carrier.handleAdminMessages("carrier");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter service type to handle, you can choose only two different services:");
        System.out.println("1- Passer Transport");
        System.out.println("2- Cargo Transport");
        System.out.println("3- Satellite Deployed");

        while (carrier.firstService == null || carrier.secondService == null) {
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
                    if (carrier.firstService == null) {
                        carrier.firstService = selectedService;
                    } else if (carrier.secondService == null && !selectedService.equals(carrier.firstService)) {
                        carrier.secondService = selectedService;
                    } else {
                        System.out.println("Service already selected, select another one");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }

        for (ServiceType serviceType : ServiceType.values()) {
            if (!serviceType.equals(ServiceType.SATELLITE_DEPLOYED)) {
                carrier.channel.basicConsume(serviceType.getName(), false, (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    carrier.processMessage(message, serviceType, delivery);
                }, consumerTag -> {});
            }
        }
    }
}


