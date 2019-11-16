package fr.nodesigner.meaoo.mqtt.androidsample.network

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.adapter.PathAdapter
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport

class GetShortestPathsInteractor {

    suspend fun execute(request: GraphService.Request) = listOfNotNull(
        GraphClient.getShortestPathWalk(request).body(),
        GraphClient.getShortestPathSubway(request).body(),
        GraphClient.getShortestPathBike(request).body(),
        GraphClient.getShortestPathCar(request).body()
    ).map {
        val input = it.cars.first()

        val transport = when (input.vehicleType) {
            Transport.WALK.string -> Transport.WALK
            Transport.SUBWAY.string -> Transport.SUBWAY
            Transport.BIKE.string -> Transport.BIKE
            Transport.CAR.string -> Transport.CAR
            else -> throw IllegalArgumentException()
        }

        val paths = input.paths.map { point -> Coordinate(point[0], point[1]) }

        PathAdapter.Path(transport, paths, input.costs)
    }
}
