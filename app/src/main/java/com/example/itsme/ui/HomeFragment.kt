package com.example.itsme.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.itsme.AddActivity
import com.example.itsme.ReceivedActivity
import com.example.itsme.model.XMLManager
import com.example.itsme.databinding.FragmentHomeBinding
import com.example.itsme.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        val sectionsPagerAdapter = SectionsPagerAdapter(this.requireActivity())
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getPageTitle(position)
            tab.icon = sectionsPagerAdapter.getPageIcon(position)
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                animateFab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.fabDownload.setOnClickListener {
            val intent = Intent(context, ReceivedActivity::class.java)
            startActivity(intent)
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            startActivity(intent)
        }


        executorService.execute {
            val tmp = XMLManager()
            tmp.readXML(tmp.writeXML())
        }
        return binding.root

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