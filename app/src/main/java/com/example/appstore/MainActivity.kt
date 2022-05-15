package com.example.appstore

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.appstore.databinding.ActivityMainBinding
import com.example.appstore.ui.app.AppFragment
import com.example.appstore.ui.games.GamesFragment
import com.example.appstore.ui.today.AddAppFragment
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.hide()

        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        viewPager.offscreenPageLimit = 2

        tabLayout.setupWithViewPager(viewPager)

        val pagerAdapter = ViewPageAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        pagerAdapter.addFragment(AppFragment(), "Apps")
        pagerAdapter.addFragment(GamesFragment(), "Games")
        pagerAdapter.addFragment(AddAppFragment(), "Add App")

        viewPager.adapter = pagerAdapter

        setupTabIcon()

    }

    private fun setupTabIcon() {
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_app)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_dashboard_black_24dp)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_notifications_black_24dp)
    }

    override fun onBackPressed() {
        if(viewPager.currentItem == 0) {
            super.onBackPressed()
        }
        else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}