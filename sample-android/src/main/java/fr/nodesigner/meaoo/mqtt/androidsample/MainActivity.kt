package fr.nodesigner.meaoo.mqtt.androidsample

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import com.github.kittinunf.result.Result
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.eclipse.paho.client.mqttv3.IMqttToken
import android.widget.Toast
import android.content.Intent
import android.util.Log
import fr.nodesigner.meaoo.androidsample.R
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import fr.nodesigner.meaoo.mqtt.android.listener.IMessageCallback
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.android.model.Path
import fr.nodesigner.meaoo.mqtt.android.model.Teleport


class MainActivity : Activity() {
    private lateinit var mHandler: Handler

    private lateinit var mExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Singleton.mContext = applicationContext
        Singleton.setInternalCb(object : IMessageCallback {

            override fun connectionLost(cause: Throwable) {
                var errorMessage = "connectionLost"
                if (cause.message != null) {
                    errorMessage = cause.message!!
                }
                cause.printStackTrace()
                Log.v(TAG, "connectionLost $errorMessage")
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.v(TAG, "message arrived")
            }

            override fun deliveryComplete(messageToken: IMqttDeliveryToken) {
                Log.v(TAG, "in delivery complete ${messageToken.message}")
            }

            override fun onConnectionSuccess(token: IMqttToken) {
                Toast.makeText(this@MainActivity, "connnected to server", Toast.LENGTH_SHORT).show()
                val teleport = Teleport(
                        vehicle_type = "walk",
                        path = arrayListOf<List<Float>>(arrayListOf(20.9f, 5.6f), arrayListOf(20.9f, 5.6f)),
                        costs = arrayListOf(0f,0f)
                )
                Singleton.publishTeleport(teleport)
            }

            override fun onConnectionFailure(token: IMqttToken, throwable: Throwable) {
                //display error message
                var errorMessage = "connection error"
                if (throwable?.message != null) {
                    errorMessage = throwable.message!!
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onDisconnectionSuccess(token: IMqttToken) {
                Log.v(TAG, "onDisconnectionSuccess")
            }

            override fun onDisconnectionFailure(token: IMqttToken, throwable: Throwable) {
                Log.v(TAG, "onDisconnectionFailure")
            }
        })
        connect()
    }

    private fun connect() {
        Singleton.setupApplication(false, true)
        Singleton.connect()
    }

}