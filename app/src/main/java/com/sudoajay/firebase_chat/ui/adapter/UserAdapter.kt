package com.sudoajay.firebase_chat.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.activity.ChatActivity
import com.sudoajay.firebase_chat.databinding.LayoutUserBinding
import com.sudoajay.firebase_chat.ui.model.User
import javax.inject.Inject

class UserAdapter @Inject constructor(val context: Context) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    var userItems: MutableList<User> = mutableListOf()

    class MyViewHolder(layoutUserBinding: LayoutUserBinding) :
        RecyclerView.ViewHolder(layoutUserBinding.root) {

        var fullName = layoutUserBinding.fullNameTextView
        var userConstraintLayout = layoutUserBinding.userConstraintLayout
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LayoutUserBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_user, parent, false
        )
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userItems[position]

        holder.fullName.text = currentUser.fullName

        holder.userConstraintLayout.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",currentUser.fullName)
            intent.putExtra("uid",currentUser.uid)
            intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userItems.size
    }

}
