package com.astriex.reflection.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp


data class Note(
    var title: String? = null,
    var content: String? = null,
    var imageUrl: String? = null,
    var userId: String? = null,
    var timeAdded: Timestamp? = null,
    var username: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(imageUrl)
        parcel.writeString(userId)
        parcel.writeParcelable(timeAdded, flags)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}