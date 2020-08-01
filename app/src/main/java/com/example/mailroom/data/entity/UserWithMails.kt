package com.example.mailroom.data.entity

import androidx.room.Embedded
import androidx.room.Relation


data class UserWithMails (
@Embedded val user :User,
@Relation(
    parentColumn = "userId",
    entityColumn = "userSenderId"
)
val mails :List<Mail>
)