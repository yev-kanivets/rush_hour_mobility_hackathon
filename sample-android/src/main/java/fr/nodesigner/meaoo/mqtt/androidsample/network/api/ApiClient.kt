package fr.nodesigner.meaoo.mqtt.androidsample.network.api

import com.google.gson.GsonBuilder
import fr.nodesigner.meaoo.mqtt.android.API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()

        val gsonConverterFactory = GsonConverterFactory
            .create(gsonBuilder)

        return@lazy Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    private val graphService by lazy { retrofit.create(ApiService::class.java) }

    suspend fun getLastUserSituation() = graphService.getLastUserSituation()
}
