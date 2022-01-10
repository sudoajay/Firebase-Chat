package com.sudoajay.firebase_chat.model

data class User(
    var fullName:String? = null,
    var email:String? = null,
    var uid:String? = null
) {
    constructor() : this("","",
        ""
    )
}


