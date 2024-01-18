package com.adityagupta.data.wordApi

data class WordApiDefinitionResponse(
    val definitions: List<Definition>,
    val word: String
)