package com.example.jonathan.kotlin.coroutines.ui.task.twotasks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_long_running_task.*
import kotlinx.android.synthetic.main.activity_recycler_view.progressBar
import com.example.jonathan.kotlin.coroutines.R
import com.example.jonathan.kotlin.coroutines.data.api.ApiHelperImpl
import com.example.jonathan.kotlin.coroutines.data.api.RetrofitBuilder
import com.example.jonathan.kotlin.coroutines.data.local.DatabaseBuilder
import com.example.jonathan.kotlin.coroutines.data.local.DatabaseHelperImpl
import com.example.jonathan.kotlin.coroutines.utils.Status
import com.example.jonathan.kotlin.coroutines.utils.ViewModelFactory

class TwoLongRunningTasksActivity : AppCompatActivity() {

    private lateinit var viewModel: TwoLongRunningTasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_running_task)
        setupViewModel()
        setupLongRunningTask()
    }

    private fun setupLongRunningTask() {
        viewModel.getStatus().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    textView.text = it.data
                    textView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.startLongRunningTask()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        )[TwoLongRunningTasksViewModel::class.java]
    }
}
