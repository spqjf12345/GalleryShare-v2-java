package com.example.project1_final.adapter

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.project1_final.fragments.GalleryFragmentTab
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_tab_button.*

class PageAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var fragments : ArrayList<Fragment> = ArrayList()

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    fun addItems(fragment : Fragment){
        fragments.add(fragment)
    }
}