package com.back.domain.wiseSaying.entity

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
        get() {
            return """
                {
                    "id": $id,
                    "content": "$content",
                    "author": "$author"
                }
            """.trimIndent()
        }


}