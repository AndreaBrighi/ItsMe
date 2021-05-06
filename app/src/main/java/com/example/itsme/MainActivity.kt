package com.example.itsme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.itsme.databinding.ActivityMainBinding
import com.example.itsme.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getPageTitle(position)
            tab.icon = sectionsPagerAdapter.getPageIcon(position)
        }.attach()

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                animateFab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        executorService.execute {
            val tmp = XMLManager()
            tmp.readXML(tmp.writeXML())
        }

    }

    private fun animateFab(position: Int) {
        when (position) {
            0 -> {
                binding.fabAdd.show()
                binding.fabDownload.hide()
            }
            1 -> {
                binding.fabDownload.show()
                binding.fabAdd.hide()
            }
            else -> {
                binding.fabAdd.show()
                binding.fabDownload.hide()
            }
        }
    }
}