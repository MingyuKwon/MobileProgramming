package com.example.kwonmingyumidtest3210

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kwonmingyumidtest3210.databinding.ActivityMainBinding
import com.example.kwonmingyumidtest3210.databinding.ProductrowBinding
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var itemList1 : ArrayList<Product>
    lateinit var orderList1 : ArrayList<Order>
    lateinit var adapter: MyDataAdapter
    lateinit var adapter2: MyDataAdapter2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initApdater()
        initLayout()
    }

    private fun initLayout() {
        adapter.itemClickListener = object : MyDataAdapter.OnItemClickListener {
            override fun OnItemClick(data: Product, index : Int) {
                if(adapter.clickItem(index))
                {
                    adapter2.ItemAdd(data)
                }
            }

        }

        adapter2.itemClickListener = object : MyDataAdapter2.OnItemClickListener {
            override fun OnItemClick(data: Order, index : Int) {
                if(adapter2.clickItem(index))
                {
                    adapter.ItemAdd(data.productName)
                }
            }

        }
    }

    private fun initApdater() {
        binding.recyclerView1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyDataAdapter(itemList1)
        binding.recyclerView1.adapter = adapter

        binding.recyclerView1

        binding.recyclerView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter2 = MyDataAdapter2(orderList1)
        binding.recyclerView2.adapter = adapter2
    }

    fun initData() {

        itemList1 = ArrayList<Product>()
        itemList1.add(Product("새우깡", 500, 3))
        itemList1.add(Product("감자깡", 600, 2))
        itemList1.add(Product("고구마깡", 300, 5))
        itemList1.add(Product("자갈치", 400, 2))
        itemList1.add(Product("포스틱", 800, 3))

        orderList1 = ArrayList<Order>()

    }
}