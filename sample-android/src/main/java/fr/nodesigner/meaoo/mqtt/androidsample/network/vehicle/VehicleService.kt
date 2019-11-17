package fr.nodesigner.meaoo.mqtt.androidsample.network.vehicle

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import retrofit2.Response
import retrofit2.http.GET

interface VehicleService {

    @GET("api/v1/vehicles")
    suspend fun getLastVehicleSituation(): Response<List<Result>>

    data class Result(
        val attitude: Attitude
    )

    data class Attitude(
        val position: Coordinate
    )
}
