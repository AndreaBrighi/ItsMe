package com.example.itsme.recyclerview

import android.content.Intent
import android.net.Uri
import com.example.itsme.R

enum class ElementType : Element {

    PHONE {
        override fun getIcon(): Int {
            return R.drawable.ic_baseline_call_24
        }

        override fun getTitle(): Int {
            return R.string.cell
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

        override fun getIntent(value: String): Intent {
            val path = "https://instagram.com/$value"
            return Intent(Intent.ACTION_VIEW, Uri.parse(path))
        }
    }
}

interface Element {

    fun getIcon(): Int

    fun getTitle(): Int

    fun getIntent(value: String): Intent
}