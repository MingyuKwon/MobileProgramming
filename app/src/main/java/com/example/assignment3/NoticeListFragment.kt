package com.example.assignment3

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.databinding.FragmentNoticeListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup



// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class NoticeListFragment : Fragment() {

    inner class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }


    lateinit var binding: FragmentNoticeListBinding
    lateinit var adapter: MyAdapter

    val scope = CoroutineScope(Dispatchers.IO)

    val url = "https://fifaonline4.nexon.com/news/notice/list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoticeListBinding.inflate(inflater, container ,false)
        initLayout()

        return binding.root
    }

    fun initLayout()
    {
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getNotice()
        }

        var searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(str: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(str: String): Boolean {
                    adapter.getFilter().filter(str)
                    return false
                }
            }

        binding.searchView.setOnQueryTextListener(searchViewTextListener)

        adapter = MyAdapter(ArrayList<MyData>())

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val spaceDecoration = VerticalSpaceItemDecoration(40)
        binding.recyclerView.addItemDecoration(spaceDecoration)

        binding.recyclerView.setHasFixedSize(true)

        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                (requireActivity() as MainActivity).changeURL(adapter.items[position].url)
            }

        }

        getNotice()
    }



    private fun getNotice() {

        scope.launch {
            adapter.items.clear()

            val doc = Jsoup.connect(url).get()
            val tableRows = doc.select("#divListPart > div.board_list > div.content > div.list_wrap > div.tbody ")

            var al = arrayListOf<String>()
            for (i : Int in 1..20 )
            {
                val anchor = tableRows.select("div:nth-child($i) > a")

                for(content in anchor)
                {
                    val noticeSort = content.select("span.td.sort").text()
                    val noticeText = content.select("span.td.subject").text()
                    val writtenDate = content.select("span.td.date").text()

                    val noticeUrl = content.absUrl("href")

                    adapter.items.add(MyData(noticeSort, noticeText, writtenDate, noticeUrl))
                    withContext(Dispatchers.Main){
                        adapter.notifyDataSetChanged()
                        binding.swipe.isRefreshing = false
                    }
                }

            }




            for(music in tableRows){
                //val name = music.select( "td:nth-child(6) > div > div > div.ellipsis.rank01 > span > a").text()
                //val singer = music.select( "td:nth-child(6) > div > div > div.ellipsis.rank02 > a").text()

                //adapter.items.add(MyData(name, singer))
            }
        }


        binding.swipe.isRefreshing = false
    }


}