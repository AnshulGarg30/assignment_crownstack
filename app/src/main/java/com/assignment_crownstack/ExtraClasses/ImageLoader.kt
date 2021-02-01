package com.assignment_crownstack.ExtraClasses

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.widget.ImageView
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ImageLoader(context: Context?) {
    var memoryCache: MemoryCache = MemoryCache()
    var fileCache: FileCache
    private val imageViews =
        Collections.synchronizedMap(WeakHashMap<ImageView, String>())
    var executorService: ExecutorService
    var handler = Handler() //handler to display images in UI thread
    val stub_id: Int= com.assignment_crownstack.R.drawable.ic_launcher_background
    fun DisplayImage(url: String, imageView: ImageView) {
        imageViews[imageView] = url
        val bitmap: Bitmap? = memoryCache.get(url)
        if (bitmap != null) imageView.setImageBitmap(bitmap) else {
            queuePhoto(url, imageView)
            imageView.setImageResource(stub_id)
        }
    }

    private fun queuePhoto(url: String, imageView: ImageView) {
        val p = PhotoToLoad(url, imageView)
        executorService.submit(PhotosLoader(p))
    }

    private fun getBitmap(url: String): Bitmap? {
        val f: File = fileCache.getFile(url)

        //from SD cache
        val b = decodeFile(f)
        return b
            ?: try {
                var bitmap: Bitmap? = null
                val imageUrl = URL(url)
                val conn =
                    imageUrl.openConnection() as HttpURLConnection
                conn.connectTimeout = 30000
                conn.readTimeout = 30000
                conn.instanceFollowRedirects = true
                val datas = conn.inputStream
                val os: OutputStream = FileOutputStream(f)
                Utility.CopyStream(datas, os)
                os.close()
                conn.disconnect()
                bitmap = decodeFile(f)
                bitmap
            } catch (ex: Throwable) {
                ex.printStackTrace()
                if (ex is OutOfMemoryError) memoryCache.clear()
                null
            }

        //from web
    }

    //decodes image and scales it to reduce memory consumption
    private fun decodeFile(f: File): Bitmap? {
        try {
            //decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val stream1 = FileInputStream(f)
            BitmapFactory.decodeStream(stream1, null, o)
            stream1.close()

            //Find the correct scale value. It should be the power of 2.
            val REQUIRED_SIZE = 70
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) break
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            //decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            val stream2 = FileInputStream(f)
            val bitmap = BitmapFactory.decodeStream(stream2, null, o2)
            stream2.close()
            return bitmap
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    //Task for the queue
    inner class PhotoToLoad(var url: String, var imageView: ImageView)

    internal inner class PhotosLoader(var photoToLoad: PhotoToLoad) : Runnable {
        override fun run() {
            try {
                if (imageViewReused(photoToLoad)) return
                val bmp = getBitmap(photoToLoad.url)
                memoryCache.put(photoToLoad.url, bmp)
                if (imageViewReused(photoToLoad)) return
                val bd = BitmapDisplayer(bmp, photoToLoad)
                handler.post(bd)
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }

    }

    fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
        val tag = imageViews[photoToLoad.imageView]
        return if (tag == null || tag != photoToLoad.url) true else false
    }

    //Used to display bitmap in the UI thread
    internal inner class BitmapDisplayer(var bitmap: Bitmap?, var photoToLoad: PhotoToLoad) :
        Runnable {
        override fun run() {
            if (imageViewReused(photoToLoad)) return
            if (bitmap != null) photoToLoad.imageView.setImageBitmap(bitmap) else photoToLoad.imageView.setImageResource(
                stub_id
            )
        }

    }

    fun clearCache() {
        memoryCache.clear()
        fileCache.clear()
    }

    init {
        fileCache = context?.let { FileCache(it) }!!
        executorService = Executors.newFixedThreadPool(5)
    }
}