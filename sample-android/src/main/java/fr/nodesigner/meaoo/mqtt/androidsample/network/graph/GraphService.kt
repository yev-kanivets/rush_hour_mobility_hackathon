package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import com.google.gson.annotations.SerializedName
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import kotlin.math.abs
import kotlin.math.min

interface GraphService {

    @GET("processed/subway.json")
    suspend fun getSubwayGraph(): Response<Graph>

    @GET("processed/vehicule.json")
    suspend fun getCarGraph(): Response<Graph>

    data class Graph(
        @SerializedName("elements") val elements: Elements
    )

    data class Elements(
        @SerializedName("nodes") val nodes: List<Node>
    )

    data class Node(
        @SerializedName("position") val position: Coordinate
    )

    @POST("road_graph/shortest_path/{vehicle_type}")
    suspend fun getShortestPath(
        @Path("vehicle_type") vehicleType: String,
        @Body body: Request
    ): Response<Result>

    @POST("road_graph/shortest_path/car")
    suspend fun getShortestPathCar(@Body body: CarRequest): Response<Result>

    data class Request(
        @SerializedName("departure") val departure: Coordinate,
        @SerializedName("arrival") val arrival: Coordinate
    )

    data class CarRequest(
        @SerializedName("departure") val departure: Coordinate,
        @SerializedName("arrival") val arrival: Coordinate,
        @SerializedName("vehicles") val vehicles: List<Vehicle>
    )

    data class Vehicle(
        @SerializedName("x") val x: Double,
        @SerializedName("y") val y: Double,
        @SerializedName("id") val id: String = "id"
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

fun List<List<Double>>.target(initialTarget: Coordinate): Coordinate {
    val coords = map { point -> Coordinate(point[0], point[1]) }
    if (coords.size == 1) return coords[0]

    val a = coords[coords.size - 2]
    val b = coords.last()

    if (abs(dist(a, b) - dist(a, initialTarget)) > EPS) {
        val isVertical = abs(a.y - b.y) > abs(a.x - b.x)
        return if (isVertical) {
            val delta = if (a.y < b.y) 0.5 else -0.5
            val newY = min(6.0, b.y + delta)
            Coordinate(b.x, newY)
        } else {
            val delta = if (a.x < b.x) 0.5 else -0.5
            val newX = min(22.0, b.x + delta)
            Coordinate(newX, b.y)
        }
    }

    return coords.last()
}
