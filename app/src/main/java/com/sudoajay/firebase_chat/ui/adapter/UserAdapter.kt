package com.sudoajay.firebase_chat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.databinding.LayoutUserBinding
import com.sudoajay.firebase_chat.model.User

class UserAdapter(val context: Context) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    var userItems: List<User> = listOf()

    class MyViewHolder(layoutUserBinding: LayoutUserBinding) :
        RecyclerView.ViewHolder(layoutUserBinding.root) {
        var fullName = layoutUserBinding.fullNameTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LayoutUserBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_user, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userItems[position]
        holder.fullName.text = currentUser.fullName
    }

}
