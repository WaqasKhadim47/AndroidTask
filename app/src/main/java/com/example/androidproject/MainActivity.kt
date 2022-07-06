package com.example.androidproject

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.Api.ApiInterface
import com.google.android.material.internal.ContextUtils.getActivity
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
       val mLayoutManager = GridLayoutManager(this@MainActivity, 1)
        val rdb = findViewById<View>(R.id.radioGrp) as RadioGroup

        rdb.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButton = radioGroup?.findViewById(radioGroup.checkedRadioButtonId) as? RadioButton
            checkedRadioButton?.let {

                if (checkedRadioButton.isChecked)
                   if(checkedRadioButton.id == R.id.linear){
                       mLayoutManager.spanCount = 1
                   }else{
                       checkedRadioButton.id = R.id.grid
                       mLayoutManager.spanCount= 2
                   }
            }

        })
//        rdb.setOnClickListener { v ->
//            val checked = (v as RadioButton).isChecked
//            // Check which radiobutton was pressed
//            if (checked) {
//                // Do your coding
//            } else {
//                // Do your coding
//            }
//        }
        recyclerView.layoutManager = mLayoutManager

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