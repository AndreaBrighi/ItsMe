package com.example.itsme.model

import android.content.Intent
import android.net.Uri
import android.text.InputType
import com.example.itsme.R

enum class ElementTypes : ElementType {

    PHONE {
        override fun getIcon(): Int {
            return R.drawable.ic_baseline_call_24
        }

        override fun getTitle(): Int {
            return R.string.cell
        }

        override fun getInput(): Int {
            return InputType.TYPE_CLASS_PHONE
        }

        override fun getIntent(value: String): Intent {
            return Intent(Intent.ACTION_DIAL, Uri.parse("tel:$value"))
        }
    },
    MAIL {
        override fun getIcon(): Int {
            return R.drawable.ic_baseline_email_24
        }

        override fun getTitle(): Int {
            return R.string.email
        }

        override fun getInput(): Int {
            return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        override fun getIntent(value: String): Intent {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this

            intent.putExtra(Intent.EXTRA_EMAIL, value)
            return intent
        }
    },
    WEB_PAGE{
        override fun getIcon(): Int {
            return R.drawable.ic_baseline_web_24
        }

        override fun getTitle(): Int {
            return R.string.web
        }

        override fun getInput(): Int {
            return InputType.TYPE_CLASS_TEXT
        }

        override fun getIntent(value: String): Intent {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(value)
            return intent
        }
    },
    FACEBOOK{
        override fun getIcon(): Int {
            return R.drawable.ic_baseline_facebook_24
        }

        override fun getTitle(): Int {
            return R.string.facebook
        }

        override fun getInput(): Int {
            return InputType.TYPE_CLASS_TEXT
        }

        override fun getIntent(value: String): Intent {
            return Intent(Intent.ACTION_VIEW, Uri.parse(value))
        }
    },
    INSTAGRAM{
        override fun getIcon(): Int {
            return R.drawable.ic_baseline_instagram_24
        }

        override fun getTitle(): Int {
            return R.string.instagram
        }

        override fun getInput(): Int {
            return InputType.TYPE_CLASS_TEXT
        }

        override fun getIntent(value: String): Intent {
            val path = "https://instagram.com/$value"
            return Intent(Intent.ACTION_VIEW, Uri.parse(path))
        }
    }
}

interface ElementType {

    fun getIcon(): Int

    fun getTitle(): Int

    fun getInput(): Int

    fun getIntent(value: String): Intent
}