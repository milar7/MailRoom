package com.example.mailroom.ui.mails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User
import kotlinx.coroutines.launch

class MailsViewModel(
    app: Application,
    val mailRepository: MailRepository
) : ViewModel() {


    fun addUser(user: User)=viewModelScope.launch {
        mailRepository.upsertUser(user)
    }
    fun addMail(mail: Mail)=viewModelScope.launch {
        mailRepository.upsertMail(mail)
    }
    fun deleteMail(mail: Mail)=viewModelScope.launch {
        mailRepository.deleteMail(mail)
    }
    fun getUserMails(id:Long)=mailRepository.getUserMails(id)
    fun getUsers()=mailRepository.getUsers()
    fun getMails()=mailRepository.getMails()
    fun searchUsersByName(name :String)=mailRepository.searchUsersByName(name)
}