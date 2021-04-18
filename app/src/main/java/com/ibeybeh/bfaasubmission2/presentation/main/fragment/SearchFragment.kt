package com.ibeybeh.bfaasubmission2.presentation.main.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.data.model.DataUserItem
import com.ibeybeh.bfaasubmission2.presentation.detail.DetailActivity
import com.ibeybeh.bfaasubmission2.presentation.main.adapter.MainAdapter
import com.ibeybeh.bfaasubmission2.presentation.settings.SettingsActivity
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.MainViewModel
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.SearchViewModelInterface
import com.kennyc.view.MultiStateView.ViewState.CONTENT
import com.kennyc.view.MultiStateView.ViewState.EMPTY
import com.kennyc.view.MultiStateView.ViewState.ERROR
import com.kennyc.view.MultiStateView.ViewState.LOADING
import kotlinx.android.synthetic.main.fragment_search.msvMain
import kotlinx.android.synthetic.main.fragment_search.rvMain
import kotlinx.android.synthetic.main.layout_error_view.btnError
import kotlinx.android.synthetic.main.layout_error_view.tvMessageError
import kotlinx.android.synthetic.main.layout_error_view.tvTitleError
import kotlinx.android.synthetic.main.layout_search_toolbar.edtInputSearch
import kotlinx.android.synthetic.main.layout_search_toolbar.search_toolbar

class SearchFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private val mainAdapter: MainAdapter by lazy {
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(search_toolbar)
        }

        initRecyclerView()

        mainViewModel = ViewModelProvider(this, NewInstanceFactory()).get(MainViewModel::class.java)

        edtInputSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    msvMain.viewState = EMPTY
                    return false
                } else {
                    msvMain.viewState = LOADING
                    mainViewModel.setSearchUser(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    msvMain.viewState = EMPTY
                    return false
                } else {
                    msvMain.viewState = LOADING
                    mainViewModel.setSearchUser(query)
                }
                return false
            }
        })

        mainViewModel.getSearchUser(
            object : SearchViewModelInterface {
                override fun onSuccess() {
                    msvMain.viewState = CONTENT
                }

                override fun onFailure(message: String, query: String) {
                    msvMain.viewState = ERROR

                    Toast.makeText(context, resources.getString(R.string.label_terjadi_kesalahan), Toast.LENGTH_SHORT).show()

                    tvTitleError.text = resources.getString(R.string.label_error)
                    tvMessageError.text = message
                    btnError.setOnClickListener {
                        mainViewModel.setSearchUser(query)
                    }
                }
            }
        ).observe(viewLifecycleOwner, getSearchUser)
    }

    private fun initRecyclerView() {
        rvMain.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
    }

    private val getSearchUser: Observer<List<DataUserItem>> = Observer<List<DataUserItem>> {
        if (it != null) {
            mainAdapter.setData(it)
        } else {
            msvMain.viewState = EMPTY
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu_setting -> {
                SettingsActivity.start(requireContext())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}