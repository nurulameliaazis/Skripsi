package com.example.userriletion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private val context: Context,private val newList: ArrayList<News>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
    return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem = newList[position]
        holder.titleImage.setImageResource(currentItem.tittleImage)
        holder.tvHeading.text = currentItem.heading

      Glide.with(context).load(currentItem.tittleImage).into(holder.titleImage)
    }

    override fun getItemCount(): Int {
    return newList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titleImage : ShapeableImageView = itemView.findViewById(R.id.shapeableImageView)
        val tvHeading : TextView= itemView.findViewById(R.id.textlist)

    }
}