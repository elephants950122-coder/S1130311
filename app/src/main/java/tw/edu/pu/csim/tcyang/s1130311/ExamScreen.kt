package tw.edu.pu.csim.tcyang.s1130311

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun ExamScreen(viewModel: ExamViewModel = viewModel()) {
    // 取得 Context 用來讀取螢幕像素
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val density = LocalDensity.current // 取得螢幕密度，用於 px 轉 dp
    // 計算 300px 對應的 dp 值
    // 這樣設定寬高，在任何螢幕上都會剛好是 300個物理像素
    val iconSizePx = 300
    val iconSizeDp = with(density) { iconSizePx.toDp() }

    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    //遊戲變數設定
    //隨機掉落的 4 張「服務圖示」
    val services = listOf(
        R.drawable.service0,
        R.drawable.service1,
        R.drawable.service2,
        R.drawable.service3
    )
    // 隨機選一張服務圖示
    var currentService by remember { mutableIntStateOf(services.random()) }
    // 掉落圖示的座標 (X, Y)
    // 初始 X: 水平置中
    // 初始 Y: 0 (螢幕最上方)
    var iconX by remember { mutableFloatStateOf((screenWidth - iconSizePx) / 2f) }
    var iconY by remember { mutableFloatStateOf(0f) }

    // 程式啟動時，讀取並更新寬高
    LaunchedEffect(Unit) {
        viewModel.updateScreenDimensions(screenWidth, screenHeight)
    }
    // 掉落迴圈 (每 0.1 秒往下 20px)
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            iconY += 20
            // 檢查觸底
            if (iconY + iconSizePx >= screenHeight) {
                // 重置位置
                iconY = 0f
                iconX = (screenWidth - iconSizePx) / 2f
                // 重新隨機產生一張「服務圖示」
                currentService = services.random()
            }
        }
    }

    // 用 Box，方便做絕對位置的對齊
    Box(modifier = Modifier.fillMaxSize()) {
        //畫面佈局
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow), // 題目要求：黃色背景
            verticalArrangement = Arrangement.Center, // 垂直置中
            horizontalAlignment = Alignment.CenterHorizontally // 水平置中
        ) {
            // 圖片
            Image(
                painter = painterResource(id = R.drawable.happy),
                contentDescription = "背景圖",
            )
            // 文字：顯示作者資訊
            Text(
                text = "瑪利亞基金會服務大考驗",
                color = Color.Black
            )
            Text(
                text = "作者：資管二B 林建宇",
                color = Color.Black
            )
            // 間距高度 10dp
            Spacer(modifier = Modifier.height(10.dp))
            // 文字：顯示螢幕寬高 (從 ViewModel 讀取)
            Text(
                text = viewModel.screenInfo,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "成績：0分",
                color = Color.Black
            )
        }
        // 處理「下方切齊螢幕高 1/2」的圖片
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f) // 高度佔螢幕 50%
                .align(Alignment.TopCenter) // 放在螢幕上半部
        ) {
            // 嬰幼兒：左邊切齊，下方切齊螢幕一半
            Image(
                painter = painterResource(id = R.drawable.role0),
                contentDescription = "嬰幼兒",
                modifier = Modifier
                    .size(iconSizeDp)
                    .align(Alignment.BottomStart) // 左下角對齊
            )

            // 兒童：右邊切齊，下方切齊螢幕一半
            Image(
                painter = painterResource(id = R.drawable.role1),
                contentDescription = "兒童",
                modifier = Modifier
                    .size(iconSizeDp)
                    .align(Alignment.BottomEnd) // 右下角對齊
            )
        }
        // 處理「下方切齊螢幕底部」的圖片
        // 成人：左邊切齊，下方切齊螢幕
        Image(
            painter = painterResource(id = R.drawable.role2),
            contentDescription = "成人",
            modifier = Modifier
                .size(iconSizeDp)
                .align(Alignment.BottomStart) // 左下角對齊整個螢幕
        )

        // 一般民眾：右邊切齊，下方切齊螢幕
        Image(
            painter = painterResource(id = R.drawable.role3),
            contentDescription = "一般民眾",
            modifier = Modifier
                .size(iconSizeDp)
                .align(Alignment.BottomEnd) // 右下角對齊整個螢幕
        )
        //掉落的「服務圖示」
        Image(
            painter = painterResource(id = currentService), // 這裡顯示隨機選到的 service 圖
            contentDescription = "Service",
            modifier = Modifier
                .size(iconSizeDp)
                .offset { IntOffset(iconX.toInt(), iconY.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        iconX += dragAmount.x // 水平拖曳
                    }
                }
        )
    }
}