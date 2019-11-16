package fr.nodesigner.meaoo.androidsample

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.github.kittinunf.result.Result
import fr.nodesigner.meaoo.MeaooApi
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : Activity() {

    private lateinit var meaooApi: MeaooApi

    private lateinit var mHandler: Handler

    private lateinit var mExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        meaooApi = MeaooApi(namespace = "p65-dev")
        mHandler = Handler()
        mExecutor = Executors.newSingleThreadExecutor()
        mExecutor.execute {
            val (_, _, result) = meaooApi.getVehicles()

            when (result) {
                is Result.Failure -> {
                    result.getException().printStackTrace()
                }
                is Result.Success -> {
                    println(result.get())
                }
            }
        }
    }
}