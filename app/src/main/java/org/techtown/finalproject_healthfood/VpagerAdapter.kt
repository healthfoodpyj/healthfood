package org.techtown.finalproject_healthfood

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int { // 프래그먼트 갯수 리턴
        return 3
    }

    override fun createFragment(position: Int): Fragment { //어떤 프래그먼트 리턴?
        when(position){
            0 -> return FragmentA()
            1 -> return FragmentB()
            2 -> return FragmentC()
            else -> return FragmentA()
        }
    }

}