package com.sudoajay.firebase_chat.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.databinding.ActivityChatBinding
import com.sudoajay.firebase_chat.ui.adapter.MessageAdapter
import com.sudoajay.firebase_chat.ui.model.Message
import com.sudoajay.firebase_chat.ui.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var receiverUser: User = User("", "", "")
    private var senderUser = User("", "", "")

    private var senderRoom: String? = ""
    private var receiverRoom: String? = ""

    var userList: MutableStateFlow<MutableList<Message>> = MutableStateFlow(mutableListOf())
    var hideProgress: MutableLiveData<Boolean> = MutableLiveData()

    var TAG = "ChatActivityTAG"

    @Inject
    lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)


        binding.activity = this
        binding.lifecycleOwner = this

        val fullName = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        mAuth = FirebaseAuth.getInstance()
        val senderUid = mAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference

        receiverUser = User(fullName, "", receiverUid)

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = fullName

        hideProgress.value = false

    }

    override fun onResume() {
        setReference()
        super.onResume()

        databaseReference.child("user").orderByChild("uid").equalTo(mAuth.currentUser?.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (singleSnapshot in dataSnapshot.children) {
                        val user = singleSnapshot.getValue(User::class.java)
                        senderUser = User(user!!.fullName, user.email, user.uid)
                    }
                    Log.e(TAG, "passed")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException())
                }
            })

        Log.e(
            TAG,
            "User - ${senderUser.fullName} + ${senderUser.email} ${mAuth.currentUser?.uid.toString()} "
        )
    }

    private fun setReference() {
        databaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        list.add(message ?: Message("", "", ""))
                    }

                    hideProgress.value = true
                    userList.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read value.", error.toException())
                }

            })
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val recyclerView = binding.chatRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            userList.collectLatest { messageList ->
                for (user in messageList) {
                    Log.i(TAG, "user ${user.fullName} user ${user.message}")
                }

                adapter.messageList = messageList
                withContext(Dispatchers.Main) {
                    adapter.notifyItemChanged(0, messageList.size)
                }
            }
        }
    }

    fun sendButtonOnClick() {
        val message = binding.enterMessageTextView.text.toString()


        val messageObjectSender = Message(receiverUser.uid, "", message)
        val messageObjectReceiver = Message(receiverUser.uid, senderUser.fullName, message)


        databaseReference.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObjectSender).addOnSuccessListener {
                databaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObjectReceiver)
            }
        binding.enterMessageTextView.text.clear()
    }
}