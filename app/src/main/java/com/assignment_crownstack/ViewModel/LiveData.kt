package com.assignment_crownstack.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.assignment_crownstack.ExtraClasses.Utility
import com.assignment_crownstack.Model.listdata
import org.json.JSONObject


class LiveData(application: Application) : AndroidViewModel(application)  {

    private val context = getApplication<Application>().applicationContext
    private var datalist: MutableLiveData<List<listdata>>? = null

    fun getlistdata(): MutableLiveData<List<listdata>> {
        if (datalist == null) {
            datalist = MutableLiveData()
            loaddata()
        }
        return datalist as MutableLiveData<List<listdata>>
    }

    private fun loaddata() {
        // do async operation to fetch users
        val StringList = ArrayList<listdata>()
        Utility.readXMLinString(
            "test.txt",context)?.let { Log.e("utildata", it) }
        val json = JSONObject(
            Utility.readXMLinString(
                "test.txt",context))
        for(i in 0 until json.getJSONArray("results").length())
        {
            val jsonobject = json.getJSONArray("results").getJSONObject(i)
            StringList.add(listdata(jsonobject.getString("artistName"),
            jsonobject.getString("artworkUrl100")))
        }
        datalist?.postValue(StringList)
    }


}
