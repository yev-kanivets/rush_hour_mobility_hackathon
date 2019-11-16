package fr.nodesigner.meaoo

interface Environment

fun createEnvironment(): Environment = try {
    Class.forName(AndroidEnvironmentClass).newInstance() as Environment
} catch (exception: ClassNotFoundException) {
    DefaultEnvironment()
}

class DefaultEnvironment : Environment

const val AndroidEnvironmentClass = "fr.nodesigner.meaoo.android.AndroidEnvironment"