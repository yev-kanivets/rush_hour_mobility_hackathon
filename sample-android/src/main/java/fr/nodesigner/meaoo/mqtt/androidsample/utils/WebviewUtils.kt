package fr.nodesigner.meaoo.mqtt.androidsample.utils

import android.util.Log
import android.webkit.WebView

class WebviewUtils {

    companion object {
        /**
         * Call javascript functions in webview.
         *
         * @param webView    webview object
         * @param methodName function name
         * @param params     function parameters
         */
        fun callJavaScript(
                webView: WebView,
                methodName: String,
                x1: Int,
                y1: Int,
                x2: Int = 0,
                y2: Int = 0,
                type: String = ""
        ) {

            val stringBuilder = StringBuilder()
            stringBuilder.append("javascript:try{")
            stringBuilder.append(methodName)
            stringBuilder.append("(")
            stringBuilder.append(x1)
            stringBuilder.append(",")
            stringBuilder.append(y1)
            stringBuilder.append(",")
            stringBuilder.append(x2)
            stringBuilder.append(",")
            stringBuilder.append(y2)
            stringBuilder.append(",")
            stringBuilder.append("'")
            stringBuilder.append(type)
            stringBuilder.append("'")
            stringBuilder.append(")}catch(error){console.error(error.message);}")
            val call = stringBuilder.toString()
            Log.v("test", call)
            webView.loadUrl(call)
        }

        /**
         * Call javascript functions in webview.
         *
         * @param webView    webview object
         * @param methodName function name
         * @param params     function parameters
         */
        fun callJavaScript(
                webView: WebView,
                methodName: String,
                list: List<Array<Int>>,
                type: String
        ) {

            val stringBuilder = StringBuilder()
            stringBuilder.append("javascript:try{")
            stringBuilder.append(methodName)
            stringBuilder.append("(")

            stringBuilder.append("[")
            for (i in list.indices) {
                stringBuilder.append("[")
                for (k in list[i].indices) {
                    stringBuilder.append(list[i][k])
                    stringBuilder.append(",")
                }
                stringBuilder.append("]")
                stringBuilder.append(",")
            }
            stringBuilder.append("]")
            stringBuilder.append(",")
            stringBuilder.append("'")
            stringBuilder.append(type)
            stringBuilder.append("'")
            stringBuilder.append(")}catch(error){console.error(error.message);}")
            val call = stringBuilder.toString()
            Log.v("test", call)
            webView.loadUrl(call)
        }

        /**
         * Call javascript functions in webview thread.
         *
         * @param webView    webview object
         * @param methodName function name
         * @param params     function parameters
         */
        fun callOnWebviewThread(webView: WebView, methodName: String, x: Int, y: Int) {
            webView.post { callJavaScript(webView, methodName, x, y) }
        }

        /**
         * Call javascript functions in webview thread.
         *
         * @param webView    webview object
         * @param methodName function name
         * @param params     function parameters
         */
        fun callOnWebviewThread(
                webView: WebView,
                methodName: String,
                x1: Int,
                y1: Int,
                x2: Int,
                y2: Int,
                type: String
        ) {
            webView.post { callJavaScript(webView, methodName, x1, y1, x2, y2, type) }
        }

        fun callOnWebviewThread(
                webView: WebView,
                methodName: String,
                list: List<Array<Int>>,
                type: String
        ) {
            webView.post { callJavaScript(webView, methodName, list, type) }
        }
    }
}