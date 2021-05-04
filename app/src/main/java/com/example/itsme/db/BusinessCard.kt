package com.example.itsme.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_cards")
data class BusinessCard(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String?,
    @ColumnInfo(name = "last_name")
    val lastName: String?,
    @ColumnInfo(name = "type")
    val type: BusinessCardType

)