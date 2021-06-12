package com.example.itsme.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.itsme.model.ElementTypes


@Entity(tableName = "business_card_elements")
data class BusinessCardElement(
    @ColumnInfo(name = "business_card")
    var businessCard: Long,
    @ColumnInfo(name = "element_type")
    val elementType: ElementTypes,
    @ColumnInfo(name = " value")
    var value: String,
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0
)
