package com.example.itsme.db

import android.content.Context
import androidx.lifecycle.LiveData

class BusinessCardRepository(application: Context) {
    private var businessCardDAO: BusinessCardDAO

    private var cardItemList: LiveData<List<BusinessCardWithElements>?>? = null

    init {
        val businessCardDatabase: BusinessCardDatabase =
            BusinessCardDatabase.STATIC.getDatabase(application)
        businessCardDAO = businessCardDatabase.businessCardDAO()
        cardItemList = businessCardDAO.getCards()
    }

    fun getCardItemList(): LiveData<List<BusinessCardWithElements>?>? {
        return cardItemList
    }

    fun addCardItem(cardItem: BusinessCard?, el: MutableList<BusinessCardElement>) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            val id = businessCardDAO.addCard(cardItem)
            el.forEach() {
                it.businessCard = id
                businessCardDAO.addElement(it)
            }
        }
    }

    fun addElementItem(element: BusinessCardElement?) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            businessCardDAO.addElement(
                element
            )
        }
    }

    fun updateCardItem(cardItem: BusinessCard) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            businessCardDAO.updateCard(cardItem)
        }
    }

    fun addElement(element: BusinessCardElement?) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            businessCardDAO.addElement(element)
        }
    }

    fun getCardItemFromId(id: Long): LiveData<BusinessCardWithElements?>? {
        return businessCardDAO.getCardItemFromId(id)
    }

    fun updateElement(element: BusinessCardElement) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            businessCardDAO.updateElement(element)
        }
    }

    fun removeElement(element: BusinessCardElement) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            businessCardDAO.removeElement(element)
        }
    }

    fun delete(card: BusinessCardWithElements) {
        BusinessCardDatabase.STATIC.databaseWriteExecutor.execute {
            card.elements.forEach {
                businessCardDAO.removeElement(it)
            }
            businessCardDAO.removeCard(card.card)
        }
    }
}