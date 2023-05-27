package com.example.assignment3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>(), Filterable {

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
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.noticeSort.text = items[position].noticeSort
        holder.binding.noticeTitle.text = items[position].noticeTitle
        holder.binding.writtenDate.text = items[position].writtenDate

    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

}