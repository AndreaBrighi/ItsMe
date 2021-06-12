package com.example.itsme.recyclerview.element

enum class States: State {
    EDIT {

        override fun editButton(): Boolean {
            return true
        }

        override fun actionButton(): Boolean {
            return false
        }
    }, ACTION {
        override fun editButton(): Boolean {
            return false
        }

        override fun actionButton(): Boolean {
            return true
        }
    }, VIEW {
        override fun editButton(): Boolean {
            return false
        }

        override fun actionButton(): Boolean {
            return false
        }
    }
}

interface State {

    fun editButton(): Boolean

    fun actionButton(): Boolean
}

