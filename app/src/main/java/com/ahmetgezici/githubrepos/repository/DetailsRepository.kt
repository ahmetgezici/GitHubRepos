package com.ahmetgezici.githubrepos.repository

import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.githubrepos.api.ApiClient
import com.ahmetgezici.githubrepos.api.ApiInterface
import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.model.UserData
import com.ahmetgezici.githubrepos.utils.datautil.Resource
import com.ahmetgezici.githubrepos.view.DetailsFragment.DetailsFragmentPageCombiner
import com.ahmetgezici.githubrepos.view.DetailsFragment.DetailsFragmentViewState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsRepository {

    var apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUserAndRepo(
        userName: String,
        repoName: String
    ): MutableLiveData<DetailsFragmentViewState> {

        val liveData = MutableLiveData<DetailsFragmentViewState>()


        val userObservable = getUserData(userName)
        val repoObservable = getRepoData(userName, repoName)

        Observable.combineLatest(userObservable, repoObservable, DetailsFragmentPageCombiner())
            .subscribeOn(Schedulers.io())
            .subscribe(liveData::postValue)
            .isDisposed

        return liveData
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun getUserData(userName: String): Observable<Resource<UserData>> {

        return Observable.create { emiter ->

            emiter.onNext(Resource.loading())

            apiInterface.getUserData(userName)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { userDataRes ->
                        emiter.onNext(Resource.success(userDataRes))
                        emiter.onComplete()
                    },
                    { error ->
                        emiter.onNext(Resource.error(error.message.toString()))
                        emiter.onComplete()
                    }
                ).isDisposed
        }

    }

    ////////////////////

    private fun getRepoData(userName: String, repoName: String): Observable<Resource<Repo>> {

        return Observable.create { emitter ->

            emitter.onNext(Resource.loading())

            apiInterface.getRepoData(userName, repoName)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { repoDataRes ->
                        emitter.onNext(Resource.success(repoDataRes))
                        emitter.onComplete()
                    },
                    { error ->
                        emitter.onNext(Resource.error(error.message.toString()))
                        emitter.onComplete()
                    }
                ).isDisposed
        }

    }

}