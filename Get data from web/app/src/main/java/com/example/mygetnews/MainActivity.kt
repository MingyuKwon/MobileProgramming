package com.example.mygetnews

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygetnews.databinding.ActivityMainBinding
import com.example.mygetnews.databinding.RowBinding
import com.example.mygetnews201911150.MyAdapter
import com.example.mygetnews201911150.MyData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter

    val url = "https://news.daum.net"
    val rssurl = "https://fs.jtbc.joins.com//RSS/culture.xml"
    val melonurl = "https://www.melon.com/chart/index.htm"


    val scope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getmelonnews()
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(ArrayList<MyData>())

        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                Toast.makeText(this@MainActivity, adapter.items[position].url , Toast.LENGTH_SHORT).show()
            }

        }

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        binding.recyclerView.adapter = adapter
        getmelonnews()
    }

    private fun getnews() {
        scope.launch {
            adapter.items.clear()

            val doc = Jsoup.connect(url).get()
            val headlines = doc.select("ul.list_newsissue>li>div.item_issue>div>strong.tit_g>a")
            for(news in headlines){
                adapter.items.add(MyData(news.text(), news.absUrl("href")))
                withContext(Dispatchers.Main){
                    adapter.notifyDataSetChanged()
                    binding.swipe.isRefreshing = false
                }

            }
        }
    }

    private fun getmelonnews() {
        scope.launch {
            adapter.items.clear()

            val doc = Jsoup.connect(melonurl).get()
            val headlines = doc.select("#frm > div > table > tbody > tr ")

            for(music in headlines){
                val name = music.select( "td:nth-child(6) > div > div > div.ellipsis.rank01 > span > a").text()
                val singer = music.select( "td:nth-child(6) > div > div > div.ellipsis.rank02 > a").text()

                adapter.items.add(MyData(name, singer))
                withContext(Dispatchers.Main){
                    adapter.notifyDataSetChanged()
                    binding.swipe.isRefreshing = false
                }
            }
        }
    }

    private fun getrssnews() {
        scope.launch {
            adapter.items.clear()

            val doc = Jsoup.connect(rssurl).parser(Parser.xmlParser()).get()
            val headlines = doc.select("item")


            for(news in headlines){
                for(news in headlines){
                    adapter.items.add(MyData(news.select("title").text(), news.select("link").text()))
                }
                withContext(Dispatchers.Main){
                    adapter.notifyDataSetChanged()
                    binding.swipe.isRefreshing = false
                }

            }
        }
    }
}