package tw.edu.pu.csim.tcyang.s1130311 // 依據你的 package 修改

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExamScreen(viewModel: ExamViewModel = viewModel()) {
    // 1. 取得 Context 用來讀取螢幕像素
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    // 2. 程式啟動時，讀取並更新寬高
    LaunchedEffect(Unit) {
        viewModel.updateScreenDimensions(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

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
}