package com.sudoajay.firebase_chat.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.databinding.LayoutForReceiverBinding
import com.sudoajay.firebase_chat.databinding.LayoutForSenderBinding
import com.sudoajay.firebase_chat.ui.model.Message
import javax.inject.Inject

class MessageAdapter @Inject constructor(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messageList: MutableList<Message> = mutableListOf()


    class SentViewHolder(layoutForSenderBinding: LayoutForSenderBinding) :
        RecyclerView.ViewHolder(layoutForSenderBinding.root) {

        var message = layoutForSenderBinding.senderTextTextView
    }


    class ReceiverViewHolder(layoutForReceiverBinding: LayoutForReceiverBinding) :
        RecyclerView.ViewHolder(layoutForReceiverBinding.root) {

        var fullName = layoutForReceiverBinding.reciverFullNameTextView
        var message = layoutForReceiverBinding.receiverTextTextView

    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(message.id))
            Message.ITEM_RECEIVE
        else
            Message.ITEM_SENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding: LayoutForSenderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_for_sender, parent, false
            )
            SentViewHolder(binding)
        } else {
            val binding: LayoutForReceiverBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.layout_for_receiver, parent, false
            )
            ReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            viewHolder.message.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.message.text = currentMessage.message
            viewHolder.fullName.text = currentMessage.fullName
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


}
