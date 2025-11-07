package com.back.standard.dto

import kotlin.math.ceil

class Page<T> (
    val totalItems: Int,
    val itemsPerPage: Int,
    val pageNo: Int,
    val keywordType: String,
    val keyword: String,
    val content: List<T>
){
    val totalPages = ceil(totalItems.toDouble() / itemsPerPage).toInt()
}