package fr.nodesigner.meaoo.mqtt.androidsample.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.android.TOPIC
import fr.nodesigner.meaoo.mqtt.android.listener.IMessageCallback
import fr.nodesigner.meaoo.mqtt.android.model.Path
import fr.nodesigner.meaoo.mqtt.androidsample.MissionExecutor
import fr.nodesigner.meaoo.mqtt.androidsample.Singleton
import fr.nodesigner.meaoo.mqtt.androidsample.adapter.PathAdapter
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserStatus
import fr.nodesigner.meaoo.mqtt.androidsample.network.api.ApiClient
import fr.nodesigner.meaoo.mqtt.androidsample.network.graph.GetShortestPathsInteractor
import fr.nodesigner.meaoo.mqtt.androidsample.network.graph.GraphClient
import fr.nodesigner.meaoo.mqtt.androidsample.network.graph.GraphService
import kotlinx.android.synthetic.main.main_activity.recyclerView
import kotlinx.android.synthetic.main.main_activity.tvPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : Activity(), MissionExecutor.Listener {

    val TAG = "Olala"
    val MISSION_REQUEST = 42

    private val presenterJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + presenterJob)

    private lateinit var adapter: PathAdapter

    private var missionExecutor: MissionExecutor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initRecycler()
        connect()
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = PathAdapter(this@MainActivity) { selectedPath ->
            val vehicleType = selectedPath.transport.string
            val target = selectedPath.paths.last()

            Singleton.publishAgentPath(Path(vehicleType, target))
            adapter.paths = listOf()
        }
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                MISSION_REQUEST -> presentOptionsToUser()
            }
        }
    }

    private fun connect() {
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
                val jsonString = String(mqttMessage.payload)
                when (topic) {
                    TOPIC.USER_SITUATION.path -> userSituationUpdate(jsonString)
                    TOPIC.USER_MISSION.path, TOPIC.USER_MISSION_DEV.path -> {
                        userMissionUpdate(jsonString)
                    }
                    TOPIC.USER_STATUS.path -> userStatusUpdate(jsonString)
                    TOPIC.OBJECTIVE_REACHED.path -> objectiveReached()
                    else -> Log.v(TAG, "[$topic] $jsonString")
                }
            }

            override fun deliveryComplete(messageToken: IMqttDeliveryToken) {
                Log.v(TAG, "in delivery complete ${messageToken.message}")
            }

            override fun onConnectionSuccess(token: IMqttToken) {
                Toast.makeText(this@MainActivity, "connnected to server", Toast.LENGTH_SHORT).show()
                Singleton.subscribeToAllTopics()
            }

            override fun onConnectionFailure(token: IMqttToken, throwable: Throwable) {
                var errorMessage = "connection error"
                if (throwable.message != null) {
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

        Singleton.setupApplication(applicationContext, false, true)
        Singleton.connect()
    }

    private val gson = Gson()

    private fun userSituationUpdate(jsonString: String) {
        val userSituation = gson.fromJson<UserSituation>(jsonString, UserSituation::class.java)
        missionExecutor?.userSituation = userSituation
        drawUserSituation(userSituation)
    }

    private fun drawUserSituation(userSituation: UserSituation) {
        tvPosition.text = "x: ${userSituation.position.x}, y: ${userSituation.position.y}"
    }

    private fun userMissionUpdate(jsonString: String) {
        val mission = gson.fromJson<Mission>(jsonString, Mission::class.java)
        uiScope.launch {
            val userSituation = ApiClient.getLastUserSituation().body()!!
            val subwayGraph = GraphClient.getSubwayGraph().body()!!

            missionExecutor =
                MissionExecutor(userSituation, subwayGraph, mission, this@MainActivity)
            drawUserSituation(userSituation)
            val intent = MissionActivity.newIntent(this@MainActivity, mission)
            startActivityForResult(intent, MISSION_REQUEST)
        }
    }

    private fun userStatusUpdate(jsonString: String) {
        val status = gson.fromJson<UserStatus>(jsonString, UserStatus::class.java)
        missionExecutor?.apply {
            userSituation = status.userSituation
            userStatus = status.status
        }
    }

    private fun objectiveReached() {
        missionExecutor?.onTargetReached()
    }

    override fun onStopped() {
        presentOptionsToUser()
    }

    override fun onTargetReached() {
        presentOptionsToUser()
    }

    override fun onMissionCompleted() {
        missionExecutor = null
    }

    private fun presentOptionsToUser() {
        val userPosition = missionExecutor?.userSituation?.position ?: return
        val targetPosition = missionExecutor?.currentTarget ?: return

        val request = GraphService.Request(userPosition, targetPosition)
        val getShortestPaths = GetShortestPathsInteractor()

        uiScope.launch {
            adapter.paths = getShortestPaths.execute(request)
        }
    }
}
