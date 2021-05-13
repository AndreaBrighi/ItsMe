package com.example.itsme.model

import com.example.itsme.R

enum class CardTypes : CardType {
    SOCIAL {
        override val stringResource: Int
            get() = R.string.social
    },
    WORK {
        override val stringResource: Int
            get() = R.string.work
    }
}

interface CardType {
    val stringResource: Int
}