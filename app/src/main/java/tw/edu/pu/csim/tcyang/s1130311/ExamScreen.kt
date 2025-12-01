package tw.edu.pu.csim.tcyang.s1130311

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExamScreen(viewModel: ExamViewModel = viewModel()) {
    // 1. 取得 Context 用來讀取螢幕像素
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val density = LocalDensity.current // 取得螢幕密度，用於 px 轉 dp
    // 計算 300px 對應的 dp 值
    // 這樣設定寬高，在任何螢幕上都會剛好是 300個物理像素
    val iconSizeDp = with(density) { 300.toDp() }

    // 2. 程式啟動時，讀取並更新寬高
    LaunchedEffect(Unit) {
        viewModel.updateScreenDimensions(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    // 最外層改用 Box，方便做絕對位置的對齊
    Box(modifier = Modifier.fillMaxSize()) {
        // 3. 畫面佈局
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
        // 4. 處理「下方切齊螢幕高 1/2」的圖片
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f) // 高度佔螢幕 50%
                .align(Alignment.TopCenter) // 放在螢幕上半部
        ) {
            // 嬰幼兒：左邊切齊，下方切齊螢幕一半
            Image(
                painter = painterResource(id = R.drawable.role0), // 請換成正確圖片ID
                contentDescription = "嬰幼兒",
                modifier = Modifier
                    .size(iconSizeDp)
                    .align(Alignment.BottomStart) // 左下角對齊 (父容器是上半屏，所以這裡就是螢幕正中間左側)
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

        // 5. 處理「下方切齊螢幕底部」的圖片
        // 直接對齊最外層 Box 的底部
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
    }
}