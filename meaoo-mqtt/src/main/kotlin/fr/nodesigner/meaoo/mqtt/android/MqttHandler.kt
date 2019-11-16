package fr.nodesigner.meaoo.mqtt.android

import android.content.Context
import android.util.Log
import fr.nodesigner.meaoo.mqtt.android.constant.ConnectionState
import fr.nodesigner.meaoo.mqtt.android.constant.QosPolicy
import fr.nodesigner.meaoo.mqtt.android.listener.IMessageCallback
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import java.util.*
import kotlin.collections.ArrayList

val TOPIC_PREFIX: String = "team10"

/**
 * Generic handler for all handler type
 *
 * @author Bertrand Martel
 */
class MqttHandler(context: Context) {

    companion object {
        private val TAG = MqttHandler::class.java.simpleName
    }

    /**
     * MQTT client
     */
    var mClient: MqttAndroidClient? = null

    /**
     * client ID used to authenticate
     */
    var mClientId: String =  UUID.randomUUID().toString()

    /**
     * username used for authentication
     */
    var mUsername: String = "team10"

    /**
     * password used for authentication
     */
    var mPassword: String = "plnuyfnsus"

    /**
     * Android context
     */
    var mContext: Context? = context

    /**
     * callback for MQTT events
     */
    var mClientCb: MqttCallback? = null

    /**
     * callback for MQTT connection
     */
    var mConnectionCb: IMqttActionListener? = null

    /**
     * list of message callbacks
     */
    var mMessageCallbacksList: ArrayList<IMessageCallback> = arrayListOf()

    /**
     * QOS policy (0 to 2)
     */
    var mQosDefault: QosPolicy = QosPolicy.QOS_POLICY_AT_MOST_ONCE

    /**
     * Sets whether the client and server should remember state across restarts and reconnects
     */
    var mCleanSessionDefault: Boolean = false

    /**
     * Sets the connection timeout value (in seconds)
     */
    var mTimeoutDefault: Int = 30

    /**
     * Sets the "keep alive" interval (in seconds)
     */
    var mKeepAliveDefault: Int = 60

    /**
     * define if ssl is used
     */
    var mUseSsl: Boolean = false

    /**
     * connection state
     */
    var connected: Boolean = false

    /**
     * define if current action is connecting or not
     */
    var connectionState: ConnectionState = ConnectionState.NONE;

