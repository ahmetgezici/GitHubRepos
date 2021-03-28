package com.ahmetgezici.githubrepos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.repository.SearchRepository
import com.ahmetgezici.githubrepos.utils.datautil.Resource

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SearchRepository()

    val loading :MutableLiveData<Boolean> = MutableLiveData()

    val repoData: MutableLiveData<List<Repo>> = MutableLiveData()

    // Get Repos

    fun getRepos(userName: String): MutableLiveData<Resource<List<Repo>>> {

        return repository.getRepos(userName)
    }

}