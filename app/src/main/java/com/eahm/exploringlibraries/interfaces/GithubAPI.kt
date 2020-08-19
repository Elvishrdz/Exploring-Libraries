package com.eahm.exploringlibraries.interfaces

import com.eahm.exploringlibraries.models.GithubIssue
import com.eahm.exploringlibraries.models.GithubRepo
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 7. Create the Rest API
 */
interface GithubAPI {

    /**
     * Using Single operator from RxJava
     */
    @GET("user/repos?per_page=100")
    fun getRepos() : Single<List<GithubRepo>>

    @GET("/repos/{owner}/{repo}/issues")
    fun getIssues(
        @Path("owner") owner : String,
        @Path("repo") repository : String
    ) : Single<List<GithubIssue>>

    /**
     * Using @Url and @Body from retrofit2 http
     *
     * With the @Url annotation we can provide the URL for this request.
     * This allows us to change the URL for each request dynamically. We
     * need this for the comments_url field of the GithubIssue class.
     */
    @POST
    fun postComment(
        @Url url : String,
        @Body issue : GithubIssue
    ) : Single<ResponseBody>

}