package fr.nodesigner.meaoo.mqtt.androidsample.entity

import com.google.gson.annotations.SerializedName
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate

data class Mission(
    @SerializedName("positions") val mission: String,
    @SerializedName("positions") val positions: List<Coordinate>
)
