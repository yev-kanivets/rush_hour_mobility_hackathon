package fr.nodesigner.meaoo.mqtt.androidsample

import android.app.Application
import android.util.Log
import android.widget.Toast
import fr.nodesigner.meaoo.mqtt.android.listener.IMessageCallback
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage

class MeaoApplication : Application() {
    val TAG = "test"

    interface MessageArrivedCallback {

        fun messageArrived(topic: String, mqttMessage: MqttMessage)
    }

    val listeners = mutableListOf<MessageArrivedCallback>()

    override fun onCreate() {
        super.onCreate()
        connect()
    }

    fun messageArrived(topic: String, mqttMessage: MqttMessage) {
        for (i in listeners.indices) {
            listeners[i].messageArrived(topic, mqttMessage)
        }
    }

    private fun connect() {
        Singleton.setInternalCb(object : IMessageCallback {

            override fun connectionLost(cause: Throwable?) {
                var errorMessage = "connectionLost"
                if (cause?.message != null) {
                    errorMessage = cause.message!!
                }
                cause?.printStackTrace()
                Log.v(TAG, "connectionLost $errorMessage")
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                for (i in listeners.indices) {
                    listeners[i].messageArrived(topic, mqttMessage)
                }

            }

            override fun deliveryComplete(messageToken: IMqttDeliveryToken) {
                Log.v(TAG, "in delivery complete ${messageToken.message}")
            }

            override fun onConnectionSuccess(token: IMqttToken) {
                Toast.makeText(this@MeaoApplication, "connnected to server", Toast.LENGTH_SHORT).show()
                Singleton.subscribeToAllTopics()
            }

            override fun onConnectionFailure(token: IMqttToken, throwable: Throwable?) {
                var errorMessage = "connection error"
                if (throwable?.message != null) {
                    errorMessage = throwable.message!!
                }
                Toast.makeText(this@MeaoApplication, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onDisconnectionSuccess(token: IMqttToken) {
                Log.v(TAG, "onDisconnectionSuccess")
            }

            override fun onDisconnectionFailure(token: IMqttToken, throwable: Throwable?) {
                Log.v(TAG, "onDisconnectionFailure")
            }
        })
        Singleton.setupApplication(applicationContext, false, true)
        Singleton.connect()
    }
}
