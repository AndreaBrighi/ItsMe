package com.example.itsme.model

import android.util.Xml

abstract class AbstractBusinessCard : BusinessCard {

    override fun addElement(elementType: ElementType, value: String) {
        if (elements.containsKey(elementType)) {
            if (!elements[elementType]!!.contains(value)) {
                elements[elementType]!!.add(value)
            } else {
                throw IllegalArgumentException()
            }
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun removeElement(elementType: ElementType, value: String) {
        if (elements.containsKey(elementType)) {
            if (elements[elementType]!!.contains(value)) {
                elements[elementType]!!.removeAll { it == value }
            } else {
                throw IllegalArgumentException()
            }
        } else {
            throw IllegalArgumentException()
        }
    }

    @kotlin.jvm.Throws(IllegalArgumentException::class)
    override fun getElementForType(type: ElementType): List<String> {
        if (elements.containsKey(type)) {
            return elements[type]!!
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun createDatabaseCard() {
        TODO("Not yet implemented")
    }

    override fun getXMLString(): String {
        val xmlSerializer = Xml.newSerializer()
        return xmlSerializer.writeDocument {
            writeElement("Card") {
                writeElement("firstName", firstName)
                writeElement("lastName", lastName)
                writeElement("cardType", cardType.toString())
                for (elementType in elements.keys) {
                    writeElement(elementType.toString()) {
                        for (string in elements[elementType]!!) {
                            writeElement("element", string)
                        }
                    }
                }
            }
        }
    }
}