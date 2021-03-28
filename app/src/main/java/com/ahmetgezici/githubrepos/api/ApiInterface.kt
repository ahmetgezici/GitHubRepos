package com.ahmetgezici.githubrepos.api

import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.model.UserData
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("users/{user}")
    fun getUserData(@Path("user") userName: String): Single<UserData>

    @GET("users/{user}/repos")
    fun getRepos(@Path("user") user: String): Single<List<Repo>>

    @GET("/repos/{user}/{repoName}")
    fun getRepoData(
        @Path("user") user: String,
        @Path("repoName") repoName: String
    ): Single<Repo>

}