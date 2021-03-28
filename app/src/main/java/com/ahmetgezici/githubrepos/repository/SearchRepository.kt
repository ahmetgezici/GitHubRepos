package com.ahmetgezici.githubrepos.repository

import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.githubrepos.api.ApiClient
import com.ahmetgezici.githubrepos.api.ApiInterface
import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.utils.datautil.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchRepository {

    var apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getRepos(userName: String): MutableLiveData<Resource<List<Repo>>> {

        val liveData = MutableLiveData<Resource<List<Repo>>>()

        liveData.postValue(Resource.loading())

        apiInterface.getRepos(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ reposList ->
                liveData.postValue(Resource.success(reposList))
            }, { throwable ->
                liveData.postValue(Resource.error(throwable.message!!))
            }).isDisposed

        return liveData
    }

}