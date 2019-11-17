package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import fr.nodesigner.meaoo.mqtt.androidsample.entity.Option
import fr.nodesigner.meaoo.mqtt.androidsample.network.vehicle.VehicleClient

class GetCarOptionInteractor {

    suspend fun execute(request: GraphService.Request): Option? {
        val carSituation =
            VehicleClient.getLastVehicleSituation()?.attitude?.position ?: return null
        val isOnCar = dist(carSituation, request.departure) < EPS

        if (isOnCar) {
            // val pathCar = GraphClient.getShortestPathCar(request).body() ?: return null

            // val path = pathCar.cars.first()
            // val target = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()

            return Option.UseTaxiToTarget(request.arrival, 0.0)
        } else {
            // val request = GraphService.Request(carSituation, request.departure)
            // val pathCar = GraphClient.getShortestPathCar(request).body() ?: return null

            // val path = pathCar.cars.first()
            // val target = path.paths.map { point -> Coordinate(point[0], point[1]) }.last()

            return Option.CallTaxi(request.departure, 0.0)
        }
    }
}
