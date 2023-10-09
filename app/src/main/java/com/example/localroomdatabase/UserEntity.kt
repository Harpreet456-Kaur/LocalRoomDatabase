package com.example.localroomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo()
    var name: String? = null,

    @ColumnInfo()
    var information: String? = null,

)