package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.adapter.PathAdapter
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport

class GetShortestPathsInteractor {

    suspend fun execute(request: GraphService.Request): List<PathAdapter.Path> {
        val subwayGraph = GraphClient.getSubwayGraph().body()!!
        val subwayNodes = subwayGraph.elements.nodes.map {
            Coordinate(it.position.x / 1000, it.position.y / 1000)
        }

        val list = listOfNotNull(
            GraphClient.getShortestPathWalk(request).body(),
            GraphClient.getShortestPathSubway(request).body(),
            GraphClient.getShortestPathBike(request).body(),
            GraphClient.getShortestPathCar(request).body()
        )

        return list.map {
            val input = it.cars.first()

            val transport = when (input.vehicleType) {
                Transport.WALK.string -> Transport.WALK
                Transport.SUBWAY.string -> Transport.SUBWAY
                Transport.BIKE.string -> Transport.BIKE
                Transport.CAR.string -> Transport.CAR
                else -> throw IllegalArgumentException()
            }

            val paths = input.paths.map { point -> Coordinate(point[0], point[1]) }
            val types = paths.map { coordinate ->
                if (input.vehicleType == Transport.SUBWAY.string && subwayNodes.contains(coordinate)) {
                    Transport.SUBWAY
                } else {
                    Transport.WALK
                }
            }

            val elements = paths.mapIndexed { index, element ->
                PathAdapter.PathItem(element, input.costs[index], types[index])
            }

            PathAdapter.Path(transport, elements)
        }
    }
}
