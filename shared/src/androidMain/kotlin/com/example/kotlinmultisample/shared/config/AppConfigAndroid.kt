package com.example.kotlinmultisample.shared.config

import android.content.Context

// Android는 Context를 필요로 하므로 앱 시작 시 아래 holder에 context를 설정해야 합니다.
object AndroidConfigLoader {
    var appContext: Context? = null
}

actual fun loadResourceText(path: String): String? {
    // 1) 클래스패스 리소스 시도
    val clsRes = object {}.javaClass.getResource(path)
    if (clsRes != null) return clsRes.readText()

    // 2) assets에서 시도 (앱에서 AndroidConfigLoader.appContext를 초기화해야 함)
    val ctx = AndroidConfigLoader.appContext ?: return null
    return try {
        ctx.assets.open("config.yaml").bufferedReader().use { it.readText() }
    } catch (_: Exception) {
        null
    }
}


