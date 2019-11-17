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
            subwayOption(request)
        )
    }

    private suspend fun subwayOption(request: GraphService.Request): Option {
        val subwayGraph = GraphClient.getSubwayGraph().body()!!

        val subwayNodes = subwayGraph.elements.nodes.map {
            Coordinate(it.position.x / 1000, it.position.y / 1000)
        }

        return if (subwayNodes.contains(request.departure)) {
            val subwayShortestPath = GraphClient.getShortestPathSubway(request).body()
            if (subwayShortestPath == null) {
                closestMetro(request.departure, subwayNodes)
            } else {
                val path = subwayShortestPath.cars.first()
                val target = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()
                Option.UseMetroToTarget(target, path.pathLength)
            }
        } else {
            closestMetro(request.departure, subwayNodes)
        }
    }

    private suspend fun closestMetro(departure: Coordinate, nodes: List<Coordinate>): Option {
        var minNode: Coordinate = nodes.first()
        var minDist = dist(departure, minNode)

        nodes.forEach {node ->
            if (dist(departure, node) < minDist) {
                minDist = dist(departure, node)
                minNode = node
            }
        }

        val walkPath = GraphClient.getShortestPathWalk(GraphService.Request(departure, minNode)).body()
        val cost = walkPath?.cars?.first()?.pathLength ?: 0.0

        return Option.WalkToSubway(minNode, cost)
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

    private fun dist(a: Coordinate, b: Coordinate): Double {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }
}
