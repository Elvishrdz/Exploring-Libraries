package com.eahm.exploringlibraries.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.interfaces.UserApi
import com.eahm.exploringlibraries.models.User
import com.eahm.exploringlibraries.utils.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_retrofit_adapters_rx_java.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAdaptersRxJava : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_adapters_rx_java)

        /**
         * Important: We use the RxJava3Adapter because the RxAndroid Schedulers are based on
         * RxJava3. All versions must be compatible to avoid runtime errors.
         */

        /**
         * Getting Started
         * 1. add the Retrofit library dependency (See build.gradle)
         *  1.1 For this example we will use the RxJava 3 adapter
         *      implementation "com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0"
         *
         * 2. add the RxJava Adapter dependency:
         *    implementation 'com.squareup.retrofit2:adapter-rxjava2:latest_version'
         *  2.1 also add the RxAndroid library to extra support for the android platform:
         *      implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
         *
         * 3. Obtain an instance of Retrofit
         *  3.1 Also add the RxJava2CallAdapterFactory to the Retrofit builder.
         *  3.2 To deserialize the JSON data we need to add the GSON Converter.
         *
         * 4. Create a User model to obtain the data from the web service. (See User.kt)
         * 5. Create the UseAPI interface (See UserApi.kt) and create the Rest API for retrofit.
         *
         * 6. Let Retrofit create an inplementation of the UserApi interface
         * 7. Use RxJava to process the response
         *
         * 8. Remember: We need to declare the Internet permission in the AndroidManifest.xml
         *
         *  Done!
         */

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            /**
             * 3.1 Add the RxJava 3 Adapter to Retrofit
             *
             * With this adapter being applied the Retrofit interfaces are able
             * to return RxJava types. e.g: Observable, Flowable or Single, etc.
             */
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            /**
             *  3.2 To deserialize the JSON data we need to add the GSON Converter.
             */
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /**
         * 6. Let Retrofit create an inplementation of the UserApi interface
         */
        val service : UserApi = retrofit.create(UserApi::class.java)

        btnRxGetUsers.setOnClickListener{
            pbRxLoading.visibility = View.VISIBLE
            /**
             * 7. Use RxJava to process the response
             */
            service.getUsers()
                /**
                 * Check that we use the RxJava3 Schedulers:
                 * io.reactivex.rxjava3.schedulers.Schedulers.io()
                 */
                .subscribeOn(Schedulers.io())
                .doOnError{
                    Log.i("AdapterError", "Do on error ${it.message}")
                }
                .onErrorReturn {
                    Log.i("AdapterError", "OnError Return ${it.message}")
                    listOf(
                        User(
                            userName = it.message ?: "Error"
                        )
                    )
                }
                /**
                 * 2.1 We change to the Android main thread included in the RxAndroid library.
                 */
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    tvRxResults.text = ""

                    if(it != null){
                        for(user in it){
                            if(user.id == -1){
                                tvRxResults.text = "The request failed!\n${user.userName}"
                            }
                            else {
                                tvRxResults.text = "${tvRxResults.text}\n${user.toString()}\n"
                            }
                        }
                    }

                    pbRxLoading.visibility = View.GONE
                }
        }

        btnRxClear.setOnClickListener{
            tvRxResults.text = ""
        }

    }
}