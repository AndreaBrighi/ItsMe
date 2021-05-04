package com.example.itsme.db

import com.example.itsme.R

enum class BusinessCardType : Type {
    SOCIAL {
        override val stringResource: Int
            get() = R.string.social
    },
    WORK {
        override val stringResource: Int
            get() = R.string.work
    }
}

interface Type {
    val stringResource: Int
}