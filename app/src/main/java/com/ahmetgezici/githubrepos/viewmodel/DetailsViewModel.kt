package com.ahmetgezici.githubrepos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetgezici.githubrepos.repository.DetailsRepository
import com.ahmetgezici.githubrepos.view.DetailsFragment.DetailsFragmentViewState

class DetailsViewModel : ViewModel() {

    private val detailsRepository = DetailsRepository()

    ////////////////////////////////////////////////////////////////////////////////////////////////

    val loading = MutableLiveData<Boolean>()

    val imageLoading = MutableLiveData<Boolean>()

    // Get User Data && Repo Data

    fun getUserAndRepo(userName: String, repoName: String) : MutableLiveData<DetailsFragmentViewState> {

        return detailsRepository.getUserAndRepo(userName,repoName)
    }

}