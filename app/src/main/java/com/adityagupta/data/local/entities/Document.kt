package com.adityagupta.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DocumentMeta (
    @PrimaryKey val docId: Int,
    @ColumnInfo(name = "docTitle") val docTitle: String,
    @ColumnInfo(name = "docLocalUri") val docLocalUri: String
)