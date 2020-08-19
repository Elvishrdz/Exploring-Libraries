package com.eahm.exploringlibraries.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.dialogs.DialogCredential
import com.eahm.exploringlibraries.interfaces.GithubAPI
import com.eahm.exploringlibraries.models.GithubIssue
import com.eahm.exploringlibraries.models.GithubRepo
import com.eahm.exploringlibraries.utils.Github
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_retrofit_rx_java.*
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRxJava : AppCompatActivity(), DialogCredential.IDialogCredentialListener {

    //region variables
    private var username : String = ""
    private var password : String = ""

    private lateinit var githubApi : GithubAPI
    private val compositeDisposable = CompositeDisposable()
    //endregion variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit_rx_java)

        /**
         * Getting Started
         * 1. Add the Retrofit library dependency (See build.gradle)
         *  1.1 Also add the GSON dependency to deserialize from JSON (See build.gradle)
         * 2. Add the RxJava Adapter dependency (See build.gradle)
         * 3. Add the RxJava dependency (See build.gradle)
         * 4. Add Internet permission to the AndroidManifest.xml
         *
         * 5. Create the data models (See GithubIssue.kt, GithubRepo.kt & GithubOwner.kt)
         *  5.1 Create the custom deserializable class
         *
         * 6. Define the BASE_URL (See Constants.kt -> Github class)
         * 7. Create the GithubAPI interface (See GithubAPI.kt)
         *
         * 8. Create a Dialog to obtain the user credentials.
         *  8.1 Create the layout design (See dialog_credentials.xml)
         *  8.2 Create the Dialog class (See DialogCredential.kt)
         *
         * 9. Implement the dialog and the listener in this activity
         *  9.1 Complete the onDialogPossitive(...) method
         *
         * 10. Create the observers (See rxJava region)
         *  10.1 Remember remove the composite observer in the onStop lifecycle method.
         * 11. Setup the spinners
         * 12. initialize retrofit and the githubAPI.
         *  12.1 Add the okHttp client to manage the authentication in our http petitions to the
         *       web service.
         *
         * DONE!
         */

        btnRRxGithubLogin.setOnClickListener{
           showCredentialsDialog()
        }

        btnRRxPostComment.setOnClickListener{
            val newComment: String = etRRxComment.text.toString()
            if (newComment.isNotEmpty()) {
                val selectedItem = sRRxIssues.selectedItem as GithubIssue
                selectedItem.body = newComment
                compositeDisposable.add(
                    githubApi.postComment(selectedItem.commentsUrl, selectedItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getCommentObserver())
                )
            }
            else Toast.makeText(this@RetrofitRxJava, "The comment is empty!", Toast.LENGTH_SHORT).show()
        }

        sRRxRepos.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("There's no repositories"))
        sRRxRepos.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(parent?.selectedItem is GithubRepo){
                    val githubRepo = parent.selectedItem as GithubRepo
                    compositeDisposable.add(
                        githubApi.getIssues(githubRepo.owner.login, githubRepo.name)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getIssuesObserver())
                    )
                }
            }

        }
        sRRxRepos.isEnabled = false

        sRRxIssues.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select a repository"))

        initGithubAPI()

        setView(1) // No auth
        showCredentialsDialog()
    }

    private fun initGithubAPI() {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(object: Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()

                    val builder = originalRequest.newBuilder().header("Authorization", Credentials.basic(username, password))

                    val newRequest = builder.build()

                    return chain.proceed(newRequest)
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Github.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        githubApi = retrofit.create(GithubAPI::class.java)
    }

    //region rxJava
    private fun getRepositoriesObserver() : DisposableSingleObserver<List<GithubRepo>>{
        return object : DisposableSingleObserver<List<GithubRepo>>(){
            override fun onSuccess(value: List<GithubRepo>?) {
                setView(2) // Auth

                if (value!!.isNotEmpty()) {
                    val spinnerAdapter: ArrayAdapter<GithubRepo> = ArrayAdapter(
                        this@RetrofitRxJava,
                        android.R.layout.simple_spinner_dropdown_item,
                        value
                    )
                    sRRxRepos.adapter = spinnerAdapter
                    sRRxRepos.isEnabled = true
                } else {
                    val spinnerAdapter = ArrayAdapter(
                        this@RetrofitRxJava,
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayOf("$username has no repositories")
                    )
                    sRRxRepos.adapter = spinnerAdapter
                    sRRxRepos.isEnabled = false
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Toast.makeText(this@RetrofitRxJava, "Can not load repositories", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getIssuesObserver() : DisposableSingleObserver<List<GithubIssue>> {
        return object : DisposableSingleObserver<List<GithubIssue>>(){
            override fun onSuccess(value: List<GithubIssue>?) {
                if (value!!.isNotEmpty()) {
                    val spinnerAdapter: ArrayAdapter<GithubIssue> = ArrayAdapter(
                        this@RetrofitRxJava,
                        android.R.layout.simple_spinner_dropdown_item,
                        value
                    )
                    sRRxIssues.isEnabled = true
                    etRRxComment.isEnabled = true
                    btnRRxPostComment.isEnabled = true
                    sRRxIssues.adapter = spinnerAdapter
                }
                else {
                    val spinnerAdapter = ArrayAdapter(
                        this@RetrofitRxJava,
                        android.R.layout.simple_spinner_dropdown_item,
                        listOf("This repository has no issues!")
                    )
                    sRRxIssues.isEnabled = false
                    etRRxComment.isEnabled = false
                    btnRRxPostComment.isEnabled = false
                    sRRxIssues.adapter = spinnerAdapter
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Toast.makeText(this@RetrofitRxJava, "Can not load issues", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCommentObserver() : DisposableSingleObserver<ResponseBody>{
        return object : DisposableSingleObserver<ResponseBody>(){
            override fun onSuccess(t: ResponseBody?) {
                etRRxComment.setText("")
                Toast.makeText(this@RetrofitRxJava, "Comment posted!", Toast.LENGTH_LONG).show()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Toast.makeText(this@RetrofitRxJava, "There was an error. Try again later.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //endregion rxJava

    //region dialog credentials
    private fun showCredentialsDialog(){
        val dialog = DialogCredential()
        val bundle = Bundle()
        bundle.putString("username", username)
        bundle.putString("username", password)

        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "credential_dialog")
    }

    override fun onDialogPossitive(username: String, password: String) {
        this.username = username
        this.password = password
        loadRepositories()
    }
    //endregion dialog credentials

    private fun loadRepositories(){
        compositeDisposable.add(
            githubApi.getRepos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRepositoriesObserver())
        )
    }

    private fun setView(code : Int){
        /**
         * 1: NO Auth
         * 2: Auth
         */
        groupNotAuth.visibility = if(code == 1) View.VISIBLE else View.GONE
        groupAuth.visibility = if(code == 1) View.GONE else View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}