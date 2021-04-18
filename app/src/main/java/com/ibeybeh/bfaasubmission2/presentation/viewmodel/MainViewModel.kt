package com.ibeybeh.bfaasubmission2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibeybeh.bfaasubmission2.data.model.DataUserItem
import com.ibeybeh.bfaasubmission2.data.model.ProfileUserItem
import com.ibeybeh.bfaasubmission2.data.model.UserItem
import com.ibeybeh.bfaasubmission2.data.remote.GithubApiClient
import com.ibeybeh.bfaasubmission2.data.remote.ServiceBuilder
import com.ibeybeh.bfaasubmission2.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private var mListenerSearch: SearchViewModelInterface? = null
    private var mListenerDetail: DetailViewModelInterface? = null
    private var mListenerFollowing: FollowingViewModelInterface? = null
    private var mListenerFollower: FollowerViewModelInterface? = null

    private val listUser = MutableLiveData<List<DataUserItem>>()
    private val listDetail = MutableLiveData<ProfileUserItem>()
    private val listFollowing = MutableLiveData<List<DataUserItem>>()
    private val listFollower = MutableLiveData<List<DataUserItem>>()

    private val request = ServiceBuilder.buildService(GithubApiClient::class.java)

    fun setSearchUser(query: String) {
        val call = request.getSearchUser(Const.TOKEN, query)

        call.enqueue(object : Callback<UserItem> {
            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                if (response.isSuccessful) {
                    mListenerSearch?.onSuccess()
                    listUser.postValue(response.body()!!.dataUserItems)
                } else {
                    mListenerSearch?.onFailure(response.errorBody().toString(), query)
                }
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                mListenerSearch?.onFailure(t.message.toString(), query)
            }
        })
    }

    fun setDetailUser(username: String) {
        val call = request.getDetailUser(Const.TOKEN, username)

        call.enqueue(object : Callback<ProfileUserItem> {
            override fun onResponse(call: Call<ProfileUserItem>, response: Response<ProfileUserItem>) {
                if (response.isSuccessful) {
                    mListenerDetail?.onSuccess()
                    listDetail.postValue(response.body())
                } else {
                    mListenerDetail?.onFailure(response.errorBody().toString(), username)
                }
            }

            override fun onFailure(call: Call<ProfileUserItem>, t: Throwable) {
                mListenerDetail?.onFailure(t.message.toString(), username)
            }
        })
    }

    fun setFollowingUser(username: String) {
        val call = request.getFollowingUser(Const.TOKEN, username)

        call.enqueue(object : Callback<List<DataUserItem>> {
            override fun onResponse(call: Call<List<DataUserItem>>, response: Response<List<DataUserItem>>) {
                if (response.isSuccessful) {
                    mListenerFollowing?.onSuccess()
                    listFollowing.postValue(response.body())
                } else {
                    mListenerFollowing?.onFailure(response.errorBody().toString(), username)
                }
            }

            override fun onFailure(call: Call<List<DataUserItem>>, t: Throwable) {
                mListenerFollowing?.onFailure(t.message.toString(), username)
            }
        })
    }

    fun setFollowerUser(username: String) {
        val call = request.getFollowerUser(Const.TOKEN, username)

        call.enqueue(object : Callback<List<DataUserItem>> {
            override fun onResponse(call: Call<List<DataUserItem>>, response: Response<List<DataUserItem>>) {
                if (response.isSuccessful) {
                    mListenerFollower?.onSuccess()
                    listFollower.postValue(response.body())
                } else {
                    mListenerFollower?.onFailure(response.errorBody().toString(), username)
                }
            }

            override fun onFailure(call: Call<List<DataUserItem>>, t: Throwable) {
                mListenerFollower?.onFailure(t.message.toString(), username)
            }
        })
    }

    fun getSearchUser(
        listenerSearch: SearchViewModelInterface? = null
    ): LiveData<List<DataUserItem>> {
        this.mListenerSearch = listenerSearch
        return listUser
    }

    fun getDetailUser(
        listenerDetail: DetailViewModelInterface? = null
    ): LiveData<ProfileUserItem> {
        this.mListenerDetail = listenerDetail
        return listDetail
    }

    fun getFollowingUser(
        listenerFollowing: FollowingViewModelInterface? = null
    ): LiveData<List<DataUserItem>> {
        this.mListenerFollowing = listenerFollowing
        return listFollowing
    }

    fun getFollowerUser(
        listenerFollower: FollowerViewModelInterface? = null
    ): LiveData<List<DataUserItem>> {
        this.mListenerFollower = listenerFollower
        return listFollower
    }
}