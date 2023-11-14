package com.adityagupta.arcompanion.activities.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.adityagupta.data.local.dao.DocumentDao
import com.adityagupta.data.local.entities.Document
import kotlinx.coroutines.launch

class PageViewModel(private val documentDao: DocumentDao) : ViewModel() {

    val allDocuments: LiveData<List<Document>> = documentDao.getAllDocuments().asLiveData()


    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = _index.map {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    public fun insertDocument(document: Document) {
        viewModelScope.launch {
            documentDao.insert(document)
        }
    }
}

class LibraryFragmentViewModelFactory(private val documentDao: DocumentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PageViewModel(documentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}