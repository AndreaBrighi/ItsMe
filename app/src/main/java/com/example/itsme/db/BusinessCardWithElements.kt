package com.example.itsme.db

import android.util.Xml
import androidx.room.Embedded
import androidx.room.Relation
import com.example.itsme.model.writeDocument
import com.example.itsme.model.writeElement

data class BusinessCardWithElements(
    @Embedded val card: BusinessCard,
    @Relation(
        parentColumn = "uidC",
        entityColumn = "business_card"
    )
    val elements: MutableList<BusinessCardElement>
) {

    fun getXMLString(): String {
        val xmlSerializer = Xml.newSerializer()
        return xmlSerializer.writeDocument {
            writeElement("Card") {
                writeElement("firstName", card.firstName)
                writeElement("lastName", card.lastName)
                for (elementType in elements.groupBy { it.elementType }) {
                    writeElement(elementType.key.name) {
                        for (string in elementType.value) {
                            writeElement("element", string.value)
                        }
                    }
                }
            }
        }
    }
}