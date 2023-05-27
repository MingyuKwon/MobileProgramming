package com.example.kwonmingyumidtest3210

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kwonmingyumidtest3210.databinding.ProductrowBinding


class MyDataAdapter(val items:ArrayList<Product>)
    : RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: Product, index : Int)

    }
    var itemClickListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: ProductrowBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.buybtn.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)

            }

        }
    }

    fun ItemAdd(name : String)
    {
        var flag = false

        var i = 0

        for(order in items)
        {
            if(order.productName == name)
            {
                flag = true
                break
            }
            i++
        }

        if(flag)
        {
            items[i].leftAmount++
            notifyItemChanged(i)
        }

    }


    fun clickItem(index : Int) : Boolean
    {
        if(items[index].leftAmount > 0)
        {
            items[index].leftAmount--
            notifyItemChanged(index)
            return true
        }

        return false

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productname.text = items[position].productName
        holder.binding.priceview.text = items[position].productPrice.toString()
        holder.binding.buybtn.text = items[position].leftAmount.toString()

    }
}