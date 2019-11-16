package fr.nodesigner.meaoo.mqtt.androidsample

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation
import fr.nodesigner.meaoo.mqtt.androidsample.network.graph.GraphService
import kotlin.math.sqrt

class MissionExecutor(
    var userSituation: UserSituation,
    private val subwayGraph: GraphService.Graph,
    private val mission: Mission,
    private val listener: Listener
) {

    interface Listener {

        fun onStopped()

        fun onTargetReached()

        fun onMissionCompleted()
    }

    private val EPS = 0.1

    var currentTargetIndex = 0
        private set

    val currentTarget: Coordinate
        get() = mission.positions[currentTargetIndex]

    var userStatus: String = "stopped"
        set(value) {
            field = value
            if (userStatus == "stopped") listener.onStopped()
        }

    fun onTargetReached() {
        currentTargetIndex++
        val isMissionCompleted = (currentTargetIndex == mission.positions.size)
        if (isMissionCompleted) {
            listener.onMissionCompleted()
        } else {
            listener.onTargetReached()
        }
    }

    private fun distance(a: Coordinate, b: Coordinate): Double {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }
}
