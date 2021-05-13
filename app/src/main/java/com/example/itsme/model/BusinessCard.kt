package com.example.itsme.model

interface BusinessCard {

    var firstName: String
    var lastName: String
    var cardType : CardType
    val elements: HashMap<ElementType, MutableList<String>>

    fun addElement(elementType: ElementType, value: String)

    fun removeElement(elementType: ElementType, value: String)

    fun getElementForType(type: ElementType): List<String>

    fun createDatabaseCard()

    fun getXMLString(): String

}