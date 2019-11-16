package fr.nodesigner.meaoo.mqtt.android.model

data class Path(
        val vehicle_type: String?,
        val target: Coordinate?
)

data class Coordinate(
        val x: Float,
        val y: Float
)

data class Teleport(
        val vehicle_type: String?,
        val costs: List<Float>,
        val path: List<List<Float>>
)