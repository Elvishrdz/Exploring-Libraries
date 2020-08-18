package com.eahm.exploringlibraries.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.adapters.StackOverflowAdapter
import com.eahm.exploringlibraries.interfaces.StackOverflowAPI
import com.eahm.exploringlibraries.models.Answer
import com.eahm.exploringlibraries.models.Question
import com.eahm.exploringlibraries.utils.ListWrapper
import com.eahm.exploringlibraries.utils.StackOverflow
import kotlinx.android.synthetic.main.activity_retrofit_gson.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class RetrofitGson : AppCompatActivity() {

    private lateinit var stackoverflowAPI : StackOverflowAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_gson)

        /**
         * Getting Started
         * 1. Add the retrofit dependency: com.squareup.retrofit2 (See build.gradle)
         *  1.1 Add also the GSON Converter dependency: com.squareup.retrofit2 converter-gson (See Build.gradle)
         * 2. Add the GSON library dependency: com.google.code.gson (See build.gradle)
         *
         * 3. Create a data model for the responses and use GSON to deserialize the fields
         *    from the JSON. (See Question.kt) (See Answer.kt)
         *
         * 4. Create the layouts. (See activity_retrofit_gson.xml & item_stackoverflow.xml)
         *  4.1 Prepare the recycler view (See StackOverflowAdapter.kt)
         *  4.2 Create methods with fake data.
         *  4.3 Fill the spinner with fake data.
         *  4.4 Provide the recycler view with fake data.
         *
         * 5. Add internet permission to the AndroidManifest.xml
         *
         * 6. Define the BaseURL for Retrofit (See Constants.kt -> StackOverflow class)
         *
         * 7. Create the StackOverflowAPI.kt interface (See StackOverflowAPI.kt)
         * 8. Create the initStackOverflowAPI() method to initialize retrofit
         *  8.1 Call the method
         *
         * 9. add a listener in the spinner. When a new question is selected from the
         *    spinner we do a new http request to get the values of that question. We
         *    provide the questionID to our query.
         *
         * 10. Call the query to get the questions in the oncreate (to automatically
         *     obtain the questions from the stackoverflow web service)
         *  10.1 We also provide fake data in case the query is not successful or there
         *       is not internet connection.
         * Done!
         */

        /**
         *  8.1 Call the method
         */
        initStackOverflowAPI()

        /**
         * 4.3 Fill the spinner with fake data.
         */
        sRGQuestions.adapter =  ArrayAdapter<Question>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            getQuestions()
        )

        rvRGList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RetrofitGson)

            /**
             * 4.4 Provide the recycler view with fake data.
             */
            adapter = StackOverflowAdapter(getAnswers())
        }

        sRGQuestions.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val question = parent?.adapter?.getItem(position) as Question
                pbRGLoading.visibility = View.VISIBLE
                stackoverflowAPI.getAnswers(question.questionId)
                    .enqueue(answersCallback)
            }
        }

        pbRGLoading.visibility = View.VISIBLE
        stackoverflowAPI.getQuestions().enqueue(questionCallback)
    }

    /**
     * 8. Create the initStackOverflowAPI() method to initialize retrofit
     */
    private fun initStackOverflowAPI(){
        val retrofit = Retrofit.Builder()
            .baseUrl(StackOverflow.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stackoverflowAPI = retrofit.create(StackOverflowAPI::class.java)
    }

    //region callbacks
    private val questionCallback = object : Callback<ListWrapper<Question>>{
        override fun onFailure(call: Call<ListWrapper<Question>>, t: Throwable) {
            pbRGLoading.visibility = View.GONE
            t.printStackTrace()
        }

        override fun onResponse(call: Call<ListWrapper<Question>>, response: Response<ListWrapper<Question>>) {
            pbRGLoading.visibility = View.GONE
            if (response.isSuccessful) {
                val questions: ListWrapper<Question>? = response.body()
                val arrayAdapter: ArrayAdapter<Question> = ArrayAdapter(
                    this@RetrofitGson,
                    android.R.layout.simple_spinner_dropdown_item,
                    questions?.items as MutableList<Question>
                )
                sRGQuestions.adapter = arrayAdapter
            }
            else {
                Log.d("QuestionsCallback", "1 Code: " + response.code().toString() + " Message: " + response.message())
            }
        }
    }

    private val answersCallback = object : Callback<ListWrapper<Answer>>{
        override fun onFailure(call: Call<ListWrapper<Answer>>, t: Throwable) {
            pbRGLoading.visibility = View.GONE
            t.printStackTrace()
        }

        override fun onResponse(call: Call<ListWrapper<Answer>>, response: Response<ListWrapper<Answer>>) {
            pbRGLoading.visibility = View.GONE
            if (response.isSuccessful) {
                val data : MutableList<Answer> = ArrayList()
                data.addAll(response.body()?.items!!)

                rvRGList.adapter = StackOverflowAdapter(data)

            } else {
                Log.d("QuestionsCallback", "2 Code: " + response.code().toString() + " Message: " + response.message())
            }
        }

    }

    //endregion callbacks

    //region 4.2 Create methods with fake data
    private fun getQuestions(): MutableList<Question> {
        val questions: MutableList<Question> = ArrayList()
        for (i in 0..7) {
            val question = Question(
                questionId = i.toString(),
                title = "Question " + i.toString(),
                body = "Body: " + i.toString()
            )
            questions.add(question)
        }
        return questions
    }

    private fun getAnswers(): MutableList<Answer> {
        val answers: MutableList<Answer> = ArrayList()
        for (i in 0..7) {
            val answer = Answer(
                answerId = i,
                isAccepted = Random.nextInt(0, 11) > 5,
                score = i + Random.nextInt(0, 100)
            )
            answers.add(answer)
        }
        return answers
    }
    //endregion 4.2 Create methods with fake data

}