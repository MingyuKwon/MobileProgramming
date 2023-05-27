package com.example.recylcerview2201911150

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.recylcerview2201911150.databinding.TodoViewholderStyleBinding
import java.time.LocalDate

class ToDOAdapter(val todos : ArrayList<ToDo>) :
    RecyclerView.Adapter<ToDOAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun OnImageClick(data: ToDo, index : Int)
    }

    var itemClickListener : OnItemClickListener? = null

    inner class ViewHolder(val binding: TodoViewholderStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageView.setOnClickListener {
                itemClickListener?.OnImageClick(todos[adapterPosition], adapterPosition)
            }

        }
    }

    fun addToDo(todo : ToDo)
    {
        todos.add(todo)
        notifyDataSetChanged()
    }

    fun clickItem(index : Int)
    {
        todos[index].isClicked = !todos[index].isClicked
        notifyItemChanged(index)
    }

    fun removeItem(index : Int)
    {
        todos.removeAt(index)
        notifyItemRemoved(index) // 갱신 한 것을 알려주기만 하면 알아서 해준다
    }

    fun moveItem(fromIndex : Int, toIndex : Int)
    {
        todos.add(toIndex, todos.get(fromIndex))
        todos.removeAt(fromIndex)
        notifyItemMoved(fromIndex, toIndex)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TodoViewholderStyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.ToDoText.text = todos[position].todoText
        holder.binding.ToDoPriority.text = todos[position].priority

        val DeadLineGap = (todos[position].DeadLine.year - LocalDate.now().year) * 365 +
                (todos[position].DeadLine.month.value - LocalDate.now().month.value) * 31 +
                (todos[position].DeadLine.dayOfMonth - LocalDate.now().dayOfMonth)


        val leftTime = CalculateLeftTime( DeadLineGap , todos[position].takenTime)

        when
        {
            leftTime > 3 -> holder.binding.TodoTimeLeft.text = "Do later, maybe"
            leftTime > 1 -> holder.binding.TodoTimeLeft.text = "Do if you can"
            leftTime >= 0 -> holder.binding.TodoTimeLeft.text = "Do Right Now"
            else -> holder.binding.TodoTimeLeft.text = "Expired"
        }

        if(todos[position].isClicked)
        {
            holder.binding.imageView.setImageResource(R.drawable.done)
            holder.binding.TodoTimeLeft.text = "Done"
            return
        }

        if(leftTime < 0)
        {
            holder.binding.imageView.setImageResource(R.drawable.expired)
            return
        }

        val priority = CalculateImportance(todos[position].priority , leftTime)

        when(priority)
        {
            3 -> holder.binding.imageView.setImageResource(R.drawable.do_right_now);
            2 -> holder.binding.imageView.setImageResource(R.drawable.do_normal);
            1 -> holder.binding.imageView.setImageResource(R.drawable.do_later);
        }



    }

    private fun CalculateLeftTime(_deadLine : Int, _takenTime : String): Int
    {
        var takenTime : Int = 14

        when(_takenTime)
        {
            "Very Long" -> takenTime = 14
            "Long" -> takenTime = 7
            "Normal" -> takenTime = 3
            "Short" -> takenTime = 2
            "Very Short" -> takenTime = 1
        }

        return _deadLine + 1 - takenTime
    }

    private fun CalculateImportance(_priority : String, _leftTime : Int) : Int
    {
        // return 3 : urgent
        // return 2 : normal
        // return 1 : maybe do it nextTime

        var priority : Float = 5f

        when(_priority)
        {
            "Very Important" -> priority = 5f
            "Important" -> priority = 4f
            "Normal" -> priority = 3f
            "Less Important" -> priority = 2f
            "Not Important" -> priority = 1f
        }


        if( _leftTime <= 0)
        {
            return 3
        }

        when{
            priority / _leftTime > 2 -> return 3
            priority / _leftTime > 0.5 -> return 2
            priority / _leftTime <= 0.5 -> return 1
        }

        return 0

    }
}

