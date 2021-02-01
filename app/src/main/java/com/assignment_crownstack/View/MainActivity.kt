package com.assignment_crownstack.View

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment_crownstack.Adapter.listadapter
import com.assignment_crownstack.Model.listdata
import com.assignment_crownstack.R
import com.assignment_crownstack.ViewModel.LiveData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var data= MutableLiveData<List<listdata>>()
    lateinit  var da:List<listdata>
    lateinit var listadapterdata:listadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_main.layoutManager= GridLayoutManager(this,2)
        listadapterdata=listadapter(this,data)
        rv_main.adapter=listadapterdata

        val model = ViewModelProviders.of(this).get(LiveData::class.java)
        model.getlistdata().observe(this, Observer<List<listdata>>{ datalist ->
            // update UI

            Log.e("dataobserved","true");
            data.value=datalist
            da=datalist!!
            Log.d("MainActivity ","Data Send"+data.value!!.size.toString())
//            listadapterdata.imageLoader?.clearCache()
            listadapterdata.notifyDataSetChanged()
        })

    }
}