import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import MongoConnection.MongoConnection;

public class SensorListener {
	
	private String topic        = "iscte_sid_2018_teste";
	private String broker       = "tcp://iot.eclipse.org:1883";
	private String clientId     = "Reader";
	private MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient sampleClient;
	private MqttConnectOptions connOpts;
	private MqttCallback callback;
	private MongoConnection mongoConnection;
	
	public SensorListener(){
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            callback = new MqttCallback() {
				
				@Override
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					System.out.println("Data");
					mongoConnection.insertData(arg1);
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					System.out.println("deliveryComplete");
					
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
					System.out.println("connectionLost");
				}
			};
			
			mongoConnection = new MongoConnection();

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}

	public void connect() {
			try {
	        	System.out.println("Connecting to broker: "+broker);
				sampleClient.connect(connOpts);
				System.out.println("Connected");
				
				sampleClient.setCallback(callback);
	            
				sampleClient.subscribe(topic);
				
				System.out.println("Subscribed to: "+topic);
				
			} catch(MqttException me) {
	            System.out.println("reason "+me.getReasonCode());
	            System.out.println("msg "+me.getMessage());
	            System.out.println("loc "+me.getLocalizedMessage());
	            System.out.println("cause "+me.getCause());
	            System.out.println("excep "+me);
	            me.printStackTrace();
	        }
			
	}
	
	public void disconnect() {
		try {
			sampleClient.disconnect();
			System.out.println("Disconnected");
		} catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
 
	}

}
