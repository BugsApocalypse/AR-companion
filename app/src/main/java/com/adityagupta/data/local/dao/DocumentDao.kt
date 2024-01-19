package com.adityagupta.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adityagupta.data.local.entities.Document
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Query("SELECT * FROM Document")
    fun getAllDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM Document WHERE docId IN (:documentIds)")
    fun loadAllByIds(documentIds: IntArray): List<Document>

    @Query("SELECT * FROM Document WHERE title LIKE :title")
    fun findByTitle(title: String): List<Document>

    @Query("UPDATE Document SET currentLocation = :newCurrentLocation WHERE docId LIKE :documentId")
    suspend fun updateDocumentLocation(newCurrentLocation: Long, documentId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( documents: Document)

    @Update
    suspend fun update(documents: Document)

    @Delete
    suspend fun delete(document: Document)
}