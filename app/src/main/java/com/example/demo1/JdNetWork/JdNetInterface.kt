package com.example.demo1.JdNetWork

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface JdNetInterface {
@FormUrlEncoded
@POST("api?appid=paipai_h5&functionId=paipai.used.insale.index.product.list")
fun getRecommend(@Field("body") body:String): Call<ResponseBody>
}