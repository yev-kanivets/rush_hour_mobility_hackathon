package fr.nodesigner.meaoo.mqtt.androidsample.entity

import fr.nodesigner.meaoo.mqtt.android.model.Coordinate

data class RoadsStatus(
        val car: List<Edge>,
        val bike: List<Edge>,
        val walk: List<Edge>
)

data class Edge(
        val id: String,
        val locations: Location,
        val state: String
)

data class Location(
        val from: Coordinate,
        val to: Coordinate
)

data class TrafficCondition(
        val id: String,
        val locations: Location,
        val slowing_factor: Int
)
/*
[
{
    "car": [
    {
        "id":"edge_10",
        "locations": {
        "from":{"x":0.2,"y":0.8},
        "to":{"x":0.2,"y":1.8}
    },
        "state":"close"
    },
    {
        "id":"edge_50",
        "locations": {
        "from":{"x":1.2,"y":0.8},
        "to":{"x":1.2,"y":1.8}
    },
        "state":"open"
    }
    ]
},
{
    "bike": [
    {
        "id":"edge_10",
        "locations": {
        "from":{"x":0.2,"y":0.8},
        "to":{"x":0.2,"y":1.8}
    },
        "state":"close"
    },
    {
        "id":"edge_50",
        "locations": {
        "from":{"x":1.2,"y":0.8},
        "to":{"x":1.2,"y":1.8}
    },
        "state":"open"
    }
    ]
},
{
    "walk": [
    {
        "id":"edge_10",
        "locations": {
        "from":{"x":0.2,"y":0.8},
        "to":{"x":0.2,"y":1.8}
    },
        "state":"close"
    },
    {
        "id":"edge_50",
        "locations": {
        "from":{"x":1.2,"y":0.8},
        "to":{"x":1.2,"y":1.8}
    },
        "state":"open"
    }
    ]
}
]
 */