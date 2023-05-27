package com.example.tabpageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tabpageview.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val textArray = arrayListOf<String>("Image", "List", "TeamMates")
    val imageArray = arrayListOf<Int>(R.drawable.baseline_image_24, R.drawable.baseline_format_list_numbered_24, R.drawable.baseline_accessibility_24)


    val imageFrag = ImageFragment()
    val itemFrag = ItemFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewpager){
            tab, position ->
            tab.text = textArray[position]
            tab.setIcon(imageArray[position])
        }.attach()

    }
}