package com.assignment_crownstack.ExtraClasses

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object Utility {
    fun readXMLinString(fileName: String?, c: Context): String? {
        try {
            val data: InputStream? = fileName?.let { c.assets.open(it) }
            val size: Int? = data?.available()
            val buffer = size?.let { ByteArray(it) }
            data?.read(buffer)
            data?.close()
            val text= buffer?.let { String(it) }
            return text
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
    fun CopyStream(datas: InputStream, os: OutputStream) {
        val buffer_size = 1024
        try {
            val bytes = ByteArray(buffer_size)
            while (true) {
                val count = datas.read(bytes, 0, buffer_size)
                if (count == -1) break
                os.write(bytes, 0, count)
            }
        } catch (ex: Exception) {
        }
    }
}