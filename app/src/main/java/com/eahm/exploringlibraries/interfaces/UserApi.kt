package com.eahm.exploringlibraries.interfaces

import com.eahm.exploringlibraries.models.User
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

/**
 * 5. Create the rest API for Retrofit
 */
interface UserApi {

    @GET("users")
    fun getUsers() : Observable<List<User>>
    /**
     * Observable. Here we use an RxJava operator to
     * obtain the HTTP response
     * Check the import is the correct to use the AndroidScheduler.mainThread() for
     * that we need the RxJava3 Observable:
     *    io.reactivex.rxjava3.core.Observable
     */
}