package com.sudoajay.firebase_chat.ui.model

data class Message (
    var id:String? = null,
    var fullName:String? = null,
    var message:String? = null

){
    companion object{
        const val ITEM_SENT = 1
        const val ITEM_RECEIVE = 2
    }
}