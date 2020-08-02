package com.example.mailroom.ui.users

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mailroom.data.MailRepository
import com.example.mailroom.data.entity.User
import kotlinx.coroutines.launch

class UserViewModel(
    app: Application,
  private val mailRepository: MailRepository
) : ViewModel() {
    fun addNewUser(user: User)=viewModelScope.launch {
        mailRepository.upsertUser(user)
    }

    fun getUsers ()= mailRepository.getUsers()


}