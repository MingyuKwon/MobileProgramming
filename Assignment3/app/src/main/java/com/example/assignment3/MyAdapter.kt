package com.example.assignment3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>(), Filterable {

    var flag = false

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            val filteredList: ArrayList<MyData> = arrayListOf<MyData>()

            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = items
                results.count = items.size

                return results
            }
            else {
                for (notice in items) {
                    if (notice.noticeTitle.contains(filterString)) {
                        filteredList.add(notice)
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size

            return results

        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults) {
            filteredItems.clear()
            filteredItems.addAll(filterResults.values as ArrayList<MyData>)
            flag = true
            notifyDataSetChanged()
        }

    }


    var filteredItems = ArrayList<MyData>()
    var itemFilter = ItemFilter()
    override fun getFilter(): Filter {
        return itemFilter
    }

    init {
        filteredItems.addAll(items)
    }

    interface OnItemClickListener{
        fun OnItemClick(position : Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class MyViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)
    {
        init{
            binding.noticeTitle.setOnClickListener{
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : RowBinding = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(flag)
        {
            return filteredItems.size
        }else
        {
            return items.size
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(flag)
        {
            holder.binding.noticeSort.text = filteredItems[position].noticeSort
            holder.binding.noticeTitle.text = filteredItems[position].noticeTitle
            holder.binding.writtenDate.text = filteredItems[position].writtenDate
        }else
        {
            holder.binding.noticeSort.text = items[position].noticeSort
            holder.binding.noticeTitle.text = items[position].noticeTitle
            holder.binding.writtenDate.text = items[position].writtenDate
        }


    }
}