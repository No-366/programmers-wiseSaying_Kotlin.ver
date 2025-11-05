package com.back.global.rq

class Rq(
    cmd: String
) {

    val action: String
    private val paramMap: Map<String, String>

    init {

        val cmdBits = cmd.split("?")
        action = cmdBits[0]

        paramMap = if (cmdBits.size == 2) {

            cmdBits[1]
                .split("&")
                .map {  // aaa=bbb
                    val paramBits = it.split("=")
                    paramBits[0] to paramBits[1]
                }
                .toMap()
        } else {
            emptyMap()
        }
    }

    private fun getParamValue(key: String): String? {
        return paramMap[key]
    }

    fun getParamValueAsInt(key: String, defaultValue: Int): Int {
        return getParamValue(key)
            ?.toIntOrNull() // 문자를 숫자로 ( 숫자로 못바꾸면 널로)
            ?: defaultValue // 엘비스 연산자로 널값이면 디폴트값으로 반환
    }

}