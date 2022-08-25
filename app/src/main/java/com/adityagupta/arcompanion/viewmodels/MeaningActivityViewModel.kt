package com.adityagupta.arcompanion.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.helpers.WikipediaHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.arcompanion.api.interfaces.WikipediaAPI
import com.adityagupta.data.OxfordWord
import com.adityagupta.data.WikiData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MeaningActivityViewModel: ViewModel() {

    private val _wordOxford = MutableLiveData<OxfordWord>()
    val wordOxford : LiveData<OxfordWord> = _wordOxford

    private val _rootedWord = MutableLiveData<String>()
    val rootedWord: LiveData<String> = _rootedWord

    private val _wikiData = MutableLiveData<WikiData>()
    val wikiData: LiveData<WikiData> = _wikiData

    private val oxfordApi = RetrofitHelper.getInstance().create(Api::class.java)
    private val wikipediaAPI = WikipediaHelper.getInstance().create(WikipediaAPI::class.java)


    fun getOxfordWordMeaning(word: String){
        GlobalScope.launch {
            val result = oxfordApi.getRootWord(word?: "hello")
            var rtwd = result.body()!!.results[0].lexicalEntries[0].inflectionOf[0].text.toString()
            _rootedWord.postValue(rtwd)

            val finalResult =  oxfordApi.getDefinition(rtwd?: "hello")

            val wordObject: OxfordWord = OxfordWord(
                finalResult.body()?.results?.get(0)?.word!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.definitions?.get(0)!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.senses?.get(0)?.examples?.get(0)?.text!!,
                finalResult.body()?.results?.get(0)?.lexicalEntries?.get(0)?.entries?.get(0)?.pronunciations?.get(0)?.audioFile.toString()!!
            )
            _wordOxford.postValue(wordObject)
        }
    }

    fun getWikiData(word: String){
        GlobalScope.launch {
            val wikiResult = wikipediaAPI.getPageDetails(word ?: "error", 1)
            val wikidata: WikiData = WikiData(
                wikiResult.body()!!.pages[0].title,
                wikiResult.body()!!.pages[0].excerpt,
            )

            _wikiData.postValue(wikidata)


        }
    }
}