package fr.nodesigner.meaoo.sample

import com.github.kittinunf.result.Result
import fr.nodesigner.meaoo.MeaooApi

fun main() {
    val meaooApi = MeaooApi(namespace = "p65-dev")

    val (_, _, result) = meaooApi.getVehicles()

    when (result) {
        is Result.Failure -> {
            result.getException().printStackTrace()
        }
        is Result.Success -> {
            println(result.get())
        }
    }

    val (_, _, result2) = meaooApi.getVehicleById("0000000000000000")

    when (result2) {
        is Result.Failure -> {
            result2.getException().printStackTrace()
        }
        is Result.Success -> {
            println(result2.get())
        }
    }
}