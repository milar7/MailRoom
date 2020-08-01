package com.example.mailroom.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Mail(
    @PrimaryKey(autoGenerate = true) val mailId : Long,
    //TODO add full text search
    val userSenderId :Long,
    var text : String,
    var type : String,
    val sendDate: Date?
    )
