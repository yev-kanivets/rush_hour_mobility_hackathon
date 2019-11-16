package fr.nodesigner.meaoo

import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.result.Result
import fr.nodesigner.meaoo.model.Vehicle
import okhttp3.OkHttpClient

class MeaooApi(private val namespace: String) {

    var vehiculeHostPrefix: String = "vehicle"
    var graphHostPrefix: String = "graph"
    var contextHostPrefix: String = "context-controller"
    var agentHostPrefix: String = "agent-controller"
    var hostSuffix = "xp65.renault-digital.com"

    var httpClient = OkHttpClient()

    val manager = FuelManager()

    init {
        manager.basePath = "http://localhost"
        createEnvironment()
    }

    inline fun <reified T : Any> processCloudRequestSync(request: Request, json: Boolean = true): Triple<Request, Response, Result<T, FuelError>> {
        return if (json) {
            request.responseObject(gsonDeserializer())
        } else {
            request.response() as Triple<Request, Response, Result<T, FuelError>>
        }
    }

    fun buildVehiclesRequest(id: String? = null): Request {
        val path = if (id == null) {
            "http://$vehiculeHostPrefix.$namespace.$hostSuffix/api/v1/vehicles"
        } else {
            "http://$vehiculeHostPrefix.$namespace.$hostSuffix/api/v1/vehicles/$id"
        }
        return manager.request(method = Method.GET, path = path)
    }

    fun getVehicles(): Triple<Request, Response, Result<List<Vehicle>, FuelError>> {
        return processCloudRequestSync(request = buildVehiclesRequest())
    }

    fun getVehicleById(id: String): Triple<Request, Response, Result<Vehicle, FuelError>> {
        return processCloudRequestSync(request = buildVehiclesRequest(id))
    }
}