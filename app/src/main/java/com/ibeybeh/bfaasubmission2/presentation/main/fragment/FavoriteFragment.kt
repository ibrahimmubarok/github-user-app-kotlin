package com.ibeybeh.bfaasubmission2.presentation.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.R.string
import com.ibeybeh.bfaasubmission2.db.UserHelper
import com.ibeybeh.bfaasubmission2.db.entity.User
import com.ibeybeh.bfaasubmission2.db.helper.MappingHelper
import com.ibeybeh.bfaasubmission2.presentation.detail.DetailActivity
import com.ibeybeh.bfaasubmission2.presentation.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.fragment_favorite.pbFavorite
import kotlinx.android.synthetic.main.fragment_favorite.rvFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(
            mutableListOf(),
            listener = {
                it.id?.let { id -> DetailActivity.start(requireContext(), it.login.toString(), id) }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        loadUserAsync()
    }

    private fun initRecyclerView() {
        rvFavorite.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadUserAsync() {
        CoroutineScope(Dispatchers.Main).launch {
            val userHelper = UserHelper.getInstance(requireContext())
            userHelper.open()
            val defferedUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            rvFavorite.visibility = View.VISIBLE
            pbFavorite.visibility = View.GONE
            val user = defferedUser.await()
            if (user.size > 0) {
                for (i in 0 until user.size) {
                    mainAdapter.addItem(
                        User(
                            user[i].id,
                            user[i].name,
                            user[i].login,
                            user[i].twitterUsername,
                            user[i].email,
                            user[i].avatarUrl,
                            user[i].bio,
                            user[i].company,
                            user[i].followers,
                            user[i].following,
                            user[i].location,
                            user[i].publicRepos
                        )
                    )
                }
                Log.d("TOTAL DATA", user.size.toString())
            }else {
                mainAdapter.setData(listOf())
                showSnackbarMessage(getString(string.label_empty_data))
            }
            userHelper.close()
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
}