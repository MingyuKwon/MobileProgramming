package com.example.assignment3

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    var url : String  = "https://fifaonline4.nexon.com/news/notice/view?n4ArticleSN=4098"

    var list = arrayListOf<Fragment>()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        list.add(NoticeListFragment())
        list.add(NoticeSeeFragment.newInstance(url))

        return when(position) {
            0 -> list[0]
            1-> list[1]
            else -> list[0]
        }
    }

    fun updateFragment()
    {
        (list[1] as NoticeSeeFragment).binding.webView.loadUrl(url)
    }

}