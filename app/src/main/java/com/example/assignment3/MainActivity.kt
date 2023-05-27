package com.example.assignment3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    val textarr = arrayListOf<String>("공지 목록", "공지 보기")
    val imgarr = arrayListOf<Int>(R.drawable.baseline_format_list_bulleted_24, R.drawable.baseline_web_24)

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    fun initLayout() {

        binding.viewPager.adapter = MyViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, pos ->
            tab.text = textarr[pos]
            tab.setIcon(imgarr[pos])
        }.attach()


    }

    fun changeURL(url : String) {
        (binding.viewPager.adapter as MyViewPagerAdapter).url = url
        (binding.viewPager.adapter as MyViewPagerAdapter).updateUrl()
    }


}