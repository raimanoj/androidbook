package com.manojrai.androidtest.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manojrai.androidtest.R
import com.manojrai.androidtest.data.model.Book
import com.manojrai.androidtest.data.model.ImageModel
import kotlinx.android.synthetic.main.item_books.view.*

class SliderAdapter(
    private var list: ArrayList<Uri>,
) : RecyclerView.Adapter<SliderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listItem =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_slider, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bindData(list[position])
    }

    override fun getItemCount() = list.size


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivSlider: ImageView = itemView.findViewById(R.id.ivSlider)

        fun bindData(uri: Uri) {
            Glide.with(ivSlider.context).load(uri)
                .into(ivSlider)
        }
    }
}