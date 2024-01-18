package com.adityagupta.arcompanion.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.helpers.WikipediaHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.arcompanion.api.interfaces.WikipediaAPI
import com.adityagupta.arcompanion.repositories.MeaningRepository
import com.adityagupta.data.OxfordWord
import com.adityagupta.data.WikiData
import com.adityagupta.data.wordApi.Definition
import com.adityagupta.data.wordApi.WordApiDetailResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MeaningActivityViewModel(private val meaningRepository: MeaningRepository): ViewModel() {


    private val _wikiData = MutableLiveData<WikiData>()
    val wikiData: LiveData<WikiData> = _wikiData

    private val _wordApiDetails = MutableLiveData<WordApiDetailResponse?>()
    val wordApiDetails: MutableLiveData<WordApiDetailResponse?> = _wordApiDetails

    private val wikipediaAPI = WikipediaHelper.getInstance().create(WikipediaAPI::class.java)

    fun getOxfordWordMeaning(word: String){
         viewModelScope.launch {
                val wordApiResult = meaningRepository.makeWordApiDetailRequest(word)
                _wordApiDetails.postValue(wordApiResult)
            }
    }

    fun getWikiData(word: String) {
        viewModelScope.launch {
            val wikiResult = wikipediaAPI.getPageDetails(word ?: "error", 1)
            val wikidata = WikiData(
                wikiResult.body()?.pages?.getOrNull(0)?.title.orEmpty(),
                wikiResult.body()?.pages?.getOrNull(0)?.excerpt.orEmpty(),
                wikiResult.body()?.pages?.getOrNull(0)?.thumbnail?.url.orEmpty()
                    ?: "https://i.ibb.co/YhRN0Q3/toppng-com-erreur-404-473x341.png"
            )
            _wikiData.postValue(wikidata)
        }
    }
}