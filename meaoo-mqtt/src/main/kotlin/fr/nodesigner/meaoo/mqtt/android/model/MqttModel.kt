package fr.nodesigner.meaoo.mqtt.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Path(
    val vehicle_type: String?,
    val target: Coordinate?
)

@Parcelize
data class Coordinate(
    val x: Double,
    val y: Double
) : Parcelable

data class Teleport(
    val vehicle_type: String?,
    val costs: List<Double>,
    val path: List<List<Double>>
)
