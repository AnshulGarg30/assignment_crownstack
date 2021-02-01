package com.assignment_crownstack.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.assignment_crownstack.ExtraClasses.ImageLoader
import com.assignment_crownstack.Model.listdata
import com.assignment_crownstack.R
import kotlinx.android.synthetic.main.list_adapter.view.*

class listadapter(val ctx: Context, val data: LiveData<List<listdata>>) :
    RecyclerView.Adapter<ViewH>() {
    var imageLoader: ImageLoader? = null

    init {
        imageLoader = ImageLoader(ctx)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewH {
        return ViewH(LayoutInflater.from(ctx).inflate(R.layout.list_adapter, p0, false))
    }

    override fun getItemCount(): Int {
        Log.d("Adapter Size ", data.value.toString())
        return data.value!!.size

    }

    override fun onBindViewHolder(p0: ViewH, p1: Int) {
        p0.bindItems(data.value!!?.get(p1), imageLoader)
    }
}

class ViewH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindItems(model: listdata, imageLoader: ImageLoader?) {
        val nam = itemView.textView
        val imag = itemView.imageView
        nam.text = model.name
        imageLoader?.DisplayImage(model.imgae, imag)
//        Picasso.get().load(model.imgae).placeholder(R.drawable.ic_launcher_background).into(imag)
    }
}