    init {
        this.mClientCb = object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                connected = false
                for (i in mMessageCallbacksList.indices) {
                    mMessageCallbacksList[i].connectionLost(cause)
                }
            }

            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                for (i in mMessageCallbacksList.indices) {
                    mMessageCallbacksList[i].messageArrived(topic, mqttMessage)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                for (i in mMessageCallbacksList.indices) {
                    mMessageCallbacksList[i].deliveryComplete(token)
                }
            }
        }
    }

    fun isConnected(): Boolean {
        if (mClient == null)
            return false
        else
            return connected
    }

    fun connect() {
        try {
            if (!isConnected()) {

                val options = MqttConnectOptions()

                options.setCleanSession(mCleanSessionDefault)
                options.setConnectionTimeout(mTimeoutDefault)
                options.setKeepAliveInterval(mKeepAliveDefault)

                options.setUserName(mUsername)
                options.setPassword(mPassword.toCharArray())

                //val serverURI = "tcp://mr1dns3dpz5mjj.messaging.solace.cloud:1883"
                val serverURI = "wss://mr1dns3dpz5mjj.messaging.solace.cloud:8443"
                mClient = MqttAndroidClient(mContext, serverURI, mClientId)
                mClient?.setCallback(mClientCb)

                mConnectionCb = object : IMqttActionListener {

                    override fun onSuccess(iMqttToken: IMqttToken) {

                        if (connectionState == ConnectionState.CONNECTING) {
                            Log.i(TAG, "connection success")
                            connected = true
                            connectionState = ConnectionState.NONE
                            for (i in mMessageCallbacksList.indices) {
                                mMessageCallbacksList[i].onConnectionSuccess(iMqttToken)
                            }
                        } else if (connectionState == ConnectionState.DISCONNECTING) {
                            Log.i(TAG, "disconnection success")
                            connected = true
                            connectionState = ConnectionState.NONE
                            for (i in mMessageCallbacksList.indices) {
                                mMessageCallbacksList[i].onDisconnectionSuccess(iMqttToken)
                            }
                        }
                    }


                    override fun onFailure(iMqttToken: IMqttToken, throwable: Throwable) {

                        if (connectionState == ConnectionState.CONNECTING) {
                            Log.e(TAG, "connection failure : " + iMqttToken.exception.message)
                            connected = false
                            connectionState = ConnectionState.NONE;
                            for (i in mMessageCallbacksList.indices) {
                                mMessageCallbacksList[i].onConnectionFailure(iMqttToken, throwable)
                            }
                        } else if (connectionState == ConnectionState.DISCONNECTING) {
                            Log.e(TAG, "disconnection failure : " + iMqttToken.exception.message)
                            connected = false
                            connectionState = ConnectionState.NONE
                            for (i in mMessageCallbacksList.indices) {
                                mMessageCallbacksList[i].onDisconnectionFailure(iMqttToken, throwable)
                            }
                        }
                    }
                };

                try {
                    connectionState = ConnectionState.CONNECTING
                    mClient?.connect(options, mContext, mConnectionCb)
                } catch (e: MqttException) {
                    e.printStackTrace()
                }
            } else {
                Log.i(TAG, "cant connect - already connected")
            }
        } catch (e: IllegalArgumentException) {
            Log.i(TAG, "parameters error. cant connect")
        }
    }

    fun disconnect() {
        if (isConnected()) {
            try {
                connectionState = ConnectionState.DISCONNECTING
                mClient?.disconnect(mContext, mConnectionCb)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        } else {
            Log.i(TAG, "cant disconnect - already disconnected")
        }
    }

    fun addIotCallback(callback: IMessageCallback) {
        mMessageCallbacksList.add(callback)
    }

    fun removeCallback(callback: IMessageCallback) {
        mMessageCallbacksList.remove(callback)
    }

    fun setQos(qos: QosPolicy) {
        mQosDefault = qos
    }

    fun setKeepAlive(keepAlive: Int) {
        mKeepAliveDefault = keepAlive
    }

    fun setConnectionTimeout(timeout: Int) {
        mTimeoutDefault = timeout
    }

    fun setCleanSession(resetSession: Boolean) {
        mCleanSessionDefault = resetSession
    }

    fun setSSL(useSSL: Boolean) {
        mUseSsl = useSSL
    }

    /**
     * Publish a message to MQTT server
     *
     * @param topic      message topic
     * @param message    message body
     * @param isRetained define if message should be retained on MQTT server
     * @param qos        define quality of service (check QosPolicy enum)
     * @param listener   completion listener (null allowed)
     * @return
     */
    fun publishMessage(topic: String, message: String, isRetained: Boolean, qos: QosPolicy, listener: IMqttActionListener): IMqttDeliveryToken? {
        if (isConnected()) {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttMessage.isRetained = isRetained
            mqttMessage.qos = qos.value
            try {
                return mClient?.publish(topic, mqttMessage, mContext, listener);
            } catch (e: MqttPersistenceException) {
                e.printStackTrace()
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        } else {
            Log.e(TAG, "cant publish message. Not connected");
        }
        return null
    }

    /**
     * Subscribe to topic
     *
     * @param topic    topic to subscribe
     * @param qos      define quality of service (check QosPolicy enum)
     * @param listener completion listener (null allowed)
     * @return
     */
    fun subscribe(topic: String, qos: QosPolicy, listener: IMqttActionListener?) {
        if (isConnected()) {
            try {
                mClient?.subscribe(topic, qos.value, mContext, listener)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        } else {
            Log.e(TAG, "cant publish message. Not connected")
        }
    }

    /**
     * Unsubscribe a topic
     *
     * @param topic    topic to unsubscribe
     * @param listener completion listener (null allowed)
     */
    fun unsubscribe(topic: String, listener: IMqttActionListener) {
        if (isConnected()) {
            try {
                mClient?.unsubscribe(topic, mContext, listener)
            } catch (e: MqttException) {
                e.printStackTrace()
            }
        } else {
            Log.e(TAG, "cant publish message. Not connected");
        }
    }

    /**
     * AGENT​ SUBSCRIPTION
        Solace bus address
        [TOPIC_PREFIX]/prod/user/situation​
        [TOPIC_PREFIX]/prod/user/mission
        [TOPIC_PREFIX]/prod/user/objective-reached
        [TOPIC_PREFIX]/prod/user/status
     */
    fun subscribeAgentSituation() {
        val topic = TOPIC_PREFIX + "/prod/user/situation"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeAgentMission() {
        val topic = TOPIC_PREFIX + "/prod/user/mission"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeAgentObjectiveReached() {
        val topic = TOPIC_PREFIX + "/prod/user/objective-reached"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeAgentStatus() {
        val topic = TOPIC_PREFIX + "/prod/user/status"
        subscribe(topic, mQosDefault, null)
    }

    /**
     * CONTEXT​ SUBSCRIPTION
        Solace bus address
        [TOPIC_PREFIX]/prod/context/change/weather
        [TOPIC_PREFIX]/prod/context/change/air
     */
    fun subscribeContextWeather() {
        val topic = TOPIC_PREFIX + "/prod/context/change/weather"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeContextAir() {
        val topic = TOPIC_PREFIX + "/prod/context/change/air"
        subscribe(topic, mQosDefault, null)
    }

    /**
     * CITY SUBSCRIPTION
        Solace bus address
        [TOPIC_PREFIX]/prod/environment/change/roads_status​
        [TOPIC_PREFIX]/prod/environment/change/lines_state​
        [TOPIC_PREFIX]/prod/environment/change/traffic_conditions​
        [TOPIC_PREFIX]/prod/environment/change/breakdown
     */
    fun subscribeCityRoadStatus() {
        val topic = TOPIC_PREFIX + "/prod/environment/change/roads_status"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeCityLinesState() {
        val topic = TOPIC_PREFIX + "/prod/environment/change/lines_state"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeCityTrafficConditions() {
        val topic = TOPIC_PREFIX + "/prod/environment/change/traffic_conditions"
        subscribe(topic, mQosDefault, null)
    }
    fun subscribeCityBreakdown() {
        val topic = TOPIC_PREFIX + "/prod/environment/change/breakdown"
        subscribe(topic, mQosDefault, null)
    }

    /**
     * VEHICLE SUBSCRIPTION
        Solace bus address
        [TOPIC_PREFIX]/prod/{id}/status/attitude​
     */
    fun subscribeVehicleAttitude(id: String) {
        val topic = TOPIC_PREFIX + "/prod/$id/status/attitude"
        subscribe(topic, mQosDefault, null)
    }

    /**
     * AGENT​ COMMAND
        Solace bus address
        [TOPIC_PREFIX]/prod/user/path
        [TOPIC_PREFIX]/prod/user/path-to-target (*)
     */
    fun publishAgentPath(message: String, isRetained: Boolean = true, qos: QosPolicy = mQosDefault, completionListener: IMqttActionListener): IMqttDeliveryToken? {
        val topic = "$TOPIC_PREFIX/prod/user/path"
        return publishMessage(topic, message, isRetained, qos, completionListener)
    }
    fun publishAgentPathToTarget(message: String, isRetained: Boolean = true, qos: QosPolicy = mQosDefault, completionListener: IMqttActionListener): IMqttDeliveryToken? {
        val topic = "$TOPIC_PREFIX/prod/user/path-to-target"
        return publishMessage(topic, message, isRetained, qos, completionListener)
    }


    /**
     *  CITY COMMAND
        Solace bus address
        [TOPIC_PREFIX]/prod/city/reset (*)
        [TOPIC_PREFIX]/prod/city/morph/traffic_conditions (*)
        [TOPIC_PREFIX]/prod/city/morph/lines_state (*)
        [TOPIC_PREFIX]/prod/city/morph/roads_status (*)
         * = DEV env​
     */
    fun publishCityReset(message: String, isRetained: Boolean = true, qos: QosPolicy = mQosDefault, completionListener: IMqttActionListener): IMqttDeliveryToken? {
        val topic = "$TOPIC_PREFIX/prod/city/reset"
        return publishMessage(topic, message, isRetained, qos, completionListener)
    }
    fun publishCityTrafficConditions(message: String, isRetained: Boolean, qos: QosPolicy, completionListener: IMqttActionListener): IMqttDeliveryToken? {
        val topic = "$TOPIC_PREFIX/prod/city/morph/traffic_conditions"
        return publishMessage(topic, message, isRetained, qos, completionListener)
    }
    fun publishCityLinesState(message: String, isRetained: Boolean, qos: QosPolicy, completionListener: IMqttActionListener): IMqttDeliveryToken? {
        val topic = "$TOPIC_PREFIX/prod/city/morph/lines_state"
        return publishMessage(topic, message, isRetained, qos, completionListener)
    }
    fun publishCityRoadStatus(message: String, isRetained: Boolean, qos: QosPolicy, completionListener: IMqttActionListener): IMqttDeliveryToken? {
        val topic = "$TOPIC_PREFIX//prod/city/morph/roads_status"
        return publishMessage(topic, message, isRetained, qos, completionListener)
    }
}