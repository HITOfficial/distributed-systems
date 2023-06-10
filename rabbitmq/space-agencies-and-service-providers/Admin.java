import com.rabbitmq.client.*;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class Admin {
    private static final String EXCHANGE_NAME = "admin";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);





        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Select message destination (or q to quit):");
        System.out.println("1 - Send to all Agencies");
        System.out.println("2 - Send to all Carriers");
        System.out.println("3 - Send to all Agencies and Carriers");

        while (true) {
            System.out.println("Enter your message:");
            String message = br.readLine();
            System.out.println("Select message mode:");
            String mode = br.readLine();
            if("q".equals(message)) break;
            switch (mode) {
                case "1":
                    sendMessage(channel, message, "agency");
                    break;
                case "2":
                    sendMessage(channel, message, "carrier");
                    break;
                case "3":
                    // TODO TEMPORARY SOLUTION
                    sendMessage(channel, message, "agency");
                    sendMessage(channel, message, "carrier");
                    break;
                default:
                    System.out.println("Invalid mode");
                    break;
            }
        }
        channel.close();
        connection.close();
    }

    private static void sendMessage(Channel channel, String message, String routingKey) throws IOException {
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
        System.out.println("Sent: " + message);
    }
}
