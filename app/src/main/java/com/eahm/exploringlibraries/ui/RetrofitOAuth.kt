package com.eahm.exploringlibraries.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.interfaces.TwitterAPI
import com.eahm.exploringlibraries.models.TwitterOAuthToken
import com.eahm.exploringlibraries.models.TwitterUser
import com.eahm.exploringlibraries.utils.Twitter
import kotlinx.android.synthetic.main.activity_retrofit_oauth.*
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitOAuth : AppCompatActivity() {

    //region some variables
    /**
     * 8. Change the values of this variable (apiKey and apiKeySecret) with your
     *    credentials. You can find this values in your Twitter developers account.
     */
    private val credentials : String =
        Credentials.basic(
            "apiKey",
            "apiKeySecret"
        )

    private lateinit var twitterApi : TwitterAPI
    private var token : TwitterOAuthToken? = null

    //endregion some variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_oauth)

        /**
         * Getting Started
         * 1. Create the basic design in activity_retrofit_oauth.xml
         * 2. Add the following dependencies (See build.gradle):
         *    Retrofit library - com.squareup.retrofit2
         *    Gson Converter - com.squareup.retrofit2: converter-gson
         *
         * 3. Add internet permission to the AndroidManifest.xml
         * 4. Create the data classes (See TwitterOAuthToken.kt & TwitterUser.kt)
         * 5. Add the BASE URL (See Constants.kt -> Twitter class)
         * 6. Create the TwitterAPI (See TwitterAPI.kt)
         *
         * 7. optional: Polish the activity_retrofit_oath.xml
         *
         * On the RetrofitOAuth activity:
         * 8. Add some variables and create a method to initialize Retrofit. Call
         *    this method in the onCreate()
         *
         * 9. Prepare the listeners for the buttons in the view.
         *  9.1 Create the callbacks for our retrofit requests.
         *
         *** our credential variable (above) needs our credentials. This values are provided
         * in the twitter developers console.
         *
         * DONE!
         */

        initTwitterAPI()

        btnROAToken.setOnClickListener{
            twitterApi.postCredentials("client_credentials")
                .enqueue(tokenCallback)
        }

        btnROAUserDetails.setOnClickListener{
            val username = etROAUsername.text.toString()

            if(username.isNotEmpty()){
                twitterApi.getUser(username)
                    .enqueue(userDetailsCallback)
            }
            else {
                Toast.makeText(this@RetrofitOAuth, "Username is empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 8. Prepare Retrofit. Provide the Authentication token to the requests and
     *    set the twitterAPI.
     */
    private fun initTwitterAPI() {
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(
                object : Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val originalRequest = chain.request()
                        val builder = originalRequest.newBuilder()
                            .header("Authorization", token?.authorization ?: credentials)

                        val newRequest = builder.build()

                        return chain.proceed(newRequest)
                    }
                }
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Twitter.BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        twitterApi = retrofit.create(TwitterAPI::class.java)
    }

    //region callbacks
    private val tokenCallback = object : Callback<TwitterOAuthToken>{
        override fun onFailure(call: Call<TwitterOAuthToken>, t: Throwable) {
            t.printStackTrace()
            Toast.makeText(this@RetrofitOAuth, "Requesting token has failed!", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<TwitterOAuthToken>, response: retrofit2.Response<TwitterOAuthToken>) {
            if (response.isSuccessful) {
                btnROAToken.isEnabled = false
                btnROAUserDetails.isEnabled = true
                etROAUsername.isEnabled = true
                token = response.body()
            }
            else {
                Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message())
                Toast.makeText(this@RetrofitOAuth, "Requesting token has failed!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private val userDetailsCallback = object : Callback<TwitterUser>{
        override fun onFailure(call: Call<TwitterUser>, t: Throwable) {
            t.printStackTrace()
            Toast.makeText(this@RetrofitOAuth, "Requesting user details has failed!", Toast.LENGTH_LONG).show()
        }

        override fun onResponse(call: Call<TwitterUser>, response: retrofit2.Response<TwitterUser>) {
            if (response.isSuccessful) {
                val userDetails: TwitterUser? = response.body()

                tvROAUserName.text = userDetails?.name ?: "Unknown"
                tvROAUserLocation.text = userDetails?.location ?: "Unknown"
                tvROAUserUrl.text = userDetails?.url ?: "Unknown"
                tvROAUserDescription.text = userDetails?.description ?: "Unknown"

            }
            else {
                Log.d("UserDetailsCallback", "Code: " + response.code().toString() + "Message: " + response.message())
                Toast.makeText(this@RetrofitOAuth, "Requesting user details has failed!", Toast.LENGTH_LONG).show()
            }
        }

    }
    //endregion callbacks

}