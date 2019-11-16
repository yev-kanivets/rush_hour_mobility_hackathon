package fr.nodesigner.meaoo.mqtt.androidsample

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation
import kotlin.math.sqrt

class MissionExecutor(
    private val mission: Mission,
    private val onTargetReached: (Boolean) -> Unit
) {

    private val EPS = 0.1

    var currentTargetIndex = 0
        private set

    val currentTarget: Coordinate
        get() = mission.positions[currentTargetIndex]

    var userSituation: UserSituation = UserSituation(
        Transport.WALK.string,
        Coordinate(0.0, 0.0),
        0.0,
        0.0
    )

    var userStatus: String = "stopped"
        set(value) {
            field = value

            val currentTarget = mission.positions[currentTargetIndex]
            val distanceToTarget = distance(currentTarget, userSituation.position)

            val isTargetReached = distanceToTarget < EPS && userStatus == "stopped"
            if (isTargetReached) {
                currentTargetIndex++
                val isMissionCompleted = (currentTargetIndex == mission.positions.size)
                onTargetReached(isMissionCompleted)
            }
        }

    private fun distance(a: Coordinate, b: Coordinate): Double {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }
}
