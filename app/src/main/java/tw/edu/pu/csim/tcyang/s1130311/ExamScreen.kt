package tw.edu.pu.csim.tcyang.s1130311

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
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

data class ServiceAnswer(
    val imageId: Int,
    val correctRoleId: Int,
    val description: String
)

@Composable
fun ExamScreen(viewModel: ExamViewModel = viewModel()) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val density = LocalDensity.current

    val screenWidth = displayMetrics.widthPixels
    val screenHeight = displayMetrics.heightPixels
    val iconSizePx = 300
    val iconSizeDp = with(density) { iconSizePx.toDp() }

    val serviceAnswers = listOf(
        ServiceAnswer(R.drawable.service0, R.drawable.role0, "極早期療育，屬於嬰幼兒方面的服務"),
        ServiceAnswer(R.drawable.service1, R.drawable.role1, "離島服務，屬於兒童方面的服務"),
        ServiceAnswer(R.drawable.service2, R.drawable.role2, "極重多障，屬於成人方面的服務"),
        ServiceAnswer(R.drawable.service3, R.drawable.role3, "輔具服務，屬於一般民眾方面的服務")
    )

    var currentQuestion by remember { mutableStateOf(serviceAnswers.random()) }
    var statusText by remember { mutableStateOf("") }

    var iconX by remember { mutableFloatStateOf((screenWidth - iconSizePx) / 2f) }
    var iconY by remember { mutableFloatStateOf(0f) }

    var currentToast by remember { mutableStateOf<Toast?>(null) }

    // 鎖定變數：防止碰撞邏輯重複執行
    var isProcessing by remember { mutableStateOf(false) }

    fun showToast(message: String) {
        currentToast?.cancel()
        val newToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        newToast.show()
        currentToast = newToast
    }

    fun isColliding(targetX: Float, targetY: Float): Boolean {
        return iconX < targetX + iconSizePx &&
                iconX + iconSizePx > targetX &&
                iconY < targetY + iconSizePx &&
                iconY + iconSizePx > targetY
    }

    fun nextQuestion() {
        // 重置位置到正上方中間
        iconY = 0f
        iconX = (screenWidth - iconSizePx) / 2f
        currentQuestion = serviceAnswers.random()

        statusText = ""
        currentToast?.cancel() // 確保上一題的 Toast 消失
    }

    suspend fun handleCollision(hitRoleId: Int?, isBottom: Boolean) {
        if (isProcessing) return
        isProcessing = true // 鎖定

        if (isBottom) {
            viewModel.score -= 1
            statusText = "(掉到最下方)"
            showToast("掉到最下方")
        } else if (hitRoleId != null) {
            if (hitRoleId == currentQuestion.correctRoleId) {
                viewModel.score += 1
            } else {
                viewModel.score -= 1
            }
            statusText = when (hitRoleId) {
                R.drawable.role0 -> "(碰撞嬰幼兒圖示)"
                R.drawable.role1 -> "(碰撞兒童圖示)"
                R.drawable.role2 -> "(碰撞成人圖示)"
                R.drawable.role3 -> "(碰撞一般民眾圖示)"
                else -> ""
            }
            showToast(currentQuestion.description)
        }

        delay(3000) // 暫停 3 秒讓使用者看結果

        nextQuestion() // 出下一題 (重置座標)

        // ★★★ 關鍵修正：多等待一點時間，讓座標重置生效後，才解鎖迴圈 ★★★
        // 這能防止迴圈在座標還沒歸零前就誤判碰撞
        delay(500)

        isProcessing = false // 解鎖，開始下一題掉落
    }

    LaunchedEffect(Unit) {
        viewModel.updateScreenDimensions(screenWidth, screenHeight)
    }

    // 遊戲迴圈
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)

            if (isProcessing) continue

            iconY += 20

            // ★★★ 安全區域檢查：必須掉下超過 100px 才開始偵測 ★★★
            // 防止因為剛出生在頂部，而誤判撞到中間的角色
            if (iconY < 100f) {
                continue
            }

            // 碰撞檢查
            if (isColliding(0f, (screenHeight / 2f) - iconSizePx)) {
                handleCollision(R.drawable.role0, false)
                continue
            }
            if (isColliding((screenWidth - iconSizePx).toFloat(), (screenHeight / 2f) - iconSizePx)) {
                handleCollision(R.drawable.role1, false)
                continue
            }
            if (isColliding(0f, (screenHeight - iconSizePx).toFloat())) {
                handleCollision(R.drawable.role2, false)
                continue
            }
            if (isColliding((screenWidth - iconSizePx).toFloat(), (screenHeight - iconSizePx).toFloat())) {
                handleCollision(R.drawable.role3, false)
                continue
            }
            if (iconY + iconSizePx >= screenHeight) {
                handleCollision(null, true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.happy), contentDescription = null)
            Text(text = "瑪利亞基金會服務大考驗", color = Color.Black)
            Text(text = "作者：資管二B 林建宇", color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = viewModel.screenInfo, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "成績：${viewModel.score}分 $statusText",
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).align(Alignment.TopCenter)
        ) {
            Image(painter = painterResource(id = R.drawable.role0), contentDescription = "嬰幼兒",
                modifier = Modifier.size(iconSizeDp).align(Alignment.BottomStart))
            Image(painter = painterResource(id = R.drawable.role1), contentDescription = "兒童",
                modifier = Modifier.size(iconSizeDp).align(Alignment.BottomEnd))
        }
        Image(painter = painterResource(id = R.drawable.role2), contentDescription = "成人",
            modifier = Modifier.size(iconSizeDp).align(Alignment.BottomStart))
        Image(painter = painterResource(id = R.drawable.role3), contentDescription = "一般民眾",
            modifier = Modifier.size(iconSizeDp).align(Alignment.BottomEnd))

        Image(
            painter = painterResource(id = currentQuestion.imageId),
            contentDescription = "Service",
            modifier = Modifier
                .size(iconSizeDp)
                .offset { IntOffset(iconX.toInt(), iconY.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (!isProcessing) {
                            change.consume()
                            iconX += dragAmount.x
                        }
                    }
                }
        )
    }
}