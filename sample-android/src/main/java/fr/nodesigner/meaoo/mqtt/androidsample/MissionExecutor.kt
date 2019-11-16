package fr.nodesigner.meaoo.mqtt.androidsample

import android.util.Log
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Mission
import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation
import kotlin.math.sqrt

class MissionExecutor(
    private val mission: Mission
) {

    var nextTarget: Coordinate = mission.positions.first()
        private set

    var userSituation: UserSituation = UserSituation("walk", Coordinate(0.0, 0.0), 0.0, 0.0)
        set(value) {
            field = value
            Log.d("FUCK", "${distance(nextTarget, value.position)}")
        }

    private fun distance(a: Coordinate, b: Coordinate): Double {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }
}
