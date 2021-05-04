package com.example.itsme.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "business_card_elements")
data class BusinessCardElement(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    @ColumnInfo(name = "business_card")
    val businessCard: Long,
    @ColumnInfo(name = "element_type")
    val firstName: BusinessCardElementType,
    @ColumnInfo(name = " value")
    val value: String
)
