package com.example.itsme.db

enum class BusinessCardElementType : Link {
    EMAIL {
        override val value: String
            get() = TODO("Not yet implemented")
    },
    TELEPHONE_NUMBER {
        override val value: String
            get() = TODO("Not yet implemented")
    },
    FAX {
        override val value: String
            get() = TODO("Not yet implemented")
    },
    FACEBOOK {
        override val value: String
            get() = TODO("Not yet implemented")
    },
    INSTAGRAM {
        override val value: String
            get() = TODO("Not yet implemented")
    },
    WEB_SITE {
        override val value: String
            get() = TODO("Not yet implemented")
    }
}

interface Link {
    val value: String
}