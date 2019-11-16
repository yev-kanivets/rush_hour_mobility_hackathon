package fr.nodesigner.meaoo.mqtt.androidsample.network.graph

import com.google.gson.GsonBuilder
import fr.nodesigner.meaoo.mqtt.android.GRAPH_URL
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GraphClient {

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()

        val gsonConverterFactory = GsonConverterFactory
            .create(gsonBuilder)

        return@lazy Retrofit.Builder()
            .baseUrl(GRAPH_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    private val graphService by lazy { retrofit.create(GraphService::class.java) }

    suspend fun getSubwayGraph() = graphService.getSubwayGraph()

    suspend fun getShortestPathWalk(request: GraphService.Request) =
        graphService.getShortestPath(Transport.WALK.string, request)

    suspend fun getShortestPathSubway(request: GraphService.Request) =
        graphService.getShortestPath(Transport.SUBWAY.string, request)

    suspend fun getShortestPathBike(request: GraphService.Request) =
        graphService.getShortestPath(Transport.BIKE.string, request)

    suspend fun getShortestPathCar(request: GraphService.Request) =
        graphService.getShortestPath(Transport.CAR.string, request)
}
