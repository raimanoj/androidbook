package com.manojrai.androidtest.ui.addnew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manojrai.androidtest.R
import com.manojrai.androidtest.data.model.ImageModel

class ShowImageAdapter(
    private val list: ArrayList<ImageModel>
) : RecyclerView.Adapter<ShowImageAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listItem =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_show_images, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount() = list.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivImage: ImageView = itemView.findViewById(R.id.ivImage)

        fun bindData(imageModel: ImageModel) {
            Glide.with(ivImage.context).load(imageModel.uri)
                .into(ivImage)
        }
    }
}