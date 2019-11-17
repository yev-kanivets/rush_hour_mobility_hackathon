package fr.nodesigner.meaoo.mqtt.androidsample.entity

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate

sealed class Option(
    open val coordinate: Coordinate,
    open val cost: Double,
    open val transport: Transport
) {

    data class WalkToTarget(
        override val coordinate: Coordinate,
        override val cost: Double
    ) : Option(coordinate, cost, Transport.WALK)

    data class BikeToTarget(
        override val coordinate: Coordinate,
        override val cost: Double
    ) : Option(coordinate, cost, Transport.BIKE)

    data class WalkToSubway(
        override val coordinate: Coordinate,
        override val cost: Double
    ) : Option(coordinate, cost, Transport.WALK)

    data class CallTaxi(
        override val coordinate: Coordinate,
        override val cost: Double
    ) : Option(coordinate, cost, Transport.CAR)

    data class UseTaxiToTarget(
        override val coordinate: Coordinate,
        override val cost: Double
    ) : Option(coordinate, cost, Transport.CAR)

    data class UseMetroToTarget(
        override val coordinate: Coordinate,
        override val cost: Double
    ) : Option(coordinate, cost, Transport.SUBWAY)
}
