package com.ibeybeh.myconsumerapp.presentation.main

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ibeybeh.myconsumerapp.R
import com.ibeybeh.myconsumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ibeybeh.myconsumerapp.db.entity.User
import com.ibeybeh.myconsumerapp.db.helper.MappingHelper
import com.ibeybeh.myconsumerapp.presentation.main.adapter.MainAdapter
import com.ibeybeh.myconsumerapp.utils.Const.EXTRA_STATE
import kotlinx.android.synthetic.main.activity_main.pbMainFavorite
import kotlinx.android.synthetic.main.activity_main.rvMainFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(
            mutableListOf()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
                Log.d("ALVI", "FREDO")
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUserAsync()
            Log.d("CEK", "TESTT")
        }else{
            savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)?.also {
                mainAdapter.setData(it)
            }
        }
    }

    private fun loadUserAsync() {
        CoroutineScope(Dispatchers.Main).launch{
            val defferedUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            rvMainFavorite.visibility = View.VISIBLE
            pbMainFavorite.visibility = View.GONE
            val user = defferedUser.await()
            if (user.size > 0) {
                for (i in 0 until user.size) {
                    mainAdapter.setData(user)
                }
                Log.d("TOTAL DATA", user.size.toString())
            } else {
                mainAdapter.setData(listOf())
                showSnackbarMessage(getString(R.string.label_empty_data))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, mainAdapter.listUser)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rvMainFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        rvMainFavorite.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}