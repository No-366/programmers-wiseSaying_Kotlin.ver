package com.back.domain.wiseSaying.repository

import com.back.domain.wiseSaying.entity.WiseSaying
import com.back.standard.dto.Page

interface WiseSayingRepository {
    fun save(wiseSaying: WiseSaying): WiseSaying

    fun findAll(): List<WiseSaying>

    fun findById(id: Int): WiseSaying?

    fun delete(wiseSaying: WiseSaying)

    fun clear()
    fun build()
    fun findByAuthorLike(s: String): List<WiseSaying>
    fun findByAuthorContent(s: String): List<WiseSaying>
    fun findByKeywordPaged(keywordType: String, keyword: String, itemPerPage: Int, pageNo: Int): Page<WiseSaying>
    fun findAllPaged(itemPerPage: Int, pageNo: Int): Page<WiseSaying>
}