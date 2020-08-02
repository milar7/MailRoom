package com.example.mailroom.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User (

    @PrimaryKey(autoGenerate = true) val userId : Long?=null,
    val name : String,
    val role :String
): Parcelable
