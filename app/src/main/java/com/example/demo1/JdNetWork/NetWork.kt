package com.example.demo1.JdNetWork

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.demo1.DataClass.JdItem
import com.example.demo1.DataClass.PaiPaiitem
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

object NetWork {
    private val baseurl =
        "https://api.m.jd.com/"
    private val paipaiurl = "https://api.m.jd.com/"
    private val retrofit =
        Retrofit.Builder().baseUrl(baseurl).build().create(JdNetInterface::class.java)
    private val paipaiRetrofit =
        Retrofit.Builder().baseUrl(paipaiurl).build().create(PaiPaiInterface::class.java)
    fun getRecommend(
        page: Int,
        callback: (code: Int, response: ArrayList<JdItem>?) -> Unit
    ) {
        val requestBodyStr =
            "{\"expressionKey\":{\"brand\":[],\"searchCats\":[{\"id\":\"0\",\"field\":\"cid1\"}],\"expandName\":[{\"attrs\":[],\"expandId\":\"99\",\"expandName\":\"成色\"}],\"price\":{\"max\":\"100000000\",\"min\":\"0\"}},\"type\":null,\"sort\":\"5\",\"pageSize\":20,\"page\":$page,\"p\":2}"
        retrofit.getRecommend(requestBodyStr).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                body?.let {
                    var itemArray = arrayListOf<JdItem>()
                    try {
                        val fromJson = Gson().fromJson(it.string(), JsonObject::class.java)
                        val jsonArray =
                            fromJson.get("result").asJsonObject.get("data").asJsonObject.get("wareInfo").asJsonArray
                        jsonArray.forEach {

                            val item = Gson().fromJson(it.toString(), JdItem::class.java)
                            itemArray.add(item)

                        }
                        callback(1, itemArray)
                    } catch (
                        e: Exception
                    ) {
                        callback(1, itemArray)
                        Log.v("例子", "转换出错！！！！！！！！！！！！！")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(0, null)
            }
        })
    }
    fun getSearch(
        page: Int,
        keyword:String,
        callback: ( response: ArrayList<PaiPaiitem>?) -> Unit
    ) {
        val requestUrl="api?functionId=pp.search.ershou_search.v1.search&body={\"query\":{\"pageNo\":$page,\"pageSize\":40,\"bizType\":13,\"service\":[{\"key\":\"stock_count\",\"value\":\"1\"}],\"sort\":{},\"key\":\"$keyword\",\"cat\":[],\"extAttrList\":[],\"brand\":[],\"price\":[],\"area\":[]}}&t=1659252556440&appid=paipai_h5"
        paipaiRetrofit.search(requestUrl).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
//                println(body?.string())
                body?.let {
                    var itemArray = arrayListOf<PaiPaiitem>()
                    try {
                        val fromJson = Gson().fromJson(it.string(), JsonObject::class.java)
                        val jsonArray =
                            fromJson.get("result").asJsonObject.get("data").asJsonObject.get("itemList").asJsonArray
                        jsonArray.forEach {
                            val item = Gson().fromJson(it.toString(), PaiPaiitem::class.java)
                            itemArray.add(item)
                        }
                        callback(itemArray)
                    } catch (
                        e: Exception
                    ) {
                        print(e)
                        callback(itemArray)
                        Log.v("例子", "转换出错！！！！！！！！！！！！！")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(null)
            }
        })
    }
}