package com.eahm.exploringlibraries.interfaces

import com.eahm.exploringlibraries.models.TwitterOAuthToken
import com.eahm.exploringlibraries.models.TwitterUser
import retrofit2.Call
import retrofit2.http.*

interface TwitterAPI {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun postCredentials(@Field("grant_type") grantType : String) : Call<TwitterOAuthToken>

    @GET("/1.1/users/show.json")
    fun getUser(@Query("screen_name") name : String) : Call<TwitterUser>
}