package com.example.androidproject

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MessageShowScreen : AppCompatActivity() {
    lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_show_screen)

        var arrayList: ArrayList<String> = ArrayList<String>()
        
        val extras = intent.extras
        if (extras != null) {
            arrayList = extras.getStringArrayList("notificationData") as ArrayList<String>
            println(
                "Getting this data ===> ${arrayList.toString()}"
            );

        }
        listView = findViewById(R.id.listview)

        val adapter = ArrayAdapter<String>(this, R.layout.notification_item, arrayList)

        listView.adapter = adapter


    }


}