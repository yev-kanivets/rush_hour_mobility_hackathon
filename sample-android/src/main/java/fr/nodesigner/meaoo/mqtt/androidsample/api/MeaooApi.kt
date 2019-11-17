package fr.nodesigner.meaoo.mqtt.androidsample.api

import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.result.Result
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Edge
import fr.nodesigner.meaoo.mqtt.androidsample.entity.TrafficCondition
import okhttp3.OkHttpClient

data class Vehicle(
        val id: String?,
        val attitude: Attitude,
        val battery: Float?,
        val available: Boolean?
)

data class Attitude(
        val id: String?,
        val position: Position,
        val orientation: Float?,
        val speed: Float?
)

data class Position(
        val x: Float,
        val y: Float
)

class MeaooApi(private val namespace: String) {

    var vehiculeHostPrefix: String = "vehicle"
    var graphHostPrefix: String = "graph"
    var contextHostPrefix: String = "context-controller"
    var agentHostPrefix: String = "agent-controller"
    var hostSuffix = "xp65.renault-digital.com"

    val manager = FuelManager().apply {
        timeoutReadInMillisecond = 30000
    }

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

    fun buildRoadStatusRequest(type: String): Request {
        val path = "http://$graphHostPrefix.$namespace.$hostSuffix/road_graph/roads_status/$type"
        return manager.request(method = Method.GET, path = path)
    }

    fun buildMetroStatusRequest(): Request {
        val path = "http://$graphHostPrefix.$namespace.$hostSuffix/road_graph/line_state"
        return manager.request(method = Method.GET, path = path)
    }

    fun buildTrafficConditionsRequest(): Request {
        val path = "http://$graphHostPrefix.$namespace.$hostSuffix/road_graph/traffic_conditions"
        return manager.request(method = Method.GET, path = path)
    }

    fun buildVehicleRequest(): Request {
        val path = "http://$vehiculeHostPrefix.$namespace.$hostSuffix/api/v1/vehicles"
        return manager.request(method = Method.GET, path = path)
    }

    fun getRoadStatus(type: String): Triple<Request, Response, Result<List<Edge>, FuelError>> {
        return processCloudRequestSync(request = buildRoadStatusRequest(type))
    }

    fun getMetroStatus(): Triple<Request, Response, Result<List<Edge>, FuelError>> {
        return processCloudRequestSync(request = buildMetroStatusRequest())
    }

    fun getTrafficConditions(): Triple<Request, Response, Result<List<TrafficCondition>, FuelError>> {
        return processCloudRequestSync(request = buildTrafficConditionsRequest())
    }
    fun getVehicles(): Triple<Request, Response, Result<List<Vehicle>, FuelError>> {
        return processCloudRequestSync(request = buildVehicleRequest())
    }
}
