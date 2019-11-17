package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Option

class GetSubwayOptionInteractor {

    val EPS = 0.05

    suspend fun execute(request: GraphService.Request): Option {
        val subwayGraph = GraphClient.getSubwayGraph().body()!!

        val subwayNodes = subwayGraph.elements.nodes.map {
            Coordinate(it.position.x / 1000, it.position.y / 1000)
        }

        val isOnSubwayStation = subwayNodes.count { dist(it, request.departure) < EPS } > 0

        return if (isOnSubwayStation) {
            val subwayShortestPath = GraphClient.getShortestPathSubway(request).body()
            if (subwayShortestPath == null) {
                closestMetro(request.departure, subwayNodes)
            } else {
                val path = subwayShortestPath.cars.first()
                val target = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()
                Option.UseSubwayToTarget(target, path.pathLength)
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
}
