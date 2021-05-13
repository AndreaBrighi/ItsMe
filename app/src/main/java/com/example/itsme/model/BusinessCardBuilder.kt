package com.example.itsme.model

class BusinessCardBuilder(elementTypes: List<ElementType>) {

    private var isBuild: Boolean = false
    private var firstName: String = ""
    private var lastName: String = ""
    private lateinit var cardType: CardType
    private val elements: HashMap<ElementType, MutableList<String>> = HashMap()

    init {
        for (type in elementTypes) {
            this.elements[type] = ArrayList()
        }
    }

    fun setFirstName(firstName: String): BusinessCardBuilder {
        this.firstName = firstName
        return this
    }

    fun setLastName(lastName: String): BusinessCardBuilder {
        this.lastName = lastName
        return this
    }

    fun setType(type: CardType): BusinessCardBuilder {
        this.cardType = type
        return this
    }

    @kotlin.jvm.Throws(IllegalArgumentException::class)
    fun addElement(elementType: ElementType, value: String): BusinessCardBuilder {
        if (elements.containsKey(elementType)) {
            if (!elements[elementType]!!.contains(value)) {
                elements[elementType]!!.add(value)
            } else {
                throw IllegalArgumentException()
            }
        } else {
            throw IllegalArgumentException()
        }
        return this
    }

    @kotlin.jvm.Throws(IllegalStateException::class)
    fun build(): BusinessCard {
        if (isBuild) {
            throw IllegalStateException()
        }
        return object : AbstractBusinessCard() {
            override var firstName: String
                get() = this@BusinessCardBuilder.firstName
                set(value) {
                    this@BusinessCardBuilder.firstName = value
                }
            override var lastName: String
                get() = this@BusinessCardBuilder.lastName
                set(value) {
                    this@BusinessCardBuilder.lastName = value
                }
            override var cardType: CardType
                get() = this@BusinessCardBuilder.cardType
                set(value) {
                    this@BusinessCardBuilder.cardType = value
                }
            override val elements: HashMap<ElementType, MutableList<String>>
                get() = this@BusinessCardBuilder.elements
        }
    }
}