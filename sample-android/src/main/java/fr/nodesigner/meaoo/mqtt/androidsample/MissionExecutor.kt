package fr.nodesigner.meaoo.mqtt.androidsample

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation

class MissionExecutor(
    var userSituation: UserSituation,
    private val mission: Mission,
    private val listener: Listener
) {

    interface Listener {

        fun onStopped()

        fun onTargetReached()

        fun onMissionCompleted()
    }

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
}
