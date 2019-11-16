package fr.nodesigner.meaoo.mqtt.androidsample

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import fr.nodesigner.meaoo.mqtt.android.MqttHandler
import fr.nodesigner.meaoo.mqtt.android.listener.IMessageCallback
import fr.nodesigner.meaoo.mqtt.android.model.Path
import fr.nodesigner.meaoo.mqtt.android.model.Teleport
import org.eclipse.paho.client.mqttv3.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Singleton {

    private val TAG = Singleton::class.java.simpleName

    private var mInstance: Singleton? = null

    lateinit var mContext: Context

    /**
     * IoT Foundation Application Handler
     */
    private var mHandler: MqttHandler? = null

    /**
     * control for auto reconnection
     */
    private var exit = false

    /**
     * define if app will reconnect automatically if connection is lost
     */
    private var reconnectAuto = true

    /**
     * reconnection interval in second
     */
    private val RECONNECT_INTERVAL = 1

    /**
     * callback for IoT Foundation wrapper
     */
    private var mIotCallback: IMessageCallback? = null

    private var mInternalCb: IMessageCallback? = null

    private val scheduler = Executors.newScheduledThreadPool(1)

    /**
     * Connect as an application with defined connection params
     *
     * @param appID         application ID
     * @param orgID         organization ID
     * @param apiKey        API KEY
     * @param apiToken      API TOKEN
     * @param useSSL        define if SSL is used or plain HTTP
     * @param reconnectAuto define if app will reconnect if connection is lost
     */
    fun setupApplication(useSSL: Boolean, autoReconnect: Boolean) {

        this.reconnectAuto = autoReconnect

        disconnect(false)
        if (mIotCallback != null) {
            mHandler?.removeCallback(mIotCallback!!)
        }

        mHandler = MqttHandler(mContext)

        mIotCallback = object : IMessageCallback {
            override fun connectionLost(cause: Throwable) {
                Log.i(TAG, "connectionLost")

                if (mInternalCb != null)
                    mInternalCb!!.connectionLost(cause)

                if (cause != null) {
                    Log.e(TAG, "connection lost : " + cause.message)
                }
                if (!exit && reconnectAuto) {
                    /*
                    scheduler.schedule(Callable<Unit> {
                        Log.i(TAG, "trying to reconnect")
                        mHandler?.connect()
                    }, RECONNECT_INTERVAL.toLong(), TimeUnit.SECONDS)
                     */
                } else {
                    Log.i(TAG, "not trying to reconnect")
                }
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                if (mInternalCb != null)
                    mInternalCb!!.messageArrived(topic, mqttMessage)
                // Log.i(TAG, "messageArrived : " + topic + " : " + String(mqttMessage.getPayload()))
            }

            override fun deliveryComplete(messageToken: IMqttDeliveryToken) {
                if (mInternalCb != null)
                    mInternalCb!!.deliveryComplete(messageToken)
                try {
                    Log.i(TAG, "deliveryComplete : " + String(messageToken.getMessage().getPayload()))
                } catch (e: MqttException) {
                    e.printStackTrace()
                }

            }

            override fun onConnectionSuccess(token: IMqttToken) {
                if (mInternalCb != null)
                    mInternalCb!!.onConnectionSuccess(token)
                exit = false
                Log.i(TAG, "subscribe to device events ...")
                //mHandler.subscribeDeviceEvents("+", "+", "+")
            }

            override fun onConnectionFailure(token: IMqttToken, throwable: Throwable) {
                if (mInternalCb != null)
                    mInternalCb!!.onConnectionFailure(token, throwable)
            }

            override fun onDisconnectionSuccess(token: IMqttToken) {
                if (mInternalCb != null)
                    mInternalCb!!.onDisconnectionSuccess(token)
                if (exit) {
                    mHandler?.removeCallback(mIotCallback!!)
                }
            }

            override fun onDisconnectionFailure(token: IMqttToken, throwable: Throwable) {
                if (mInternalCb != null)
                    mInternalCb?.onDisconnectionFailure(token, throwable)
            }
        }

        mHandler?.addIotCallback(mIotCallback!!)

        mHandler?.setSSL(useSSL)
    }

    fun setInternalCb(callback: IMessageCallback) {
        mInternalCb = callback
    }

    /**
     * connect to MQTT server
     *
     * @return
     */
    fun connect(): Boolean {

        if (mHandler != null) {
            mHandler!!.connect()
            return true
        }
        return false
    }

    /**
     * disconnect from MQTT server
     *
     * @param reconnect define if app will reconnect after disconnection
     * @return
     */
    fun disconnect(reconnect: Boolean): Boolean {

        if (!reconnect)
            exit = true
        if (mHandler != null && mHandler!!.isConnected()) {
            mHandler!!.disconnect()
            return true
        }
        return false
    }

    fun isAutoReconnect(): Boolean {
        return reconnectAuto
    }

    fun subscribeToAllTopics() {
        mHandler?.apply {
            subscribeAgentSituation()
        }
    }

    fun publishAgentPath(path: Path) {
        mHandler?.publishAgentPathToTarget(message = Gson().toJson(path), completionListener = object : IMqttActionListener {
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.v(TAG, "failure")
            }

            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.v(TAG, "success : $asyncActionToken")
            }
        })
    }

    fun publishTeleport(teleport: Teleport) {
        Log.v(TAG, Gson().toJson(teleport))
        mHandler?.publishAgentPathToTarget(message = Gson().toJson(teleport), completionListener = object : IMqttActionListener {
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.v(TAG, "failure")
            }

            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.v(TAG, "success : $asyncActionToken")
            }
        })
    }

    fun publishCityReset(path: Path) {
        mHandler?.publishCityReset(message = "", completionListener = object : IMqttActionListener {
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.v(TAG, "failure")
            }

            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.v(TAG, "success : $asyncActionToken")
            }
        })
    }
}
