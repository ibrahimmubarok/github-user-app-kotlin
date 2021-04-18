package com.ibeybeh.bfaasubmission2.presentation.detail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.data.model.DataUserItem
import com.ibeybeh.bfaasubmission2.presentation.detail.DetailActivity
import com.ibeybeh.bfaasubmission2.presentation.main.adapter.MainAdapter
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.FollowerViewModelInterface
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.FollowingViewModelInterface
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.MainViewModel
import com.ibeybeh.bfaasubmission2.utils.Const.FOLLOWING_FRAGMENT
import com.ibeybeh.bfaasubmission2.utils.Const.SECTION_PAGER
import kotlinx.android.synthetic.main.fragment_following.pbFollow
import kotlinx.android.synthetic.main.fragment_following.rvFollowing

class FollowFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private val followingAdapter: MainAdapter by lazy {
        MainAdapter(
            mutableListOf(),
            listener = {
                DetailActivity.start(
                    requireContext(),
                    it.login,
                    it.id
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    companion object {

        fun newInstance(index: Int, username: String): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putString(FOLLOWING_FRAGMENT, username)
            bundle.putInt(SECTION_PAGER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this, NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getFollowingUser(
            object : FollowingViewModelInterface {
                override fun onSuccess() {
                    pbFollow.visibility = View.GONE
                }

                override fun onFailure(message: String, username: String) {
                    Toast.makeText(context, resources.getString(R.string.label_following_error), Toast.LENGTH_SHORT).show()
                }
            }
        ).observe(viewLifecycleOwner, getFollowingData)
        mainViewModel.getFollowerUser(
            object : FollowerViewModelInterface {
                override fun onSuccess() {
                    pbFollow.visibility = View.GONE
                }

                override fun onFailure(message: String, username: String) {
                    Toast.makeText(context, R.string.label_follower_error, Toast.LENGTH_SHORT).show()
                }
            }
        ).observe(viewLifecycleOwner, getFollowerData)

        var index = 1

        if (arguments != null) {
            val username = arguments?.getString(FOLLOWING_FRAGMENT, "")
            index = arguments?.getInt(SECTION_PAGER, 0) as Int
            if (username != null) {
                if (index == 1) {
                    mainViewModel.setFollowingUser(username)
                }else {
                    mainViewModel.setFollowerUser(username)
                }
            }
        }

        initRecyclerView()
    }

    private val getFollowingData: Observer<List<DataUserItem>> = Observer<List<DataUserItem>> {
        if (it != null) {
            followingAdapter.setData(it)
        }else{
            Toast.makeText(context, resources.getString(R.string.label_following_tidak_ada), Toast.LENGTH_SHORT).show()
        }
    }

    private val getFollowerData: Observer<List<DataUserItem>> = Observer<List<DataUserItem>> {
        if (it != null) {
            followingAdapter.setData(it)
        }else {
            Toast.makeText(context, R.string.label_follower_tidak_ada, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        rvFollowing.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = followingAdapter
        }
    }
}