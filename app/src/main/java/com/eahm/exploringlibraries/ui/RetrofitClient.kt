package com.eahm.exploringlibraries.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.interfaces.PostService
import com.eahm.exploringlibraries.models.Post
import com.eahm.exploringlibraries.utils.BASE_URL
import com.eahm.exploringlibraries.utils.Util
import kotlinx.android.synthetic.main.activity_retrofit_client.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 8. Implement the Callback<List<Post>> (from retrofit2.Callback)
 *  This must match the give value of our 'Call' in the PostService.kt interface.
 */
class RetrofitClient : AppCompatActivity(), Callback<List<Post>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_client)

        /**
         * Getting Started
         * 1. add the retrofit library dependencies. Visit https://square.github.io/retrofit/ for more
         *    information.
         *    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
         * 1.1 Retrofit requires at minimum Java 8+ or Android API 21+
         *     Add the Java8 version to the project (See build.gradle)
         * 1.2 Add the GSON Converter dependency to deserialize from JSON data.
         * 1.3 Add the Internet permissions to the AndroidManifest.xml
         *
         *** We will use https://jsonplaceholder.typicode.com/ as our web service.
         *
         * 2. Create a constant class and define the BaseURL for our web service.
         * 3. Build the Retrofit instance (See below)
         *  3.1 Define the ConverterFactory to deserialize from JSON using GSON Converter Factory
         * 4. Create a Post model to parse the JSON response (See Post.kt)
         * 5. Create an interface for our service. (See PostService.kt)
         * 6. Let Retrofit generate an implementation of the 'PostService' interface
         * 7. Prepare the 'call' to be added to the retrofit queue
         * 8. Implement the Callback<List<Post>> (from retrofit2.Callback)
         *  8.1 Implement the onResponse and the onFailure methods.
         *
         * 9. Execute our request.
         *
         * Done!
         */

        /**
         * 3. Create the Retrofit instance
         */
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            /**
             * 3.1 Define the ConverterFactory to deserialize from JSON using GSON Converter Factory
             */
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /**
         * 6. The Retrofit class generates an implementation of the 'PostService' interface.
         */
        val service : PostService = retrofit.create(PostService::class.java)

        btnRCGetPosts.setOnClickListener{
            Util.hideKeyboard(this)
            
            if(etRCUserId.text.toString().isNotEmpty()){
                pbRCLoading.visibility = View.VISIBLE

                /**
                 * 7. Each Call from the created 'PostService' can make a synchronous or
                 *    asynchronous HTTP request to the remote web server.
                 */
                val posts : Call<List<Post>> = service.listPosts(etRCUserId.text.toString().toInt())

                /**
                 * 9. Asynchronously send the request and notify callback of its response or if an error occurred
                 */
                posts.enqueue(this)
            }
        }

        btnRCClear.setOnClickListener{
            tvRCResults.text = ""
        }

    }

    /**
     * 8.1 Implement onResponse method
     */
    @SuppressLint("SetTextI18n")
    override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
        pbRCLoading.visibility = View.GONE
        tvRCResults.text = ""

        if(response.isSuccessful){
            val postList : List<Post>? = response.body()

            if (postList != null) {
                if(postList.isEmpty()){
                    tvRCResults.text = resources.getString(R.string.response_empty)
                }
                else {
                    for(post in postList){
                        tvRCResults.text = "${tvRCResults.text}\n\n${post.id}\n${post.title}\n${post.body}"
                    }
                }
                return
            }
        }

        tvRCResults.text = resources.getString(R.string.response_fail)
    }

    /**
     * 8.1 Implement onFailure method
     */
    @SuppressLint("SetTextI18n")
    override fun onFailure(call: Call<List<Post>>, t: Throwable) {
        pbRCLoading.visibility = View.GONE

        tvRCResults.text = resources.getString(R.string.response_fail) + " " + t.toString()
    }
}