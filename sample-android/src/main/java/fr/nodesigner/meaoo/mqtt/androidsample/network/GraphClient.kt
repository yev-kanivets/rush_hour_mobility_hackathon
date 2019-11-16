package fr.nodesigner.meaoo.mqtt.androidsample.network

import com.google.gson.GsonBuilder
import fr.nodesigner.meaoo.mqtt.android.GRAPH_URL
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

    private val codeforcesApi by lazy { retrofit.create(GraphService::class.java) }

    suspend fun getShortestPathSubway(request: GraphService.Request) =
        codeforcesApi.getShortestPathSubway(request)
}
