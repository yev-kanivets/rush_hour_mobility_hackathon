package fr.nodesigner.meaoo.mqtt.androidsample.network.api

import fr.nodesigner.meaoo.mqtt.androidsample.entity.UserSituation
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/user/situation/last")
    suspend fun getLastUserSituation(): Response<UserSituation>
}
