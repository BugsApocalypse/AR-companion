package com.adityagupta.arcompanion.repositories

import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.data.OxfordWord


class MeaningRepository {

    private val oxfordApi = RetrofitHelper.getInstance().create(Api::class.java)

    suspend fun makeOxfordRequest(word: String): OxfordWord {

            val result = oxfordApi.getRootWord(word?: "hello")
            if(result.isSuccessful){
                val rootedWord = result.body()!!.results[0].lexicalEntries[0].inflectionOf[0].text.toString()
                val finalResult =  oxfordApi.getDefinition(rootedWord?: "hello")
                if(finalResult.isSuccessful) {
                    return OxfordWord(
                        finalResult.body()?.results?.get(0)?.word ?: "error",
                        finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(
                            0
                        )?.definitions?.get(0) ?: "Looks like even Oxford doesn't know the meaning of the word you are looking for :(",
                        finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(
                            0
                        )?.examples?.get(0)?.text ?: "Check the spelling and try again :)",
                        finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(
                            0
                        )?.audioFile.toString() ?: "https://www.google.com"
                    )

                }else{
                    //TODO(implement a better error handling)
                    return OxfordWord("error", "something went wrong and we are working on it!!", "error", "error")
                }
            }else{
                //TODO(implement a better error handling)
                return OxfordWord("error", "something went wrong and we are working on it!!", "error", "error")
            }

    }
}