package fr.nodesigner.meaoo.mqtt.android.listener

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage

/**
 * interface between applications and iotf-library
 *
 * @author Bertrand Martel
 */
interface IMessageCallback {

    /**
     * This method is called when the connection to the server is lost.
     *
     * @param cause the reason behind the loss of connection.
     */
    fun connectionLost(cause: Throwable)

    /**
     * This method is called when a message arrives from the server.
     *
     * @param topic       name of the topic on the message was published to
     * @param mqttMessage the actual message
     * @throws Exception
     */
    @Throws(Exception::class)
    fun messageArrived(topic: String, mqttMessage: MqttMessage)

    /**
     * Called when delivery for a message has been completed, and all acknowledgments have been received.
     *
     * @param messageToken he delivery token associated with the message.
     */
    fun deliveryComplete(messageToken: IMqttDeliveryToken)

    /**
     * Called when connection is established
     *
     * @param iMqttToken token for this connection
     */
    fun onConnectionSuccess(iMqttToken: IMqttToken)

    /**
     * Called when connection has failed
     *
     * @param iMqttToken token when failure occured
     * @param throwable  exception
     */
    fun onConnectionFailure(iMqttToken: IMqttToken, throwable: Throwable)

    /**
     * Called when disconnection is successfull
     *
     * @param iMqttToken token for this connection
     */
    fun onDisconnectionSuccess(iMqttToken: IMqttToken)

    /**
     * Called when disconnection failed
     *
     * @param iMqttToken token when failure occured
     * @param throwable  exception
     */
    fun onDisconnectionFailure(iMqttToken: IMqttToken, throwable: Throwable)
}