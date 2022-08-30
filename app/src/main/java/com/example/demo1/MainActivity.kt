package com.example.demo1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.compose.AsyncImage
import coil.util.CoilUtils
import com.example.demo1.DataClass.JdItem
import com.example.demo1.DataClass.PaiPaiitem
import com.example.demo1.JdNetWork.NetWork
import com.example.demo1.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.DecimalFormat
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CraneTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ViewRoutes.MainView.name
                ) {
                    composable(ViewRoutes.MainView.name) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = androidx.compose.material.MaterialTheme.colors.primary
                        ) {
                            DefaultPreview()
                        }
                    }
                    composable(ViewRoutes.ArticleDetailView.name) {
                        navController.navigate(ViewRoutes.MainView.name)
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Bar(tabState: TabValues, onSelect: (index: Int) -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentSize()
    ) {
        TabRow(modifier = Modifier.align(Alignment.CenterVertically),
            contentColor = androidx.compose.material.MaterialTheme.colors.primary,
            selectedTabIndex = tabState.ordinal,
            divider = {
            },
            indicator = { tabPositions: List<TabPosition> ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[tabState.ordinal])
                        .fillMaxSize()
                        .padding(horizontal = 4.dp)
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(16.dp))
                )
            }) {

            val textModifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)

            TabValues.values().forEach {
                val name = it.name
                val selected = tabState.ordinal == it.ordinal
                Tab(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    selected = selected,
                    onClick = {
                        onSelect(it.ordinal)
                    }) {
                    Text(
                        modifier = textModifier,
                        color = if (selected) Color.White else Color.Gray,
                        text = it.name
                    )
                }
            }
        }
    }
}

@Composable
fun Back() {

    Column() {
        Text(
            text = "京东退货仓,满100包邮,京东直管的退货仓通过京东快递发货,购买请咨询微信vx-bot。购买前仔细核对,若商品主体功能无问题退货由买方支付退货运费。若商品主体功能有问题请联系客服微信vx-bot免费售后。",
            Modifier
                .padding(5.dp)
                .wrapContentSize()
                .fillMaxWidth()
                .background(Color(0xFF720D5D)),
            color = Color(0xff9c7d4a),
        )
    }
}


@OptIn(
    ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val tabValues = TabValues.values()
    val transitionState = remember {
        MutableTransitionState(SplashState.Shown)
    }

    val updateTransition = updateTransition(transitionState, label = "")
    val splashAlpha by updateTransition.animateFloat(transitionSpec = { tween(3000) }, label = "") {
        //第一次执行会先返回一个初始值，更新targetState后在逐渐变化。
        if (it == SplashState.Shown) 1f else 0f
    }
    val contentAlpha by updateTransition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = "contentAlpha"
    ) {
        if (it == SplashState.Shown) 0f else 1f
    }
    Box {
        Scaffold(modifier = Modifier.statusBarsPadding()) {
            var tabState by remember {
                mutableStateOf(tabValues[0])
            }
            BackdropScaffold(modifier = Modifier
                .alpha(contentAlpha)
                .padding(it),
                appBar = {
                    Bar(tabState) {
                        tabState = TabValues.values()[it]
                    }
                },
                frontLayerScrimColor = Color.Unspecified,
                scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
                backLayerContent = { Back() },
                frontLayerShape = BottomSheetShape,
                frontLayerContent = {
                    AnimatedContent(targetState = tabState, transitionSpec = {
                        val direction = if (initialState.ordinal < targetState.ordinal)
                            AnimatedContentScope.SlideDirection.Left else AnimatedContentScope
                            .SlideDirection.Right
                        slideIntoContainer(
                            towards = direction,
                            animationSpec = tween(600)
                        ) with
                                slideOutOfContainer(
                                    towards = direction,
                                    animationSpec = tween(600)
                                ) using SizeTransform(
                            clip = false,
                            sizeAnimationSpec = { _, _ ->
                                tween(600, easing = EaseInOut)
                            })
                    })
                    {
                        when (it) {
                            tabValues[0] -> {
                                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                                    Column(
                                        modifier = Modifier.padding(
                                            start = 24.dp,
                                            top = 20.dp,
                                            end = 24.dp
                                        )
                                    ) {
                                        Text(
                                            text = "推荐",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        firstScreen()
                                    }
                                }
                            }
                            tabValues[1] -> {
                                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                                    Column(
                                        modifier = Modifier.padding(
                                            start = 24.dp,
                                            top = 20.dp,
                                            end = 24.dp
                                        )
                                    ) {
                                        Text(
                                            text = "搜索结果",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        secondScreen()
                                    }
                                }
                            }
                            tabValues[2] -> {
                                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                                    Column(
                                        modifier = Modifier.padding(
                                            start = 24.dp,
                                            top = 20.dp,
                                            end = 24.dp
                                        )
                                    ) {
                                        Text(
                                            text = "购物车",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        thirdScreen()
                                    }
                                }
                            }
                        }

                    }
                }
            )
        }

        LandingScreen(modifier = Modifier.alpha(splashAlpha), transitionState)
    }
}

