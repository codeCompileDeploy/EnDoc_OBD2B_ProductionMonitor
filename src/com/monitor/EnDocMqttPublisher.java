package com.monitor;

import javax.net.ssl.SSLContext;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.Map;

public class EnDocMqttPublisher {

    // --- CONFIGURATION ---
    private static final String BROKER_URL = "ssl://6a0a9e90c4c84b8c82636f0d326ea811.s1.eu.hivemq.cloud:8883"; 
    private static final String CLIENT_ID  = "EnDoc_App_" + System.currentTimeMillis();
    private static final String TOPIC      = "Production/Monitor";
    
    // HiveMQ Access Credentials
    private static final String USERNAME   = "ProductionMonitor"; 
    private static final String PASSWORD   = "Prod1234"; 
    
    private static MqttClient client;

    /**
     * Explicitly connects to HiveMQ Cloud.
     */
    public static synchronized boolean connect() {
        if (client != null && client.isConnected()) {
            return true;
        }

        try {
            System.out.println("[MQTT INFO] Initiating connection to HiveMQ Cloud...");
            client = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());

            // --- ADDED CALLBACK FOR ADVANCED DEBUGGING ---
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.err.println("[MQTT ERROR] CONNECTION LOST! Reason: " + 
                            (cause != null ? cause.getMessage() : "Unknown"));
                    if (cause != null) cause.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    // Only used if you subscribe to receive data back
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("[MQTT INFO] Broker ACK received! Delivery is 100% complete.");
                }
            });

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(15);
            options.setKeepAliveInterval(30);
            options.setAutomaticReconnect(true);
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

            // Configure SSL/TLS
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            options.setSocketFactory(sslContext.getSocketFactory());

            // User Credentials
            if (USERNAME != null && !USERNAME.trim().isEmpty()) {
                options.setUserName(USERNAME);
                options.setPassword(PASSWORD.toCharArray());
            }

            client.connect(options);
            System.out.println("[MQTT SUCCESS] Successfully connected to HiveMQ! Client ID: " + CLIENT_ID);
            return true;

        } catch (MqttException e) {
            System.err.println("[MQTT ERROR] Failed to connect (Reason " + e.getReasonCode() + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("[MQTT ERROR] Critical system/SSL error during connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Publishes a raw string/JSON payload directly to "Production/Monitor".
     */
    public static void pushCloudData(String payload) {
        new Thread(() -> {
            try {
                System.out.println("[MQTT DEBUG] Preparing to send payload...");

                if (client == null || !client.isConnected()) {
                    System.out.println("[MQTT DEBUG] Client not connected. Attempting auto-connect...");
                    boolean isConnected = connect();
                    if (!isConnected) {
                        System.err.println("[MQTT ERROR] Aborting publish. Could not establish connection.");
                        return;
                    }
                }

                System.out.println("[MQTT DEBUG] Constructing MQTT Message...");
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(1); // QoS 1 requires broker acknowledgment
                message.setRetained(false);

                System.out.println("[MQTT DEBUG] Publishing to topic: '" + TOPIC + "' | Payload: " + payload);
                client.publish(TOPIC, message);
                
                // Because we are using the synchronous MqttClient, if it passes the line above without 
                // throwing an error, the message has officially reached the network buffer.
                System.out.println("[MQTT SUCCESS] Publish command executed successfully.");

            } catch (MqttException e) {
                System.err.println("[MQTT ERROR] Publish failed! Reason Code: " + e.getReasonCode());
                System.err.println("[MQTT ERROR] Message: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                // Catches unexpected Java errors (like NullPointerExceptions) that silently kill threads
                System.err.println("[MQTT ERROR] Unexpected application error during publish: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sends a Map of key-value pairs formatted as a JSON object to Production/Monitor.
     */
    public static void sendMqttData(Map<String, Object> dataMap) {
        System.out.println("[MQTT DEBUG] sendMqttData(Map) triggered.");
        
        if (dataMap == null || dataMap.isEmpty()) {
            System.err.println("[MQTT WARNING] Attempted to send an empty or null data map. Ignoring request.");
            return;
        }

        try {
            StringBuilder jsonBuilder = new StringBuilder("{");
            int count = 0;
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                jsonBuilder.append("\"").append(entry.getKey()).append("\":");
                
                Object value = entry.getValue();
                if (value == null) {
                    jsonBuilder.append("null");
                } else if (value instanceof Number || value instanceof Boolean) {
                    jsonBuilder.append(value);
                } else {
                    jsonBuilder.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"");
                }
                
                if (++count < dataMap.size()) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("}");

            pushCloudData(jsonBuilder.toString());
            
        } catch (Exception e) {
            System.err.println("[MQTT ERROR] Failed to parse Map into JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Disconnects and cleans up resources on application shutdown.
     */
    public static void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                System.out.println("[MQTT INFO] Disconnecting from broker...");
                client.disconnect();
                client.close();
                System.out.println("[MQTT INFO] Disconnected successfully.");
            }
        } catch (MqttException e) {
            System.err.println("[MQTT ERROR] Error while disconnecting: " + e.getMessage());
            e.printStackTrace();
        }
    }
}