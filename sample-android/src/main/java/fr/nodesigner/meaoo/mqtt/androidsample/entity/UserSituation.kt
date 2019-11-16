package fr.nodesigner.meaoo.mqtt.androidsample.entity

import com.google.gson.annotations.SerializedName
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate

data class UserSituation(
    @SerializedName("vehicle_type") val vehicleType: String,
    @SerializedName("position") val position: Coordinate,
    @SerializedName("cost_per_step") val costPerStep: Double,
    @SerializedName("total_cost") val totalCost: Double
)
