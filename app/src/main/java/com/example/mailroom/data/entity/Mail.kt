package com.example.mailroom.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Mail(
    @PrimaryKey(autoGenerate = true) val mailId : Long?=null,
    //TODO add full text search
    val userSenderId :Long,
    @Embedded(prefix = "sender")
    val sender :User,
    @Embedded(prefix = "receiver")
    val receiver :User,
    var title : String,
    var text : String,
    var type : String,
    val sendDate: Date?
    ): Parcelable
