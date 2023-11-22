package com.adityagupta.arcompanion

import android.app.Application
import com.adityagupta.data.local.database.AppDatabase

class ARCompanionApplication: Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}