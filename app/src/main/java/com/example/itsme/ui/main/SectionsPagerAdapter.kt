package com.example.itsme.ui.main

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.itsme.R

class SectionsPagerAdapter(private val fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    private val tabTitles = fa.resources.getStringArray(R.array.tabs_text)
    private val tabIcons = fa.resources.obtainTypedArray(R.array.tab_icons)

    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return MainFragment.newInstance(position + 1)
    }

    fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }

    override fun getItemCount(): Int {
        // Show 2 total pages.
        return tabTitles.size
    }

    fun getPageIcon(position: Int): Drawable? {
        return ResourcesCompat.getDrawable(
            fa.resources,
            tabIcons.getResourceId(position, 0),
            fa.theme
        )
    }
}