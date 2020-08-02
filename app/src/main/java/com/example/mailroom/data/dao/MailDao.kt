package com.example.mailroom.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mailroom.data.entity.Mail
import com.example.mailroom.data.entity.User
import com.example.mailroom.data.entity.UserWithMails

@Dao
interface MailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mail: Mail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User)

    @Query("SELECT * FROM User")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE name LIKE '%' || :name || '%'")
    fun searchUsersByName(name:String): LiveData<List<User>>

    @Query("SELECT * FROM Mail ORDER BY sendDate desc ")
    fun getMails(): LiveData<List<Mail>>

    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithMails(): LiveData<List<UserWithMails>>

}