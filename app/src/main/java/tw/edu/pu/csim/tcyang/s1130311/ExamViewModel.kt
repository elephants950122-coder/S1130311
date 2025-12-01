package tw.edu.pu.csim.tcyang.s1130311

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExamViewModel : ViewModel() {
    // 儲存螢幕資訊的變數
    var screenInfo by mutableStateOf("讀取中...")
        private set

    // 更新函式
    fun updateScreenDimensions(width: Int, height: Int) {
        // 格式為：螢幕大小：寬度*高度 (包含小數點)
        screenInfo = "螢幕大小：${width.toFloat()} * ${height.toFloat()}"
    }
}