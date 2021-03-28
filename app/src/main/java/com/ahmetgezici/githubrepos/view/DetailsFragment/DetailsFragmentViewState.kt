package com.ahmetgezici.githubrepos.view.DetailsFragment

import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.model.UserData
import com.ahmetgezici.githubrepos.utils.datautil.Resource

class DetailsFragmentViewState(val userResource: Resource<UserData>?, val repoResource: Resource<Repo>?)