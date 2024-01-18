package com.adityagupta.arcompanion.api.interfaces

import com.adityagupta.data.entries.Definition
import com.adityagupta.data.lemma.rootWord
import com.adityagupta.data.wordApi.WordApiDefinitionResponse
import com.adityagupta.data.wordApi.WordApiDetailResponse
import com.adityagupta.data.wordApi.WordApiExampleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface Api {

    @GET("{rootedWord}")
    suspend fun getDetailsFromWordApi(@Path("rootedWord") word: String): Response<WordApiDetailResponse>
}