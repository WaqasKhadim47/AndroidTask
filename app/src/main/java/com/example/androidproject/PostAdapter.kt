package com.example.androidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(final val posts: List<UserDataItem>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner  class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){

        val headingText : TextView = ItemView.findViewById(R.id.heading);
        val titleText : TextView = ItemView.findViewById(R.id.title);


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val postItem = posts[position]
        holder.headingText.setText(postItem.title);
        holder.titleText.setText(postItem.body);

    }

    override fun getItemCount(): Int {
        return posts.size;
    }
}