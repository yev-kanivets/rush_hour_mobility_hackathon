package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Option
import kotlin.math.sqrt

class GetOptionsInteractor {

    suspend fun execute(request: GraphService.Request): List<Option> {
        val list = listOfNotNull(
            GraphClient.getShortestPathCar(request).body()
        )

        return listOfNotNull(
            getWalkOption(request),
            getBikeOption(request),
            GetSubwayOptionInteractor().execute(request)
        )
    }

    private suspend fun getWalkOption(request: GraphService.Request): Option? {
        val pathWalk = GraphClient.getShortestPathWalk(request).body() ?: return null
        val path = pathWalk.cars.first()
        val target = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()
        return Option.WalkToTarget(target, path.pathLength)
    }

    private suspend fun getBikeOption(request: GraphService.Request): Option? {
        val pathBike = GraphClient.getShortestPathBike(request).body() ?: return null
        val path = pathBike.cars.first()
        val target = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()
        return Option.BikeToTarget(target, path.pathLength)
    }
}

fun dist(a: Coordinate, b: Coordinate): Double {
    return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
}
