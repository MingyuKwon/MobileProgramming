package com.example.recylcerview2201911150

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recylcerview2201911150.databinding.ActivityMainBinding
import org.w3c.dom.Text
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: ToDOAdapter

    val toDoArray  = ArrayList<ToDo>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initRecyclerView()
        initLayOut()
    }


    private fun initRecyclerView() {
        binding.ToDoRecycle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ToDOAdapter(toDoArray)

        adapter.itemClickListener = object : ToDOAdapter.OnItemClickListener {
            override fun OnImageClick(data: ToDo, index: Int) {
                adapter.clickItem(index)
            }

        }

        binding.ToDoRecycle.adapter = adapter

        val simpleCallback = object :  ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.ToDoRecycle)

    }

    private fun initAdapter() {
        binding.apply {
            var items = resources.getStringArray(R.array.todo_priority)
            val adapter1 = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items)
            PriorityInput.adapter = adapter1

            items = resources.getStringArray(R.array.todo_TimeTaken)
            val adapter2 = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items)
            TakenTimeInput.adapter = adapter2

            items = resources.getStringArray(R.array.year)
            val adapter3 = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items)
            YearInput.adapter = adapter3

            items = resources.getStringArray(R.array.month)
            val adapter4 = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item , items)
            MonthInput.adapter = adapter4

            items = resources.getStringArray(R.array.day)
            val adapter5 = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, items)
            DayInput.adapter = adapter5

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initLayOut() {
        binding.add.setOnClickListener{

            Toast.makeText(this, binding.EditText.text.toString() , Toast.LENGTH_SHORT)

            adapter.addToDo(ToDo(
                binding.EditText.text.toString(),
                binding.PriorityInput.selectedItem.toString() ,
                binding.TakenTimeInput.selectedItem.toString(),
                LocalDate.of(binding.YearInput.selectedItem.toString().toInt(), binding.MonthInput.selectedItem.toString().toInt() , binding.DayInput.selectedItem.toString().toInt()),
                false))

        }
    }
}