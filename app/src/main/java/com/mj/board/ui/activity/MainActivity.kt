package com.mj.board.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.mj.board.R
import com.mj.board.databinding.ActivityMainBinding
import com.mj.board.ui.fragment.CategoryFragment
import com.mj.board.ui.fragment.MemoFragment
import com.mj.board.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        binding.pagerContent.adapter = viewPagerAdapter
        binding.pagerContent.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.layoutTab))
        binding.layoutTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.pagerContent.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })

    }

    inner class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentStatePagerAdapter(fm, behavior) {

        var fragments = ArrayList<Fragment>()

        init {
            fragments.add(MemoFragment())
            fragments.add(CategoryFragment())
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }


}