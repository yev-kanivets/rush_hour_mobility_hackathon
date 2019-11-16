package fr.nodesigner.meaoo.mqtt.androidsample

import android.util.Log
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation
import kotlin.math.sqrt

class MissionExecutor(
    private val mission: Mission
) {

    var nextTarget: Coordinate = mission.positions.first()
        private set

    var userSituation: UserSituation = UserSituation(
        Transport.WALK.string,
        Coordinate(0.0, 0.0),
        0.0,
        0.0
    )

    var userStatus: String = "stopped"
        set(value) {
            field = value
            Log.d(
                "FUCK",
                "User stopped at x: ${userSituation.position.x}, y: ${userSituation.position.y}, dist: ${distance(
                    nextTarget,
                    userSituation.position
                )}"
            )
        }

    private fun distance(a: Coordinate, b: Coordinate): Double {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }
}
