package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import com.google.gson.annotations.SerializedName
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface GraphService {

    @POST("road_graph/shortest_path/{vehicle_type}")
    suspend fun getShortestPath(
        @Path("vehicle_type") vehicleType: String,
        @Body body: Request
    ): Response<Result>

    data class Request(
        @SerializedName("departure") val departure: Coordinate,
        @SerializedName("arrival") val arrival: Coordinate
    )

    data class Result(
        @SerializedName("cars") val cars: List<ResultItem>
    )

    data class ResultItem(
        @SerializedName("id") val vehicleType: String,
        @SerializedName("paths") val paths: List<List<Double>>,
        @SerializedName("costs") val costs: List<Double>,
        @SerializedName("path_length") val pathLength: Double
    )
}
