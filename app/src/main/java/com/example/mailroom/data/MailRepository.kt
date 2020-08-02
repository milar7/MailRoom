package com.example.mailroom.data

import androidx.lifecycle.LiveData
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User

class MailRepository(val db :MailDatabase) {




    suspend fun upsertMail(mail: Mail) = db.getMailDao().upsert(mail)
    suspend fun deleteMail(mail: Mail) = db.getMailDao().deleteMail(mail)
    suspend fun upsertUser(user: User) = db.getMailDao().upsert(user)

    fun getUserMails(id:Long)=db.getMailDao().getUserMails(id)
    fun getUserWithMail()=db.getMailDao().getUserWithMails()
    fun getUsers()=db.getMailDao().getUsers()
    fun getMails()=db.getMailDao().getMails()

    fun searchUsersByName(name:String)=db.getMailDao().searchUsersByName(name)


    fun getUserMailsSenderDesc(id:Long)    =db.getMailDao().getUserMailsSenderDesc(id)
    fun getUserMailsSenderAsc(id:Long)      =db.getMailDao().getUserMailsSenderAsc(id)
    fun getUserMailsReceiverDesc(id:Long) =db.getMailDao().getUserMailsReceiverDesc(id)
    fun getUserMailsReceiverAsc(id:Long)   =db.getMailDao().getUserMailsReceiverAsc(id)

}