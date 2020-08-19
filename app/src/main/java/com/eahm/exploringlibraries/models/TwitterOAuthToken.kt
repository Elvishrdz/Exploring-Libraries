package com.eahm.exploringlibraries.models

import com.google.gson.annotations.SerializedName

/**
 * 4. The TwitterOAuthToken class store the token requested to the
 * TwitterAPI. It contains the consumer key and secret.
 */
data class TwitterOAuthToken(
    @SerializedName("access_token") var accessToken : String,
    @SerializedName("token_type") var tokenType : String
){
    val authorization : String
        get() = "$tokenType $accessToken"
}

