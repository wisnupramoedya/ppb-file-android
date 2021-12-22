package com.wisnupram.uploadfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wisnupram.uploadfile.model.Result
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository): ViewModel() {
    val presenceList = MutableLiveData<List<Result>>()
    val errorMesssage = MutableLiveData<String>()

    fun getAllPresence() {
        val response = repository.getAllPresence()
        response.enqueue(object: Callback<List<Result>> {
            override fun onResponse(call: Call<List<Result>>, response: Response<List<Result>>) {
                presenceList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Result>>, t: Throwable) {
                errorMesssage.postValue(t.message)
            }
        })
    }
}