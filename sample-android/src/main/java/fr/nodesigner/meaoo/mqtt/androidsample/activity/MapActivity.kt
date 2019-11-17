package fr.nodesigner.meaoo.mqtt.androidsample.activity

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.androidsample.utils.WebviewUtils

class MapActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)
        WebView.setWebContentsDebuggingEnabled(true)
        val mWebView: WebView = findViewById(R.id.map_view)
        mWebView.settings.javaScriptEnabled = true
        mWebView.setInitialScale(200)
        mWebView.loadUrl("file:///android_asset/map.html")
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