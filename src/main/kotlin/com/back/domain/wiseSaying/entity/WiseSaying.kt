package com.back.domain.wiseSaying.entity

import com.back.global.standard.ut.JsonUtil.jsonStrToMap

data class WiseSaying(
    var id: Int = 0,
    var content: String,
    var author: String
) {

    fun modify(content: String, author: String) {
        this.content = content
        this.author = author
    }

    fun isNew(): Boolean {
        return id == 0
    }

    val jsonStr: String
        get() { // 커스텀 getter
            return """
                {
                    "id": $id,
                    "content": "$content",
                    "author": "$author"
                }
            """.trimIndent()
        }

    companion object{
        fun fromJsonStr(jsonStr: String): WiseSaying{
            val map = jsonStrToMap(jsonStr)

            return WiseSaying(
                id = map["id"] as Int,
                content = map["content"] as String,
                author = map["author"] as String
            )
        }
    }


}