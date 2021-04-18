package com.ibeybeh.myconsumerapp.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibeybeh.myconsumerapp.R
import com.ibeybeh.myconsumerapp.db.entity.User
import kotlinx.android.synthetic.main.item_user.view.imgUserItem
import kotlinx.android.synthetic.main.item_user.view.tvNameItem

class MainAdapter(
    private val data: MutableList<User>,
    private val listener: ((User) -> Unit)? = null
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var listUser = ArrayList<User>()
        set(listUser) {
            this.listUser.clear()
            this.listUser.addAll(listUser)
            notifyDataSetChanged()
        }

    fun setData(items: List<User>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dataUser: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(dataUser.avatarUrl)
                    .into(imgUserItem)

                tvNameItem.text = dataUser.login

                itemView.setOnClickListener {
                    listener?.invoke(dataUser)
                }
            }
        }
    }
}