package com.example.kwonmingyumidtest3210

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kwonmingyumidtest3210.databinding.OrderrowBinding


class MyDataAdapter2(val orders:ArrayList<Order>)
    : RecyclerView.Adapter<MyDataAdapter2.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: Order, index : Int)
    }
    var itemClickListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: OrderrowBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.orderview.setOnClickListener{
                itemClickListener?.OnItemClick(orders[adapterPosition], adapterPosition)

            }
        }
    }

    fun clickItem(index : Int) : Boolean
    {
        if(orders[index].amount > 0)
        {
            orders[index].amount--
            notifyItemChanged(index)
            return true
        }

        return false
        notifyItemChanged(index)
    }

    fun ItemAdd(data: Product)
    {
        var flag = false

        var i = 0

        for(order in orders)
        {
            if(order.productName == data.productName)
            {
                flag = true
                break
            }
            i++
        }

        if(flag)
        {
            orders[i].amount++
            notifyItemChanged(i)
        }else
        {
            orders.add(Order(data.productName, 1))
            notifyDataSetChanged()
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productname.text = orders[position].productName
        holder.binding.orderview.text = orders[position].amount.toString()

    }
}