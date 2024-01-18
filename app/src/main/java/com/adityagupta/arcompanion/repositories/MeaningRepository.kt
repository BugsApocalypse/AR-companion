package com.adityagupta.arcompanion.repositories

import android.util.Log
import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.data.OxfordWord
import com.adityagupta.data.wordApi.Definition
import com.adityagupta.data.wordApi.WordApiDetailResponse
class MeaningRepository(private val api: Api) {

    suspend fun makeWordApiDetailRequest(word: String): WordApiDetailResponse? {
        return runCatching {
            api.getDetailsFromWordApi(word)
        }.getOrNull()?.takeIf { it.isSuccessful }?.body()
    }
}



