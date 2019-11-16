package fr.nodesigner.meaoo.mqtt.androidsample.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mission(
    @SerializedName("mission") val mission: String,
    @SerializedName("positions") val positions: List<Coordinate>
) : Parcelable
