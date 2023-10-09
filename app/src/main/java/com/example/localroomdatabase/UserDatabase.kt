package com.example.localroomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    companion object{
        var userDatabase : UserDatabase? = null

         fun getInstance(context: Context): UserDatabase{
            if (userDatabase == null){
                userDatabase = Room.databaseBuilder(context,UserDatabase::class.java,
                    context.resources.getString(R.string.app_name)).build()
            }
            return userDatabase!!
        }
    }
}