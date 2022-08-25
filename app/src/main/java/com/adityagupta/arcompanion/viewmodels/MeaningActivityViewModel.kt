package com.adityagupta.arcompanion.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.helpers.WikipediaHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.arcompanion.api.interfaces.WikipediaAPI
import com.adityagupta.data.OxfordWord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MeaningActivityViewModel: ViewModel() {

    private val _wordOxford = MutableLiveData<OxfordWord>()
    val wordOxford : LiveData<OxfordWord> = _wordOxford

    private val oxfordApi = RetrofitHelper.getInstance().create(Api::class.java)
    private val wikipediaAPI = WikipediaHelper.getInstance().create(WikipediaAPI::class.java)


    fun getOxfordWordMeaning(word: String){
        GlobalScope.launch {
            val result = oxfordApi.getRootWord(word?: "hello")
            val rootedWord = result.body()!!.results[0].lexicalEntries[0].inflectionOf[0].text.toString()

            val finalResult =  oxfordApi.getDefinition(rootedWord?: "hello")
            val wikiResult = wikipediaAPI.getPageDetails(rootedWord, 1)

            val wordObject: OxfordWord = OxfordWord(
                finalResult.body()?.results?.get(0)?.word!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.definitions?.get(0)!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.examples?.get(0)?.text!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(0)?.audioFile.toString()!!
            )
            _wordOxford.postValue(wordObject)
        }
    }
}