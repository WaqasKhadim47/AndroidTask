package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.Api.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
     val BASE_URL ="https://jsonplaceholder.typicode.com/";

    lateinit var recyclerView : RecyclerView
    lateinit var  adapter : PostAdapter
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.pBar)
        recyclerView.layoutManager = LinearLayoutManager(this);

        getPosts()
    }

    private fun getPosts() {
        progressBar.visibility = View.VISIBLE
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java);

        val dataResponse =  retrofitBuilder.getPosts();

        dataResponse.enqueue(object: Callback<List<UserDataItem>?> {
            override fun onResponse(
                call: Call<List<UserDataItem>?>,
                response: Response<List<UserDataItem>?>
            ) {
                val responseBody = response.body()!!;
                adapter = PostAdapter(responseBody)
                recyclerView.adapter = adapter
                progressBar.visibility = View.GONE

            }

            override fun onFailure(call: Call<List<UserDataItem>?>, t: Throwable) {
                progressBar.visibility = View.GONE
            }

        })

    }
}