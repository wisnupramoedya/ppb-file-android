package com.thomas.mvvmretrofitrecyclerviewkotlin

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thomas.mvvmretrofitrecyclerviewkotlin.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository)  : ViewModel() {
    val presenceList = MutableLiveData<List<Result>>()
    val errorMessage = MutableLiveData<String>()
    val presence = MutableLiveData<Result>()

    fun getAllPresences() {
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

    fun postPresence(name: String, nik: String, image_mask: String) {
        val response = repository.postPresence(name, nik, image_mask)
        response.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                presence.postValue(response.body())
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}