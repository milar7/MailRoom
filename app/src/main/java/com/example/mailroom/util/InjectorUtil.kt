package com.example.mailroom.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mailroom.data.MailRepository
import com.example.mailroom.ui.mails.MailsViewModel
import com.example.mailroom.ui.users.UserViewModel

object InjectorUtil {


    class MailViewModelProviderFactory
        (
        val app: Application
        , val mailRepository: MailRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MailsViewModel(app, mailRepository) as T
        }

    }
    class UserViewModelProviderFactory
        (
        val app: Application
        , val mailRepository: MailRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(app, mailRepository) as T
        }

    }
}