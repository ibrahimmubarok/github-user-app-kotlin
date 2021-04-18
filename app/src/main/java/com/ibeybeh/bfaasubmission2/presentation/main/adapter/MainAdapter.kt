package com.ibeybeh.bfaasubmission2.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.data.model.DataUserItem
import com.ibeybeh.bfaasubmission2.db.entity.User
import kotlinx.android.synthetic.main.item_user.view.imgUserItem
import kotlinx.android.synthetic.main.item_user.view.tvNameItem

class MainAdapter(
    private val data: MutableList<DataUserItem>,
    private val listener: ((DataUserItem) -> Unit)? = null
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    fun addItem(user: User) {
        this.data.add(DataUserItem(
            user.avatarUrl,
            null,
            null,
            null,
            user.id,
            user.login
        ))
        notifyItemInserted(this.data.size - 1)
    }

    fun addData(items: List<User>) {
        this.data.clear()
        for (i in items.indices) {
            this.data.add(
                DataUserItem(
                    items[i].avatarUrl,
                    null,
                    null,
                    null,
                    items[i].id,
                    items[i].login
                )
            )
        }
        notifyDataSetChanged()
    }

    fun setData(items: List<DataUserItem>) {
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

        fun bind(dataUser: DataUserItem) {
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