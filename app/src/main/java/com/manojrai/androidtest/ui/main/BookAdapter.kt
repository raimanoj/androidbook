package com.manojrai.androidtest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manojrai.androidtest.R
import com.manojrai.androidtest.data.model.Book
import kotlinx.android.synthetic.main.item_books.view.*

class BookAdapter(
    private var list: ArrayList<Book>,
    private val callBack: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listItem =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_books, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.itemView.tvAuthorName.text = data.authorName
        holder.itemView.tvBookName.text = data.booName
        holder.itemView.tvPrice.text = data.price
        holder.itemView.tvDoi.text = data.doi
        Glide.with(holder.itemView.ivThumbNail.context).load(data.list[0])
            .into(holder.itemView.ivThumbNail)
        holder.itemView.ivThumbNail.setOnClickListener {
            callBack(data)
        }
    }

    override fun getItemCount() = list.size

    fun addList(list: ArrayList<Book>) {
        this.list = list
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* private var tvBookName: ImageView = itemView.findViewById(R.id.tvBookName)
         private var tvAuthorName: ImageView = itemView.findViewById(R.id.tvAuthorName)
         private var tvPrice: ImageView = itemView.findViewById(R.id.tvPrice)
         private var tvDoi: ImageView = itemView.findViewById(R.id.tvDoi)
         private var ivThumbNail: ImageView = itemView.findViewById(R.id.ivThumbNail)*/
    }
}