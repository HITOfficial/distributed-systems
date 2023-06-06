import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;


public class Agency {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        // binding queues to exchange
        for (ServiceType serviceType : ServiceType.values()) {
            channel.queueDeclare(serviceType.getName(), true, false, false, null);
        }

        System.out.println("Enter service type:  or q to quit");
        System.out.println("1- Passer Transport");
        System.out.println("2- Cargo Transport");
        System.out.println("3- Satellite Deployed");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String message = br.readLine();
            if ("q".equals(message)) break;
            switch (message) {
                case "1":
                    channel.basicPublish("", ServiceType.PASSER_TRANSPORT.getName(), null, "message".getBytes("UTF-8"));
                    break;
                case "2":
                    channel.basicPublish("", ServiceType.CARGO_TRANSPORT.getName(), null, "message".getBytes("UTF-8"));
                    break;
                case "3":
                    channel.basicPublish("", ServiceType.SATELLITE_DEPLOYED.getName(), null, "message".getBytes("UTF-8"));
                    break;
                default:
                    System.out.println("Wrong service type");
                    continue;
            }

            System.out.println("Sent: " + message);
        }
        channel.close();
        connection.close();
    }
}
