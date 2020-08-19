package com.eahm.exploringlibraries.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.adapters.MainAdapter
import com.eahm.exploringlibraries.models.MenuElement
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val appSections = listOf(
        MenuElement(
            "The app menu is ready!",
            "Goes to MainActivity",
            "This is a basic example of the app menu for each new activity where a new functionality will be tested.",
            MainActivity::class.java
        ),
        MenuElement(
            "Retrofit2",
            "User Posts",
            "Retrieve the posts from a user using Retrofit",
            RetrofitClient::class.java
        ),
        MenuElement(
            "Retrofit2 | RxJava | RxAndroid",
            "Get Users",
            "We use RxJava and RxAndroid to obtain all the users. In this example, to our retrofit instance, we provide the RxJava Adapter",
            RetrofitAdaptersRxJava::class.java
        ),
        MenuElement(
            "Retrofit2 | GSON",
            "StackOverflow API",
            "We will use the StackOverflow API to make an http query using Retrofit and GSON",
            RetrofitGson::class.java
        ),
        MenuElement(
            "Retrofit2 | RxJava",
            "Auth with Github API ",
            "We use retrofit to authenticate through a web service. In this example we obtain information from the GithubAPI",
            RetrofitRxJava::class.java
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMainSections.apply {
            adapter = MainAdapter(appSections.asReversed())
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        }
    }
}