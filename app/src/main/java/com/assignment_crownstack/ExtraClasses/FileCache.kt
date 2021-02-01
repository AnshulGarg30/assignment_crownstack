package com.assignment_crownstack.ExtraClasses

import android.content.Context
import android.util.Log
import com.assignment_crownstack.R
import java.io.File

class FileCache(context: Context) {
    private var cacheDir: File
    fun getFile(url: String): File {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        val filename = url.hashCode().toString()
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        return File(cacheDir, filename)
    }

    fun clear() {
        val files = cacheDir!!.listFiles() ?: return
        for (f in files) f.delete()
    }

    init {
        //Find the dir to save cached images
        cacheDir = File(context.getExternalFilesDir(null)?.getAbsolutePath(), context.getResources().getString(
            R.string.app_name) + "_share")

        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
            Log.e("cacheDir2",cacheDir.toString())
        }
        else
        {
            Log.e("cacheDir3",cacheDir.toString())
        }
        Log.e("cacheDir",cacheDir.toString())
    }
}