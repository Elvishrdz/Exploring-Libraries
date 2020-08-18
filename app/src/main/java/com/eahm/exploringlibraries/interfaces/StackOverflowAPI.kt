package com.eahm.exploringlibraries.interfaces

import com.eahm.exploringlibraries.models.Answer
import com.eahm.exploringlibraries.models.Question
import com.eahm.exploringlibraries.utils.ListWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 7. Create the Rest API with the queries to the StackOverflow web service.
 */
interface StackOverflowAPI {

    @GET("/2.2/questions?order=desc&sort=votes&site=stackoverflow&tagged=android&filter=withbody")
    fun getQuestions() : Call<ListWrapper<Question>>

    @GET("/2.2/questions/{id}/answers?order=desc&sort=votes&site=stackoverflow")
    fun getAnswers(@Path("id") questionId : String) : Call<ListWrapper<Answer>>

}