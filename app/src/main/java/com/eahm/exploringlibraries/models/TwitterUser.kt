package com.eahm.exploringlibraries.models

/**
 * 4. TwitterUser stores some (or all) the user fields provided by
 * the TwitterAPI.
 */
data class TwitterUser(
    var name : String,
    var location : String,
    var description : String,
    var url : String
)