package fr.nodesigner.meaoo.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vehicle(
        val id: String?,
        val attitude: Attitude?,
        val battery: Float?,
        val available: Boolean?
) : Parcelable

@Parcelize
data class Attitude(
        val id: String?,
        val position: Position?,
        val orientation: Float?,
        val speed: Float?
) : Parcelable

@Parcelize
data class Position(
        val x: Float?,
        val y: Float?
) : Parcelable