package com.adityagupta.data.wordApi

data class Result(
    val definition: String,
    val examples: List<String>,
    val partOfSpeech: String,
    val synonyms: List<String>,
    val typeOf: List<String>
)