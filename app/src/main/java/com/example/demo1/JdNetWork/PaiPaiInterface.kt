package com.example.demo1.JdNetWork

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url

interface PaiPaiInterface {
    @Headers("referer: https://paipai.jd.com/")
    @GET
    fun search(@Url()url:String):Call<ResponseBody>
}