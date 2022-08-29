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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MeaningActivityViewModel(
): ViewModel() {

    private val meaningRepository: MeaningRepository = MeaningRepository()

    private val _wordOxford = MutableLiveData<OxfordWord>()
    val wordOxford : LiveData<OxfordWord> = _wordOxford

    private val _rootedWord = MutableLiveData<String>()
    val rootedWord: LiveData<String> = _rootedWord

    private val _wikiData = MutableLiveData<WikiData>()
    val wikiData: LiveData<WikiData> = _wikiData

    private val wikipediaAPI = WikipediaHelper.getInstance().create(WikipediaAPI::class.java)


    fun getOxfordWordMeaning(word: String){
         viewModelScope.launch {
                val result = meaningRepository.makeOxfordRequest(word)

                _wordOxford.postValue(result)
                _rootedWord.postValue(wordOxford.value?.title ?: "error")
            }

    }

    fun getWikiData(word: String){
        GlobalScope.launch {
            val wikiResult = wikipediaAPI.getPageDetails(word ?: "error", 1)
            val wikidata: WikiData = WikiData(
                wikiResult.body()!!.pages[0].title,
                wikiResult.body()!!.pages[0].excerpt,
                wikiResult.body()!!.pages[0].thumbnail?.url?: "https://i.ibb.co/YhRN0Q3/toppng-com-erreur-404-473x341.png"
            )

            _wikiData.postValue(wikidata)


        }
    }
}