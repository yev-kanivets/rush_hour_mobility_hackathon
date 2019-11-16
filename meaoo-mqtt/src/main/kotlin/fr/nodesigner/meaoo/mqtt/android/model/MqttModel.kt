package fr.nodesigner.meaoo.mqtt.android.model

data class Path(
    val vehicle_type: String?,
    val target: Coordinate?
)

data class Coordinate(
    val x: Double,
    val y: Double
)

data class Teleport(
    val vehicle_type: String?,
    val costs: List<Double>,
    val path: List<List<Double>>
)
