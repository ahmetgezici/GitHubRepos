package com.ahmetgezici.githubrepos.view.DetailsFragment

import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.model.UserData
import com.ahmetgezici.githubrepos.utils.datautil.Resource
import io.reactivex.rxjava3.functions.BiFunction

class DetailsFragmentPageCombiner :
    BiFunction<Resource<UserData>, Resource<Repo>, DetailsFragmentViewState> {
    override fun apply(t1: Resource<UserData>?, t2: Resource<Repo>?): DetailsFragmentViewState {
        return DetailsFragmentViewState(t1, t2)
    }
}