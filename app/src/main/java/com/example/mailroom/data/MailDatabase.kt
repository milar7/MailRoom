package com.example.mailroom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mailroom.data.converter.Converters
import com.example.mailroom.data.dao.MailDao
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User

@Database(entities = [Mail::class,User::class],version = 1)
@TypeConverters(Converters::class)
abstract class MailDatabase : RoomDatabase() {

    abstract fun getMailDao():MailDao

    companion object {
        @Volatile
        private var instance: MailDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MailDatabase::class.java,
                "mail_room_db"
            ).build()


    }

}

