package com.ibeybeh.bfaasubmission2.presentation.detail.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.presentation.detail.fragment.FollowFragment

class SectionsPagerAdapter(
    private val mContext: Context,
    private var username: String,
    fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    fun setDataUsername(username: String) {
        this.username = username
    }

    private val tabTitles = intArrayOf(
        R.string.label_following,
        R.string.label_follower
    )

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return FollowFragment.newInstance(position+1, username)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }
}