@Composable
fun LandingScreen(modifier: Modifier, transitionState: MutableTransitionState<SplashState>) {

    Box(
        modifier = modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        // Adds composition consistency. Use the value when LaunchedEffect is first called
        LaunchedEffect(Unit) {
            delay(2000)
            transitionState.targetState = SplashState.Completed
        }

        Image(painterResource(id = R.mipmap.img), contentDescription = null)
    }
}

@Composable
fun gridItem(item: JdItem) {

    Column {
        Box(contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = item.imageurl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.clip(RoundedCornerShape(5.dp))
            )
            if (item.size != "") {
                Text(
                    text = item.size,
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .background(Color(0x88ff3434))
                        .padding(2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        var price = item.price.toFloat() + item.price.toFloat() * 0.17
        var jdprice = item.jdPrice.toFloat()
        Text(text = item.wname, maxLines = 2)
        Row(modifier = Modifier.padding(5.dp)) {
            Text(
                text = "￥" + if (price < jdprice) String.format("%.2f", price) else "联系客服",
                color = Color(0xffff3434),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "￥" + item.jdPrice,
                color = Color(0xff999999),
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

@Composable
fun PaiPaiGridItem(item: PaiPaiitem) {

    Column {
        Box(contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = "https://m.360buyimg.com/mobilecms/" + item.mainPic,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
            )
            if (item.stockRate != "") {
                Text(
                    text = "库存" + item.stockRate + "件",
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .background(Color(0x88ff3434))
                        .padding(2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        var price = 0.0
        var jdprice = 0.0f
        try {
            price = item.price.toFloat() + item.price.toFloat() * 0.17
            jdprice = item.skuPriceNew.toFloat()
        } catch (e: Exception) {

        }
        Text(text = item.title, maxLines = 2)
        Row(modifier = Modifier.padding(5.dp)) {
            Text(
                text = "￥" + if (price < jdprice) String.format("%.2f", price) else "联系客服",
                color = Color(0xffff3434),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "￥" + item.skuPriceNew,
                color = Color(0xff999999),
                textDecoration = TextDecoration.LineThrough
            )
        }

    }
}

@Composable
fun firstScreen() {
    val coroutineScope = rememberCoroutineScope()
    var page by remember {
        mutableStateOf(1)
    }
    val list = remember {
        mutableStateListOf<JdItem>()
    }

    var lastPage by remember {
        mutableStateOf(false)
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(list) {
            gridItem(item = it)
        }
        if (!lastPage) {
            item {
                coroutineScope.launch {
                    NetWork.getRecommend(page) { code, response ->
                        if(response!=null)
                        {
                            if (response.size != 0) {
                                list.addAll(response)
                                Log.v("例子", "开始网络请求数据并加载！！！！！！！！！！！！")
                            } else {
                                lastPage = true
                            }
                            page++
                        }
                    }
                    page++
                }
            }
        }
    }
}

@Composable
fun secondScreen() {
    val coroutineScope = rememberCoroutineScope()
    var page by remember {
        mutableStateOf(1)
    }
    val list = remember {
        mutableStateListOf<PaiPaiitem>()
    }

    var lastPage by remember {
        mutableStateOf(false)
    }

    var keyWord by remember {
        mutableStateOf("")
    }
    Column {
        OutlinedTextField(
            value = keyWord,
            onValueChange = {
                keyWord = it
            },
            keyboardActions = KeyboardActions {
                page = 1
                list.clear()
                if (keyWord != "") {
                    coroutineScope.launch {
                        NetWork.getSearch(page, keyWord) { response ->
                            response?.let {
                                if (it.size != 0) {
                                    list.addAll(response)
                                    Log.v("例子", "开始网络请求数据并加载！！！！！！！！！！！！")
                                } else {
                                    lastPage = true
                                }

                            }
                        }
                        page++
                    }
                }
            },
            keyboardOptions = KeyboardOptions(),
            placeholder = {
                Text(text = "搜索", color = Color.White)
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color(0xFF720D5D),
            ),
            singleLine = true
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(list) {
                PaiPaiGridItem(item = it)
                Log.v("例子", "重组！！！！！！！！！11")
            }
            if (!lastPage&&keyWord!="") {
                item {
                    coroutineScope.launch {
                        NetWork.getSearch(page, keyWord) { response ->
                            if(response!=null)
                            {
                                if (response.size != 0) {
                                    list.addAll(response)
                                    Log.v("例子", "开始网络请求数据并加载！！！！！！！！！！！！")
                                } else {
                                    lastPage = true
                                }
                                page++
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun thirdScreen() {

    val list = remember {
        mutableStateListOf<JdItem>()
    }
    Text(text = "待实现")
}

enum class SplashState { Shown, Completed }
enum class TabValues { 推荐, 搜索, 购物车 }
