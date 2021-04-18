package com.ibeybeh.bfaasubmission2.presentation.viewmodel

interface SearchViewModelInterface {

    fun onSuccess()
    fun onFailure(message: String, query: String)
}

interface DetailViewModelInterface {

    fun onSuccess()
    fun onFailure(message: String, username: String)
}

interface FollowerViewModelInterface {

    fun onSuccess()
    fun onFailure(message: String, username: String)
}

interface FollowingViewModelInterface {

    fun onSuccess()
    fun onFailure(message: String, username: String)
}