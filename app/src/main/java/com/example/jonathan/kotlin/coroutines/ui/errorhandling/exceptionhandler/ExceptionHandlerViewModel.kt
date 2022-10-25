package com.example.jonathan.kotlin.coroutines.ui.errorhandling.exceptionhandler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import com.example.jonathan.kotlin.coroutines.data.api.ApiHelper
import com.example.jonathan.kotlin.coroutines.data.local.DatabaseHelper
import com.example.jonathan.kotlin.coroutines.data.model.ApiUser
import com.example.jonathan.kotlin.coroutines.utils.Resource

class ExceptionHandlerViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<ApiUser>>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        users.postValue(Resource.error("Something Went Wrong"))
    }

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch(exceptionHandler) {
            users.postValue(Resource.loading())
            val usersFromApi = apiHelper.getUsers()
            users.postValue(Resource.success(usersFromApi))
        }
    }

    fun getUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }

}