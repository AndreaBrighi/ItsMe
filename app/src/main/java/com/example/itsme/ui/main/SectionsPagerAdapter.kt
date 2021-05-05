package com.example.itsme.ui.main

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.itsme.R

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    private val tabsTitle = fa.resources.getStringArray(R.array.tabs_text)
    private val tabsIcon = fa.resources.obtainTypedArray(R.array.tabs_icon)

    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1)
    }

    fun getPageTitle(position: Int): CharSequence {
        return tabsTitle[position]
    }

    override fun getItemCount(): Int {
        // Show 2 total pages.
        return tabsTitle.size
    }

    fun getPageIcon(position: Int): Drawable? {
        return ResourcesCompat.getDrawable(
            fa.resources,
            tabsIcon.getResourceId(tabsIcon.getIndex(position), -1),
            fa.theme
        )
    }
}