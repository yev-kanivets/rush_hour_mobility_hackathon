package fr.nodesigner.meaoo.mqtt.androidsample.network.vehicle

import com.google.gson.GsonBuilder
import fr.nodesigner.meaoo.mqtt.android.VEHICLE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object VehicleClient {

    private val retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()

        val gsonConverterFactory = GsonConverterFactory
            .create(gsonBuilder)

        return@lazy Retrofit.Builder()
            .baseUrl(VEHICLE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    private val vehicleService by lazy { retrofit.create(VehicleService::class.java) }

    suspend fun getLastVehicleSituation() = vehicleService.getLastVehicleSituation().body()?.firstOrNull()
}
