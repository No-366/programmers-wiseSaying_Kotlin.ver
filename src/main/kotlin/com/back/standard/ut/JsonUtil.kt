package com.back.standard.ut


// 유틸은 상태값을 갖지 않는 오브젝트로 만들어주는것이 좋다
object JsonUtil {
    fun jsonStrToMap(jsonStr: String): Map<String, Any> {
        return jsonStr
            .removeSurrounding("{", "}")
            .split(",")
            .mapNotNull {
                val keyValue = it
                    .split(":", limit = 2)
                    .map(String::trim)

                if (keyValue.size != 2) return@mapNotNull null

                val key = keyValue[0].removeSurrounding("\"")
                val value = if (keyValue[1].startsWith("\"") && keyValue[1].endsWith("\"")) {
                    keyValue[1].removeSurrounding("\"")
                } else {
                    keyValue[1].toInt()
                }

                key to value
            }.toMap()
    }

    // Map 리스트를 JSON 문자열로 변환
    fun toString(mapList: List<Map<String, Any?>>): String {
        return mapList.joinToString(
            prefix = "[\n", separator = ",\n", postfix = "\n]"
        ) { map -> toString(map).prependIndent("    ") }
    }

    // Map을 JSON 문자열로 변환
    fun toString(map: Map<String, Any?>): String {
        return map.entries.joinToString(
            prefix = "{\n", separator = ",\n", postfix = "\n}"
        ) { (key, value) ->
            val formattedKey = "\"$key\""
            val formattedValue = when (value) {
                is String -> "\"$value\""
                else -> value
            }
            "    $formattedKey: $formattedValue"
        }
    }
}