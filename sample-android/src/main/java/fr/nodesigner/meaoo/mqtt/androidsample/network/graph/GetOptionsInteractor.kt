package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Option
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport
import kotlin.math.sqrt

class GetOptionsInteractor {

    suspend fun execute(request: GraphService.Request): List<Option> {
        val list = listOfNotNull(
            GraphClient.getShortestPathWalk(request).body(),
            GraphClient.getShortestPathBike(request).body(),
            GraphClient.getShortestPathCar(request).body()
        )

        return listOf(
            GetSubwayOptionInteractor().execute(request)
        )
    }

    private fun mapTransport(input: GraphService.ResultItem): Transport {
        return when (input.vehicleType) {
            Transport.WALK.string -> Transport.WALK
            Transport.SUBWAY.string -> Transport.SUBWAY
            Transport.BIKE.string -> Transport.BIKE
            Transport.CAR.string -> Transport.CAR
            else -> throw IllegalArgumentException()
        }
    }
}

fun dist(a: Coordinate, b: Coordinate): Double {
    return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
}
