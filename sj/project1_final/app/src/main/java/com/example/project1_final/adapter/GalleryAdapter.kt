package com.example.project1_final.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1_final.OpenGalleryFolderActivity
import com.example.project1_final.R
import com.example.project1_final.model.MediaFileData

class GalleryAdapter(private val context: Context, private val dataset: List<MediaFileData>, private val countImages:List<Int>): RecyclerView.Adapter<GalleryAdapter.ImageViewHolder>() {
    class ImageViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.gridImg)
        val gridName: TextView = view.findViewById(R.id.gridName)
        val gridSub: TextView = view.findViewById(R.id.gridSub)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_folder_image, parent, false)
        return ImageViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = dataset.getOrNull(position)
        holder.gridName.text = item!!.bucketName
        holder.gridSub.text = countImages[position].toString()

        holder.imageView.setOnClickListener {
            //Toast.makeText(context, "Clicked", LENGTH_SHORT).show()
            val intent:Intent = Intent(context, OpenGalleryFolderActivity::class.java)
            intent.apply{
                putExtra("bucketId", item.bucketId)
                putExtra("bucketName", item.bucketName)
            }
            context.startActivity(intent)
        }

        Glide.with(holder.imageView)
            .load(item!!.uri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}