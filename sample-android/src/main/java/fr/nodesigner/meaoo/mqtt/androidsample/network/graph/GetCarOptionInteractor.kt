package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Option
import fr.nodesigner.meaoo.mqtt.androidsample.network.vehicle.VehicleClient

class GetCarOptionInteractor {

    suspend fun execute(request: GraphService.Request): Option? {
        val carGraph = GraphClient.getCarGraph().body()!!

        val carNodes = carGraph.elements.nodes.map {
            Coordinate(it.position.x / 1000, it.position.y / 1000)
        }

        val carSituation =
            VehicleClient.getLastVehicleSituation()?.attitude?.position ?: return null
        val isOnCar = dist(carSituation, request.departure) < EPS
        val vehicles = listOf(GraphService.Vehicle(carSituation.x, carSituation.y))


        if (isOnCar) {
            val target = closestCarNode(request.arrival, carNodes)

            val request = GraphService.CarRequest(carSituation, target, vehicles)
            val pathCar = GraphClient.getShortestPathCar(request).body() ?: return null

            val path = pathCar.cars.first()
            val finalTarget = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()

            return Option.UseTaxiToTarget(finalTarget, path.pathLength)
        } else {
            val target = closestCarNode(request.departure, carNodes)

            val request = GraphService.CarRequest(carSituation, target, vehicles)
            val pathCar = GraphClient.getShortestPathCar(request).body() ?: return null

            val path = pathCar.cars.first()
            val finalTarget = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()

            return Option.CallTaxi(finalTarget, path.pathLength)
        }
    }

    private fun closestCarNode(target: Coordinate, nodes: List<Coordinate>): Coordinate {
        var minNode: Coordinate = nodes.first()
        var minDist = dist(target, minNode)

        nodes.forEach { node ->
            if (dist(target, node) < minDist) {
                minDist = dist(target, node)
                minNode = node
            }
        }

        return minNode
    }
}
