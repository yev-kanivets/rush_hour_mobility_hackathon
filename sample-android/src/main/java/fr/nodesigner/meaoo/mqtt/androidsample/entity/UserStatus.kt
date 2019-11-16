package fr.nodesigner.meaoo.mqtt.androidsample.entity

import com.google.gson.annotations.SerializedName

data class UserStatus(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("situation") val userSituation: UserSituation
)
