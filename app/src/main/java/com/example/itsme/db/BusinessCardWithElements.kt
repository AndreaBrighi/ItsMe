package com.example.itsme.db

import androidx.room.Embedded
import androidx.room.Relation

data class BusinessCardWithElements(
    @Embedded val card: BusinessCard,
    @Relation(
        parentColumn = "uid",
        entityColumn = "business_card"
    )
    val elements: List<BusinessCardElement>
)