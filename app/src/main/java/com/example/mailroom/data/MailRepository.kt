package com.example.mailroom.data

import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User

class MailRepository(val db :MailDatabase) {




    suspend fun upsertMail(mail: Mail) = db.getMailDao().upsert(mail)
    suspend fun upsertUser(user: User) = db.getMailDao().upsert(user)

    fun getUserWithMail()=db.getMailDao().getUserWithMails()


}