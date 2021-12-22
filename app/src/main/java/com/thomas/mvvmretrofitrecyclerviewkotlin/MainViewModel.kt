package com.thomas.mvvmretrofitrecyclerviewkotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thomas.mvvmretrofitrecyclerviewkotlin.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository)  : ViewModel() {
    val presenceList = MutableLiveData<List<Result>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllMovies() {
        val response = repository.getAllPresences()
        response.enqueue(object : Callback<List<Result>> {
            override fun onResponse(call: Call<List<Result>>, response: Response<List<Result>>) {
                presenceList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Result>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}