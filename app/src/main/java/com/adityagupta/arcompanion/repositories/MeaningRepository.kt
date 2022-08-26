package com.adityagupta.arcompanion.repositories

import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.data.OxfordWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}


class MeaningRepository {

    private val oxfordApi = RetrofitHelper.getInstance().create(Api::class.java)

    suspend fun makeOxfordRequest(word: String): OxfordWord {

            val result = oxfordApi.getRootWord(word?: "hello")
            var rtwd = result.body()!!.results[0].lexicalEntries[0].inflectionOf[0].text.toString()

            val finalResult =  oxfordApi.getDefinition(rtwd?: "hello")

            val wordObject: OxfordWord = OxfordWord(
                finalResult.body()?.results?.get(0)?.word!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.definitions?.get(0)!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.examples?.get(0)?.text!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(0)?.audioFile.toString()!!
            )
        return wordObject

    }
}