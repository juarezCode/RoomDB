package com.juarez.ktbdusuarios.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val firstSurname: String,
    val secondSurname: String,
    val age: Int,
    val ticketNumber: Int
)
