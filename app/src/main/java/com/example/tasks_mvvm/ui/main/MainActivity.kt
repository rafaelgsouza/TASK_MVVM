package com.example.tasks_mvvm.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks_mvvm.R
import com.example.tasks_mvvm.databinding.ActivityMainBinding
import com.example.tasks_mvvm.databinding.DialogTaskBinding
import com.example.tasks_mvvm.ui.adapter.TaskAdapter
import com.example.tasks_mvvm.ui.listener.TaskClickListener

class MainActivity : AppCompatActivity(), View.OnClickListener, TaskClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = TaskAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    override fun onClick(view: View){
        if(view.id == R.id.button_add){
            newTask()
        }
    }

    override fun clickDone(position: Int) {
        viewModel.handleDone(position)
    }

    private fun setupRecyclerView() {
        binding.recyclerTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerTasks.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.tasks.observe(this, Observer {
            adapter.submitDataset(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.insertedTask.observe(this,Observer{
            val str: String = if(it){
                "Tarefa inserida com sucesso."
            } else {
                "Erro ao inserir a tarefa."
            }

            Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        })

        viewModel.updatedTask.observe(this, Observer{
            val str: String = if(it){
                "Estado da tarefa alterado com sucesso."
            } else {
                "Estado da tarefa não foi alterado."
            }

            Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupListeners() {
        binding.buttonAdd.setOnClickListener(this)
    }

    private fun newTask() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_task, null)

        val bindingDialog: DialogTaskBinding = DialogTaskBinding.bind(dialogView)

        val builder = AlertDialog.Builder(this).setView(dialogView).setTitle("Adicionar nova tarefa").setPositiveButton("Salvar", DialogInterface.OnClickListener{
            dialog, which ->
                val str = bindingDialog.editDescription.text.toString()
                viewModel.addTask(str)
                dialog.dismiss()
        })
        .setNegativeButton("Cancelar", DialogInterface.OnClickListener{
                dialog, which ->
                    dialog.dismiss()
            })

        val dialog = builder.create()
        dialog.show()
    }
}