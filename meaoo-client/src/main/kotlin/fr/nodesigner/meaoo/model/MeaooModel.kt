package fr.nodesigner.meaoo.model

data class Vehicle(
        val id: String?,
        val attitude: Attitude?,
        val battery: Float?,
        val available: Boolean?
)

data class Attitude(
        val id: String?,
        val position: Position?,
        val orientation: Float?,
        val speed: Float?
)

data class Position(
        val x: Float?,
        val y: Float?
)