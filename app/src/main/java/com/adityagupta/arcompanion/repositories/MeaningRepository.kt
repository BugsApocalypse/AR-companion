package com.adityagupta.arcompanion.repositories

import android.util.Log
import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.data.OxfordWord
import com.adityagupta.data.wordApi.Definition
import com.adityagupta.data.wordApi.WordApiDetailResponse
import retrofit2.Response

class MeaningRepository(private val api: Api) {

    suspend fun makeWordApiDetailRequest(word: String): WordApiDetailResponse? {
        return try {
            val response: Response<WordApiDetailResponse> = api.getDetailsFromWordApi(word)

            if (response.isSuccessful) {
                response.body()
            } else {
                // Handle unsuccessful response here
                Log.e("MeaningRepository", "Unsuccessful response: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            // Handle general exceptions here
            Log.e("MeaningRepository", "Exception: ${e.message}")
            null
        }
    }
}
