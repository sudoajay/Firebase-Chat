package com.sudoajay.firebase_chat.activity.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.sudoajay.firebase_chat.ui.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor() : ViewModel() {

    var TAG = "FriendsViewModelTAG"
    private lateinit var databaseReference: DatabaseReference
    var hideProgress: MutableLiveData<Boolean> = MutableLiveData()
    var userList:MutableStateFlow<MutableList<User>> = MutableStateFlow(mutableListOf())


    init {
        loadHideProgress()
        databaseReference = FirebaseDatabase.getInstance().reference

        readFromDataBase()

    }

    private fun readFromDataBase(){
        databaseReference.child("user").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val list = mutableListOf<User>()
               for (postSnapshot in snapshot.children){
                   val currentUser = postSnapshot.getValue(User::class.java)
                   list.add(currentUser?:User(null,null,null))
               }
                Log.e(TAG , "Calling ")
                hideProgress.value = true
                userList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    private fun loadHideProgress() {
        hideProgress.value = false
    }


}