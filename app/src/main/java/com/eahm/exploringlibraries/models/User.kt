package com.eahm.exploringlibraries.models

/**
 * 4. The user class must match the JSON response:
{
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    "address": {
        "street": "Kulas Light",
        "suite": "Apt. 556",
        "city": "Gwenborough",
        "zipcode": "92998-3874",
        "geo": {
            "lat": "-37.3159",
            "lng": "81.1496"
        }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
        "name": "Romaguera-Crona",
        "catchPhrase": "Multi-layered client-server neural-net",
        "bs": "harness real-time e-markets"
    }
},
 */
data class User(
    val id : Int = -1,
    val name : String = "",
    val userName : String = "",
    val email : String= "",
    val address : Address = Address(),
    val phone : String= "",
    val website : String = "",
    val company : Company = Company()
)

data class Address(
    val street : String= "",
    val suite : String= "",
    val city : String= "",
    val zipCode : String= "",
    val geo : GeoLocation = GeoLocation()
)

data class GeoLocation(
    val lat : Double = 0.0,
    val lng : Double = 0.0
)

data class Company(
    val name : String = "",
    val catchPhrase : String = "",
    val bs : String = ""
)