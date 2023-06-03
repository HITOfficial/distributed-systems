import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Z1_Producer_Direct {

    public static void main(String[] argv) throws Exception {

        // info
        System.out.println("Z1 PRODUCER");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // queue
        String QUEUE_NAME = "queue1";
        String EXCHANGE_NAME = "DIRECT";
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        // producer (publish msg)
        while (true) {
            System.out.println("Routing key: \n >");
            String key = br.readLine();
            System.out.println("Message: \n >");
            String message = br.readLine();
            if (key.equals("q")) {
                break;
            }

            channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes("UTF-8") );

        }


        // close
        channel.close();
        connection.close();
    }
}
