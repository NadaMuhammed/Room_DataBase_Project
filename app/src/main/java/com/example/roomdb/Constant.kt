package com.example.roomdb

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

class Constant {

    companion object{
         suspend fun getBitmapFunction(context: Context): Bitmap {
            val loading = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data("https://avatars3.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
                .build()

            val result = (loading.execute(request) as SuccessResult).drawable
            return (result as BitmapDrawable).bitmap
        }
    }
}