package com.example.jonathan.kotlin.coroutines.ui.errorhandling.trycatch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler_view.*
import com.example.jonathan.kotlin.coroutines.R
import com.example.jonathan.kotlin.coroutines.data.api.ApiHelperImpl
import com.example.jonathan.kotlin.coroutines.data.api.RetrofitBuilder
import com.example.jonathan.kotlin.coroutines.data.local.DatabaseBuilder
import com.example.jonathan.kotlin.coroutines.data.local.DatabaseHelperImpl
import com.example.jonathan.kotlin.coroutines.data.model.ApiUser
import com.example.jonathan.kotlin.coroutines.ui.base.ApiUserAdapter
import com.example.jonathan.kotlin.coroutines.utils.Status
import com.example.jonathan.kotlin.coroutines.utils.ViewModelFactory

class TryCatchActivity : AppCompatActivity() {

    private lateinit var viewModel: TryCatchViewModel
    private lateinit var adapter: ApiUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter =
            ApiUserAdapter(
                arrayListOf()
            )
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getUsers().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun renderList(users: List<ApiUser>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        )[TryCatchViewModel::class.java]
    }

}
