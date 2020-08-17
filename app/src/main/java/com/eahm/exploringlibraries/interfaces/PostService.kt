package com.eahm.exploringlibraries.interfaces

import com.eahm.exploringlibraries.models.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {

    /**
     * 5. Retrofit turns your http api into a java interface
     *
     * The @GET value will be added to our BaseURL (https://jsonplaceholder.typicode.com/posts?userId=2)
     * The @Query value define a variable to be replaced in the @Get value string.
     */
    /**
     * Every method must have an HTTP annotation that provides the request method and
     * relative URL. There are eight built-in annotations: HTTP, GET, POST, PUT, PATCH,
     * DELETE, OPTIONS and HEAD. The relative URL of the resource is specified in the
     * annotation.
     *
     * You can also specify query parameters in the URL. e.g: @GET("users/list?sort=desc")
     *
     */
    @GET("posts/")
    fun listPosts(@Query("userId") id : Int) : Call<List<Post>>
    /**
     * 7. Call<List<Post>> -> Each 'Call' from the created PostService can make a
     *    synchronous or asynchronous HTTP request to the remote web  server.
     */

}