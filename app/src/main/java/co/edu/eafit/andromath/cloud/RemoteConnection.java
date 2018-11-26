package co.edu.eafit.andromath.cloud;

import android.os.StrictMode;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;

public class RemoteConnection {

    public RemoteConnection() {

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            String uri = "amqp://sbvfmqsr:TyV5Xs3YxndY8n-jqCIM4eDhFQgqM7gW@otter.rmq.cloudamqp.com/sbvfmqsr";

            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(uri);

            Connection conn = factory.newConnection();
            Channel ch = conn.createChannel();
            RpcClient service = new RpcClient(ch, "", "Hello");

            System.out.println(service.stringCall("Here is the equation"));
            conn.close();
        } catch (Exception e) {
            System.err.println("Main thread caught exception: " + e);
            e.printStackTrace();
        }
    }
}