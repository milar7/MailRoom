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
    @Delete
    suspend fun deleteMail(mail: Mail)

    @Query("SELECT * FROM User")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE name LIKE '%' || :name || '%' OR role LIKE '%' || :name || '%' ")
    fun searchUsersByName(name:String): LiveData<List<User>>

    @Query("SELECT * FROM Mail")
    fun getMails(): LiveData<List<Mail>>

    @Query("SELECT * FROM Mail WHERE senderuserId = :id or receiveruserId =:id")
    fun getUserMails(id:Long): LiveData<List<Mail>>


    @Query("SELECT * FROM Mail WHERE senderuserId = :id ORDER BY sendDate DESC")
    fun getUserMailsSenderDesc(id:Long): LiveData<List<Mail>>

    @Query("SELECT * FROM Mail WHERE receiveruserId = :id ORDER BY sendDate DESC")
    fun getUserMailsReceiverDesc(id:Long): LiveData<List<Mail>>

    @Query("SELECT * FROM Mail WHERE senderuserId = :id ORDER BY sendDate ASC")
    fun getUserMailsSenderAsc(id:Long): LiveData<List<Mail>>
    @Query("SELECT * FROM Mail WHERE receiveruserId = :id ORDER BY sendDate ASC")
    fun getUserMailsReceiverAsc(id:Long): LiveData<List<Mail>>

    @Transaction
    @Query("SELECT * FROM User")
    fun getUserWithMails(): LiveData<List<UserWithMails>>

}