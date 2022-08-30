package com.example.demo1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.demo1.DataClass.JdItem
import com.example.demo1.JdNetWork.NetWork
import com.google.gson.Gson
import java.lang.Exception

fun main()
{
NetWork.getSearch(1,"吹风机"){
    it?.forEach {
       println(it.title)
        println(it.mainPic)
        println(it.price)
    }
}
}

@Composable
fun canvasTest()
{
    Canvas(modifier = Modifier.fillMaxWidth()){
        drawPath(path = Path().apply {
            //设置原点
            moveTo(0f,0f)
            //设置前点要到达的点
            lineTo(size.width,0f)
            //起点连接最后一个点实现
            close()
        }, brush = Brush.horizontalGradient(colors = listOf(Color.Gray,Color.White)), style = Stroke(width = size.width,cap= StrokeCap.Round))
        drawArc(brush = Brush.horizontalGradient(colors = listOf(Color.Gray,Color.White)),1f,2f,true)
        drawOval(brush = Brush.horizontalGradient(colors = listOf(Color.Gray,Color.White)))

    }
}