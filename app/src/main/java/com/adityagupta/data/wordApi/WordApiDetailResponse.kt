package com.adityagupta.data.wordApi

data class WordApiDetailResponse(
    val frequency: Double,
    val results: List<Result>,
    val word: String
)