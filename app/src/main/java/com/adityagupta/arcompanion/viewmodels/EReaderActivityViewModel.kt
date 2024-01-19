package com.adityagupta.arcompanion.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adityagupta.data.local.dao.DocumentDao
import com.adityagupta.data.local.entities.Document

class EReaderActivityViewModel(private val documentDao: DocumentDao): ViewModel()  {

    val allDocuments: LiveData<List<Document>> = documentDao.getAllDocuments().asLiveData()


}