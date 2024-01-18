package com.adityagupta.arcompanion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.arcompanion.repositories.MeaningRepository

class ViewModelFactory(private val meaningRepository: MeaningRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeaningActivityViewModel::class.java)) {
            return MeaningActivityViewModel(meaningRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
