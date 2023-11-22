package com.adityagupta.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import android.os.Parcel
import android.os.Parcelable

@Entity
data class Document(
    @PrimaryKey(autoGenerate = true) val docId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String = "",
    @ColumnInfo(name = "creationDate") val creationDate: String = "",
    @ColumnInfo(name = "creator") val creator: String = "",
    @ColumnInfo(name = "producer") val producer: String = "",
    @ColumnInfo(name = "subject") val subject: String = "",
    @ColumnInfo(name = "docLocalUri") val docLocalUri: String
) : Parcelable {

    // Implement the writeToParcel method
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(docId)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(creationDate)
        parcel.writeString(creator)
        parcel.writeString(producer)
        parcel.writeString(subject)
        parcel.writeString(docLocalUri)
    }

    // Implement the describeContents method
    override fun describeContents(): Int {
        return 0
    }

    // Companion object with CREATOR field
    companion object CREATOR : Parcelable.Creator<Document> {
        // Implement the createFromParcel method
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        // Implement the newArray method
        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }

    // Secondary constructor to create a Document from a Parcel
    private constructor(parcel: Parcel) : this(
        docId = parcel.readLong(),
        title = parcel.readString() ?: "",
        author = parcel.readString() ?: "",
        creationDate = parcel.readString() ?: "",
        creator = parcel.readString() ?: "",
        producer = parcel.readString() ?: "",
        subject = parcel.readString() ?: "",
        docLocalUri = parcel.readString() ?: ""
    )
}
