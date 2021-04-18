package com.ibeybeh.bfaasubmission2.presentation.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.bumptech.glide.Glide
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.R.string
import com.ibeybeh.bfaasubmission2.data.model.ProfileUserItem
import com.ibeybeh.bfaasubmission2.db.DatabaseContract
import com.ibeybeh.bfaasubmission2.db.UserHelper
import com.ibeybeh.bfaasubmission2.db.entity.User
import com.ibeybeh.bfaasubmission2.db.helper.MappingHelper
import com.ibeybeh.bfaasubmission2.presentation.detail.adapter.SectionsPagerAdapter
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.DetailViewModelInterface
import com.ibeybeh.bfaasubmission2.presentation.viewmodel.MainViewModel
import com.ibeybeh.bfaasubmission2.utils.Const.EXTRA_USER
import com.ibeybeh.bfaasubmission2.utils.Const.ID_KEYS
import com.ibeybeh.bfaasubmission2.utils.Const.USERNAME_KEYS
import com.kennyc.view.MultiStateView.ViewState.CONTENT
import com.kennyc.view.MultiStateView.ViewState.EMPTY
import com.kennyc.view.MultiStateView.ViewState.ERROR
import kotlinx.android.synthetic.main.activity_detail.detailToolbar
import kotlinx.android.synthetic.main.activity_detail.imgUserDetail
import kotlinx.android.synthetic.main.activity_detail.msvDetail
import kotlinx.android.synthetic.main.activity_detail.tabLayoutDetail
import kotlinx.android.synthetic.main.activity_detail.tvBioUserDetail
import kotlinx.android.synthetic.main.activity_detail.tvCompanyUserDetail
import kotlinx.android.synthetic.main.activity_detail.tvFollowerUserDetail
import kotlinx.android.synthetic.main.activity_detail.tvFollowingUserDetail
import kotlinx.android.synthetic.main.activity_detail.tvNameUserDetail
import kotlinx.android.synthetic.main.activity_detail.tvRepositoryUserDetail
import kotlinx.android.synthetic.main.activity_detail.viewPagerDetail
import kotlinx.android.synthetic.main.layout_error_view.btnError
import kotlinx.android.synthetic.main.layout_error_view.tvMessageError
import kotlinx.android.synthetic.main.layout_error_view.tvTitleError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var userHelper: UserHelper
    private var user: User? = null
    private var id = 0
    private var dataDummyUser: ProfileUserItem? = null

    private var isCheckedState = false

    companion object {

        fun start(
            context: Context,
            username: String?,
            id: Int?
        ) {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(USERNAME_KEYS, username)
                putExtra(ID_KEYS, id)
            }
            context.startActivity(intent)
        }
    }

    private val mTabSectionsAdapter: SectionsPagerAdapter by lazy {
        SectionsPagerAdapter(
            applicationContext,
            "",
            supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(detailToolbar)

        if (supportActionBar != null) {
            supportActionBar?.title = resources.getString(R.string.title_detail_user)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewPagerDetail.adapter = mTabSectionsAdapter
        tabLayoutDetail.setupWithViewPager(viewPagerDetail)

        val username = intent.getStringExtra(USERNAME_KEYS) as String
        id = intent.getIntExtra(ID_KEYS, 0)

        mTabSectionsAdapter.setDataUsername(username)

        mainViewModel = ViewModelProvider(this, NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getDetailUser(
            object : DetailViewModelInterface {
                override fun onSuccess() {
                    msvDetail.viewState = CONTENT
                }

                override fun onFailure(message: String, username: String) {
                    msvDetail.viewState = ERROR

                    tvTitleError.text = resources.getString(R.string.label_error)
                    tvMessageError.text = message
                    btnError.setOnClickListener {
                        mainViewModel.setDetailUser(username)
                    }
                }
            }
        ).observe(this, getDetailUser)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            val defferedUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryById(id.toString())
                MappingHelper.mapCursorToObject(cursor)
            }
            val userData = defferedUser.await()
            Log.d("NGECEK KELAS", userData.toString())
            if (userData.id != 0 && userData.login != null) {

                user = userData
                msvDetail.viewState = CONTENT
                initView()
                isCheckedState = true

                Log.d("BERHASIL DITEMUKAN", userData.toString())
            } else {
                Log.d("Data KOSONG", "$id ini tidak ada")

                isCheckedState = false

                mainViewModel.setDetailUser(username)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val getDetailUser: Observer<ProfileUserItem> = Observer {
        dataDummyUser = it
        if (it != null) {
            Glide.with(applicationContext)
                .load(it.avatarUrl)
                .into(imgUserDetail)

            tvNameUserDetail.text = it.name
            if (it.company != null) {
                tvCompanyUserDetail.text = it.company
            } else {
                tvCompanyUserDetail.text = resources.getString(R.string.text_user_no_company)
            }
            tvRepositoryUserDetail.text = "${resources.getString(R.string.text_total_repository)} ${it.publicRepos}"
            if (it.bio != null) {
                tvBioUserDetail.text = it.bio
            } else {
                tvBioUserDetail.text = resources.getString(R.string.label_tidak_ada_bio)
            }
            tvFollowingUserDetail.text = "${resources.getString(R.string.label_following)} ${it.following}"
            tvFollowerUserDetail.text = "${resources.getString(R.string.label_follower)} ${it.followers}"
        } else {
            msvDetail.viewState = EMPTY
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_item_menu, menu)

        if(userHelper.isUserFavorite(id)) {
            menu?.getItem(0)?.setIcon(R.drawable.ic_baseline_favorite_24)
        }else{
            menu?.getItem(0)?.setIcon(R.drawable.ic_baseline_favorite_border_24)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.menu_button_favorite -> {
                if (!isCheckedState) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_USER, user)

                    val values = ContentValues()
                    values.put(DatabaseContract.UserColumns.ID, dataDummyUser?.id)
                    values.put(DatabaseContract.UserColumns.NAME, dataDummyUser?.name ?: "")
                    values.put(DatabaseContract.UserColumns.LOGIN, dataDummyUser?.login)
                    values.put(DatabaseContract.UserColumns.TWITTER_USERNAME, dataDummyUser?.twitterUsername ?: "")
                    values.put(DatabaseContract.UserColumns.EMAIL, dataDummyUser?.email ?: "")
                    values.put(DatabaseContract.UserColumns.AVATAR, dataDummyUser?.avatarUrl ?: "")
                    values.put(DatabaseContract.UserColumns.BIO, dataDummyUser?.bio ?: "")
                    values.put(DatabaseContract.UserColumns.COMPANY, dataDummyUser?.company ?: "")
                    values.put(DatabaseContract.UserColumns.FOLLOWER, dataDummyUser?.followers ?: 0)
                    values.put(DatabaseContract.UserColumns.FOLLOWING, dataDummyUser?.following ?: 0)
                    values.put(DatabaseContract.UserColumns.LOCATION, dataDummyUser?.location ?: "")
                    values.put(DatabaseContract.UserColumns.PUBLIC_REPO, dataDummyUser?.publicRepos ?: 0)

                    val result = userHelper.insert(values)

                    if (result > 0) {
                        user?.id = result.toInt()
                        Toast.makeText(this@DetailActivity, getString(string.label_item_berhasil_ditambah), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DetailActivity, getString(string.label_gagal_menambah_data), Toast.LENGTH_SHORT).show()
                    }

                    item.setIcon(R.drawable.ic_baseline_favorite_24)
                    isCheckedState = true
                } else {
                    userHelper.open()
                    showAlertDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        Glide.with(applicationContext)
            .load(user?.avatarUrl)
            .into(imgUserDetail)

        tvNameUserDetail.text = user?.name
        if (user?.company != null) {
            tvCompanyUserDetail.text = user?.company
        } else {
            tvCompanyUserDetail.text = resources.getString(string.text_user_no_company)
        }
        tvRepositoryUserDetail.text = "${resources.getString(string.text_total_repository)} ${user?.publicRepos}"
        if (user?.bio != null) {
            tvBioUserDetail.text = user?.bio
        } else {
            tvBioUserDetail.text = resources.getString(string.label_tidak_ada_bio)
        }
        tvFollowingUserDetail.text = "${resources.getString(string.label_following)} ${user?.following}"
        tvFollowerUserDetail.text = "${resources.getString(string.label_follower)} ${user?.followers}"
    }

    private fun showAlertDialog() {
        val dialogTitle = resources.getString(string.title_hapus_data)
        val dialogMessage = resources.getString(string.message_hapus_data)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(string.action_hapus)) { _, _ ->
                val result = userHelper.deleteById(user?.id.toString()).toLong()
                isCheckedState = false
                if (result > 0) {
                    Log.d("DIHAPUS", "Berhasil Di Hapus")
                    val intent = Intent()
                    userHelper.close()
                    finish()
                }
            }
            .setNegativeButton(getString(string.action_tidak)) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}