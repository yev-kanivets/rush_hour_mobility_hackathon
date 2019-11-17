package fr.nodesigner.meaoo.mqtt.androidsample.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.android.NAMESPACE
import fr.nodesigner.meaoo.mqtt.android.TOPIC
import fr.nodesigner.meaoo.mqtt.androidsample.MeaoApplication
import fr.nodesigner.meaoo.mqtt.androidsample.api.MeaooApi
import fr.nodesigner.meaoo.mqtt.androidsample.entity.*
import fr.nodesigner.meaoo.mqtt.androidsample.utils.WebviewUtils
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MapActivity : Activity() {

    val TAG = "test"

    lateinit var mWebView: WebView
    lateinit var meooApi: MeaooApi
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
    private val executor = Executors.newScheduledThreadPool(1)

    private fun connect() {
        executor.execute {
            try {
                val (_, _, result) = meooApi.getVehicles()
                when (result) {
                    is Result.Failure -> {
                        result.getException().printStackTrace()
                    }
                    is Result.Success -> {
                        var res = result.get()
                        for (i in res.indices) {
                            WebviewUtils.callOnWebviewThread(mWebView, "setCarMarker",
                                    (res[i].attitude.position.x * 1000).toInt(),
                                    (res[i].attitude.position.y * 1000).toInt())
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            val (_, _, result2) = meooApi.getRoadStatus("car")
            when (result2) {
                is Result.Failure -> {
                    result2.getException().printStackTrace()
                }
                is Result.Success -> {
                    var res = result2.get()
                    for (i in res.indices) {
                        if (res[i].state == "close") {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "road")
                        }
                    }
                }
            }
            val (_, _, result21) = meooApi.getRoadStatus("bike")
            when (result21) {
                is Result.Failure -> {
                    result21.getException().printStackTrace()
                }
                is Result.Success -> {
                    var res = result21.get()
                    for (i in res.indices) {
                        if (res[i].state == "close") {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "road")
                        }
                    }
                }
            }
            val (_, _, result22) = meooApi.getRoadStatus("walk")
            when (result22) {
                is Result.Failure -> {
                    result22.getException().printStackTrace()
                }
                is Result.Success -> {
                    var res = result22.get()
                    for (i in result22.get().indices) {
                        if (res[i].state == "close") {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "road")
                        }
                    }
                }
            }
            val (_, _, result3) = meooApi.getTrafficConditions()
            when (result3) {
                is Result.Failure -> {
                    result3.getException().printStackTrace()
                }
                is Result.Success -> {
                    var res = result3.get()
                    for (i in res.indices) {
                        if (res[i].slowing_factor in 1..2) {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "slowdown", "#FFA12C")
                        }
                        if (res[i].slowing_factor in 2..4) {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "slowdown", "#FF872C")
                        }
                        if (res[i].slowing_factor in 4..6) {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "slowdown", "#FE612C")
                        }
                        if (res[i].slowing_factor in 6..8) {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "slowdown", "#FD3A2D")
                        }
                        if (res[i].slowing_factor in 8..11) {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "slowdown", "#F11D28")
                        }
                    }
                }
            }
            val (_, _, result4) = meooApi.getMetroStatus()
            when (result4) {
                is Result.Failure -> {
                    result4.getException().printStackTrace()
                }
                is Result.Success -> {
                    var res = result4.get()
                    for (i in res.indices) {
                        if (res[i].state == "close") {
                            WebviewUtils.callOnWebviewThread(mWebView, "close",
                                    (res[i].locations.from.x * 1000).toInt(),
                                    (res[i].locations.from.y * 1000).toInt(),
                                    (res[i].locations.to.x * 1000).toInt(),
                                    (res[i].locations.to.y * 1000).toInt(), "metro")
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)
        findViewById<Button>(R.id.btnStop).setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        val app = application as MeaoApplication
        app.listeners.add(object : MeaoApplication.MessageArrivedCallback {
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                val jsonString = String(mqttMessage.payload)
                Log.v("test", jsonString)
                when (topic) {
                    TOPIC.USER_SITUATION.path -> {
                        val userSituation = Gson().fromJson<UserSituation>(jsonString, UserSituation::class.java)
                        Log.v("test", userSituation.toString())
                        Log.v("test", "" + userSituation.position.x + " et " + userSituation.position.y)
                        WebviewUtils.callOnWebviewThread(mWebView, "move", (userSituation.position.x * 1000).toInt(), (userSituation.position.y * 1000).toInt())
                        scroll(webview = mWebView, x = (userSituation.position.x * 1000).toInt(), y = (userSituation.position.y * 1000).toInt())
                    }
                    TOPIC.USER_STATUS.path -> {
                        val userStatus = Gson().fromJson<UserStatus>(jsonString, UserStatus::class.java)
                        if (userStatus.status == "stopped") {
                            executor.schedule({
                                setResult(RESULT_OK)
                                finish()
                            }, 2, TimeUnit.SECONDS)
                        } else {
                            WebviewUtils.callOnWebviewThread(mWebView, "move", (userStatus.userSituation.position.x * 1000).toInt(), (userStatus.userSituation.position.y * 1000).toInt())
                            scroll(webview = mWebView, x = (userStatus.userSituation.position.x * 1000).toInt(), y = (userStatus.userSituation.position.y * 1000).toInt())
                        }
                    }
                    TOPIC.USER_MISSION.path -> {
                        val mission = Gson().fromJson<Mission>(jsonString, Mission::class.java)
                        for (i in mission.positions.indices) {
                            WebviewUtils.callOnWebviewThread(mWebView, "setDestinationMarker", (mission.positions[i].x * 1000).toInt(), (mission.positions[i].y * 1000).toInt())
                        }
                    }
                    TOPIC.CAR_SITUATION.path -> {
                        val carSituation = Gson().fromJson<CarSituation>(jsonString, CarSituation::class.java)
                        WebviewUtils.callOnWebviewThread(mWebView, "setCarMarker", (carSituation.position.x * 1000).toInt(), (carSituation.position.y * 1000).toInt())
                        //scroll(webview = mWebView, x = (carSituation.position.x * 1000).toInt(), y = (carSituation.position.y * 1000).toInt())
                    }
                    TOPIC.ROADS_STATUS.path -> {
                        val roadStatus = Gson().fromJson<List<RoadsStatus>>(jsonString)
                        for (i in roadStatus[0].car.indices) {
                            if (roadStatus[0].car[i].state == "close") {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (roadStatus[0].car[i].locations.from.x * 1000).toInt(),
                                        (roadStatus[0].car[i].locations.from.y * 1000).toInt(),
                                        (roadStatus[0].car[i].locations.to.x * 1000).toInt(),
                                        (roadStatus[0].car[i].locations.to.y * 1000).toInt(), "road")
                            }
                        }
                        for (i in roadStatus[0].bike.indices) {
                            if (roadStatus[0].bike[i].state == "close") {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (roadStatus[0].bike[i].locations.from.x * 1000).toInt(),
                                        (roadStatus[0].bike[i].locations.from.y * 1000).toInt(),
                                        (roadStatus[0].bike[i].locations.to.x * 1000).toInt(),
                                        (roadStatus[0].bike[i].locations.to.y * 1000).toInt(), "road")
                            }
                        }
                        for (i in roadStatus[0].walk.indices) {
                            if (roadStatus[0].walk[i].state == "close") {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (roadStatus[0].walk[i].locations.from.x * 1000).toInt(),
                                        (roadStatus[0].walk[i].locations.from.y * 1000).toInt(),
                                        (roadStatus[0].walk[i].locations.to.x * 1000).toInt(),
                                        (roadStatus[0].walk[i].locations.to.y * 1000).toInt(), "road")
                            }
                        }
                    }
                    TOPIC.LINE_STATE.path -> {
                        val lineState = Gson().fromJson<Array<Edge>>(jsonString, Array<Edge>::class.java)
                        for (i in lineState.indices) {
                            if (lineState[i].state == "close") {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (lineState[i].locations.from.x * 1000).toInt(),
                                        (lineState[i].locations.from.y * 1000).toInt(),
                                        (lineState[i].locations.to.x * 1000).toInt(),
                                        (lineState[i].locations.to.y * 1000).toInt(), "metro")
                            }
                        }
                    }
                    TOPIC.TRAFFIC_CONDITIONS.path -> {
                        val lineState = Gson().fromJson<Array<TrafficCondition>>(jsonString, Array<TrafficCondition>::class.java)
                        for (i in lineState.indices) {
                            if (lineState[i].slowing_factor in 1..2) {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (lineState[i].locations.from.x * 1000).toInt(),
                                        (lineState[i].locations.from.y * 1000).toInt(),
                                        (lineState[i].locations.to.x * 1000).toInt(),
                                        (lineState[i].locations.to.y * 1000).toInt(), "slowdown", "#FFA12C")
                            }
                            if (lineState[i].slowing_factor in 2..4) {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (lineState[i].locations.from.x * 1000).toInt(),
                                        (lineState[i].locations.from.y * 1000).toInt(),
                                        (lineState[i].locations.to.x * 1000).toInt(),
                                        (lineState[i].locations.to.y * 1000).toInt(), "slowdown", "#FF872C")
                            }
                            if (lineState[i].slowing_factor in 4..6) {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (lineState[i].locations.from.x * 1000).toInt(),
                                        (lineState[i].locations.from.y * 1000).toInt(),
                                        (lineState[i].locations.to.x * 1000).toInt(),
                                        (lineState[i].locations.to.y * 1000).toInt(), "slowdown", "#FE612C")
                            }
                            if (lineState[i].slowing_factor in 6..8) {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (lineState[i].locations.from.x * 1000).toInt(),
                                        (lineState[i].locations.from.y * 1000).toInt(),
                                        (lineState[i].locations.to.x * 1000).toInt(),
                                        (lineState[i].locations.to.y * 1000).toInt(), "slowdown", "#FD3A2D")
                            }
                            if (lineState[i].slowing_factor in 8..11) {
                                WebviewUtils.callOnWebviewThread(mWebView, "close",
                                        (lineState[i].locations.from.x * 1000).toInt(),
                                        (lineState[i].locations.from.y * 1000).toInt(),
                                        (lineState[i].locations.to.x * 1000).toInt(),
                                        (lineState[i].locations.to.y * 1000).toInt(), "slowdown", "#F11D28")
                            }
                        }
                    }
                    else -> {
                        if (topic.endsWith("attitude")) {
                            val carSituation = Gson().fromJson<CarSituation>(jsonString, CarSituation::class.java)
                            WebviewUtils.callOnWebviewThread(mWebView, "setCarMarker", (carSituation.position.x * 1000).toInt(), (carSituation.position.y * 1000).toInt())
                        } else {
                            Log.v(TAG, "[$topic] $jsonString")
                        }
                    }
                }
            }
        })
        meooApi = MeaooApi(NAMESPACE)
        WebView.setWebContentsDebuggingEnabled(true)
        mWebView = findViewById(R.id.map_view)
        mWebView.settings.javaScriptEnabled = true
        mWebView.setInitialScale(250)
        mWebView.loadUrl("file:///android_asset/map.html")
        /*
        findViewById<Button>(R.id.move200_2000).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 200, 2000)
            //mWebView.scrollTo(0, 1200)
            scroll(webview = mWebView, x = 200, y = 2000)
        }
        findViewById<Button>(R.id.move12800_2000).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 12800, 2000)
            //mWebView.scrollTo(3500, 1200)
            scroll(webview = mWebView, x = 12800, y = 2000)
        }
        findViewById<Button>(R.id.move21800_3800).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 21800, 3800)
            //mWebView.scrollTo(6000, 100)
            scroll(webview = mWebView, x = 21800, y = 3800)
        }
        findViewById<Button>(R.id.move9200_200).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 9200, 200)
            //mWebView.scrollTo(2300, 1200)
            scroll(webview = mWebView, x = 9200, y = 200)
        }
        findViewById<Button>(R.id.move21800_200).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 21800, 200)
            //mWebView.scrollTo(6000, 1200)
            scroll(webview = mWebView, x = 21800, y = 200)
        }
        findViewById<Button>(R.id.move7400_5600).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 7400, 5600)
            scroll(webview = mWebView, x = 7400, y = 5600)
        }
        findViewById<Button>(R.id.move5600_2000).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "move", 5600, 2000)
            scroll(webview = mWebView, x = 5600, y = 2000)
        }
        findViewById<Button>(R.id.set_destination_marker).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "setDestinationMarker", 14000, 2000)
            scroll(webview = mWebView, x = 14000, y = 2000)
        }
        findViewById<Button>(R.id.set_car_marker).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "setCarMarker", 5600, 2000)
            scroll(webview = mWebView, x = 5600, y = 2000)
        }

        findViewById<Button>(R.id.set_road_close_car).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "setRoadClosedForCarMarker", 5600, 2000)
            scroll(webview = mWebView, x = 5600, y = 2000)
        }
        findViewById<Button>(R.id.set_road_close_bike).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "setRoadClosedForBikeMarker", 5600, 2000)
            scroll(webview = mWebView, x = 5600, y = 2000)
        }
        findViewById<Button>(R.id.set_road_close_pedestrian).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "clearAll", 0, 0)
            WebviewUtils.callOnWebviewThread(
                    mWebView,
                    "setRoadClosedForPedestrianMarker",
                    5600,
                    2000
            )
            scroll(webview = mWebView, x = 5600, y = 2000)
        }
        findViewById<Button>(R.id.set_metro_issue).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "close", 10000, 5600, 1200, 1800, "metro")
            scroll(webview = mWebView, x = 10000, y = 5600)
        }
        findViewById<Button>(R.id.set_road_slowdown).setOnClickListener {
            WebviewUtils.callOnWebviewThread(mWebView, "close", 1200, 800, 1200, 1800, "slowdown")
            scroll(webview = mWebView, x = 1200, y = 800)
        }
        findViewById<Button>(R.id.set_roadmap).setOnClickListener {
            val list = arrayListOf<Array<Int>>()
            list.add(arrayOf(200, 2000))
            list.add(arrayOf(200, 3800))
            list.add(arrayOf(2000, 3800))
            list.add(arrayOf(2000, 5600))
            list.add(arrayOf(10000, 5600))
            list.add(arrayOf(10000, 1600))
            list.add(arrayOf(2000, 1600))
            WebviewUtils.callOnWebviewThread(
                    mWebView, "drawRoadMap", list, "#507FF4"
            )
            scroll(webview = mWebView, x = 200, y = 2000)
        }
        */
        connect()
    }

    fun scroll(webview: WebView, x: Int, y: Int) {
        var scrollX = (x / 8.72).toInt()
        if (x < 500) {
            scrollX = 0
        }
        var scrollY = 0
        if (y < 2500) {
            scrollY = 0
        } else if (y < 4000) {
            scrollY = 200
        } else {
            scrollY = 0
        }
        webview.scrollTo(scrollX, 0)
    }
}
