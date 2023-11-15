package com.adityagupta.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Document(
    @PrimaryKey(autoGenerate = true) val docId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String = "",
    @ColumnInfo(name = "creationDate") val creationDate: String = "" ,
    @ColumnInfo(name = "creator") val creator: String = "",
    @ColumnInfo(name = "producer") val producer: String = "",
    @ColumnInfo(name = "subject") val subject: String = "",
    @ColumnInfo(name = "docLocalUri") val docLocalUri: String
)