package com.example.itsme.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface BusinessCardDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCard(card: BusinessCard?): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateCard(card: BusinessCard?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addElement(element: BusinessCardElement?)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateElement(element: BusinessCardElement?)

    @Delete
    fun removeElement(element: BusinessCardElement?)

    @Delete
    fun removeCard(card: BusinessCard?)


    @Transaction
    @Query("SELECT * FROM business_cards AS BC LEFT JOIN business_card_elements AS BCE ON (BCE.business_card = BC.uidC)")
    fun getCards(): LiveData<List<BusinessCardWithElements>?>?

    @Transaction
    @Query("SELECT * FROM business_cards AS BC LEFT JOIN business_card_elements AS BCE ON (BCE.business_card = BC.uidC) WHERE BC.uidC = :id ")
    fun getCardItemFromId(id: Long): LiveData<BusinessCardWithElements?>?
}