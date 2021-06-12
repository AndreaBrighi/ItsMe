package com.example.itsme.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.itsme.model.CardTypes

@Entity(tableName = "business_cards")
data class BusinessCard(
    @PrimaryKey(autoGenerate = true)
    val uidC: Long = 0,
    @ColumnInfo(name = "first_name")
    var firstName: String,
    @ColumnInfo(name = "last_name")
    var lastName: String,
    @ColumnInfo(name = "type")
    var types: CardTypes,
    @ColumnInfo(name = "received")
    val received: Boolean

)