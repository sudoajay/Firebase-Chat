package com.sudoajay.firebase_chat.di

import android.content.Context
import com.sudoajay.firebase_chat.ui.adapter.UserAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleProvides {


    @Singleton
    @Provides
    fun providesUserAdapter( @ApplicationContext appContext: Context): UserAdapter = UserAdapter(appContext)




}

