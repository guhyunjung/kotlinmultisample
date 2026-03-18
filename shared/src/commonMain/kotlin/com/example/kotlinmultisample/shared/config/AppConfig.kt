package com.example.kotlinmultisample.shared.config

/**
 * 단순한 AppConfig DTO (YAML의 최소 필드를 포함)
 * 복잡한 구조가 필요하면 추후 kotlinx-serialization + kaml로 교체하세요.
 */
data class StockConfig(
    val apiBase: String = "",
    val serviceKey: String = ""
)

data class AppConfigDto(
    val stock: StockConfig = StockConfig()
)

/** 플랫폼별 actual 구현이 제공해야 하는 리소스 로더 */
expect fun loadResourceText(path: String): String?

/**
 * 매우 단순한 YAML 파서: 'stock:' 블록에서 'apiBase'와 'serviceKey'를 추출합니다.
 * (프로덕션에서는 안정적인 YAML 파서 사용 권장)
 */
fun loadAppConfig(): AppConfigDto? {
    val text = loadResourceText("/config.yaml") ?: return null
    var inStock = false
    var apiBase: String = ""
    var serviceKey: String = ""

    val kvRegex = Regex("^([A-Za-z0-9_]+)\\s*:\\s*([\\"']?)(.*?)\\2\\s*$")

    val lines = text.lines()
    for (line in lines) {
        val trimmed = line.trimStart()
        if (!inStock) {
            if (trimmed.startsWith("stock:")) {
                inStock = true
            }
            continue
        }

        // 종료 조건: 다음 최상위 키 등장 (no indent)
        if (line.isNotEmpty() && !line.first().isWhitespace()) break

        val m = kvRegex.find(trimmed)
        if (m != null) {
            val key = m.groupValues[1]
            val value = m.groupValues[3]
            when (key) {
                "apiBase" -> apiBase = value
                "serviceKey" -> serviceKey = value
            }
        }
    }

    return AppConfigDto(stock = StockConfig(apiBase = apiBase, serviceKey = serviceKey))
}


