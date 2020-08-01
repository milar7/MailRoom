package com.example.mailroom.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User (

    @PrimaryKey(autoGenerate = true) val userId : Long,
    val name : String,
    val role :String,
    val age :Int
)
