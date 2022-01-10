package com.sudoajay.firebase_chat.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.sudoajay.firebase_chat.R
import com.sudoajay.firebase_chat.activity.bottomSheet.NavigationDrawerBottomSheet
import com.sudoajay.firebase_chat.activity.sendFeedback.SendFeedback
import com.sudoajay.firebase_chat.activity.viewModel.FriendsViewModel
import com.sudoajay.firebase_chat.databinding.ActivityFriendsBinding
import com.sudoajay.firebase_chat.helper.InsetDivider
import com.sudoajay.firebase_chat.helper.Toaster
import com.sudoajay.firebase_chat.ui.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FriendsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendsBinding
    private var userItems: List<User> = listOf()
    private var doubleBackToExitPressedOnce = false
    private lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var navigationDrawerBottomSheet: NavigationDrawerBottomSheet
    @Inject
    lateinit var adapter :UserAdapter

    val viewModel: FriendsViewModel by viewModels()
    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isDarkTheme = BaseActivity.isSystemDefaultOn(resources)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkTheme) {
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    true
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_friends)

        binding.viewmodel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this

        setSupportActionBar(binding.bottomAppBar)


    }

    override fun onResume() {
        setReference()
        super.onResume()
    }

    private fun setReference() {
        mAuth = FirebaseAuth.getInstance()

        //      Setup Swipe Refresh
        binding.swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        binding.swipeRefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(applicationContext, R.color.swipeBgColor)
        )

        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }

        //         Setup BottomAppBar Navigation Setup
        binding.bottomAppBar.navigationIcon?.mutate()?.let {
            it.setTint(
                ContextCompat.getColor(applicationContext, R.color.colorAccent)
            )
            binding.bottomAppBar.navigationIcon = it
        }


        setRecyclerView()

    }
    private fun setRecyclerView() {
        val recyclerView = binding.userItemRecyclerView
        val divider = getInsetDivider()
        recyclerView.addItemDecoration(divider)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.userList.collectLatest {
                adapter.userItems= it
                adapter.notifyItemChanged(0,it.size)
            }
        }

    }

    private fun getInsetDivider(): RecyclerView.ItemDecoration {
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.divider_height)
        val dividerColor = ContextCompat.getColor(applicationContext, R.color.divider)
        val marginLeft = resources.getDimensionPixelSize(R.dimen.divider_inset)
        return InsetDivider.Builder(this)
            .orientation(InsetDivider.VERTICAL_LIST)
            .dividerHeight(dividerHeight)
            .color(dividerColor)
            .insets(marginLeft, 0)
            .build()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_toolbar_menu, menu)
        val actionSearch = menu.findItem(R.id.search_optionMenu)
        manageSearch(actionSearch)

        return super.onCreateOptionsMenu(menu)
    }

    private fun manageSearch(searchItem: MenuItem) {
        val searchView =
            searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        manageFabOnSearchItemStatus(searchItem)
        manageInputTextInSearchView(searchView)
    }

    private fun manageFabOnSearchItemStatus(searchItem: MenuItem) {
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                binding.darkModeFloatingActionButton.hide()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                binding.darkModeFloatingActionButton.show()
                return true
            }
        })
    }

    private fun manageInputTextInSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                refreshData()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> showNavigationDrawer()
            R.id.logOut_optionMenu->{
                mAuth.signOut()
                finish()
                startActivity(
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                )
            }
            R.id.refresh_optionMenu -> refreshData()
            R.id.sendFeedBack_optionMenu -> {
                startActivity(
                    Intent(
                        applicationContext,
                        SendFeedback::class.java
                    )
                )
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun showNavigationDrawer() {
        navigationDrawerBottomSheet.show(
            supportFragmentManager.beginTransaction(),
            navigationDrawerBottomSheet.tag
        )
    }

    private fun refreshData(){

    }

    override fun onBackPressed() {
        onBack()
    }

    private fun onBack() {
        if (doubleBackToExitPressedOnce) {
            closeApp()
            return
        }
        doubleBackToExitPressedOnce = true
        Toaster.showToast(applicationContext, getString(R.string.click_back_text))
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000L)
            doubleBackToExitPressedOnce = false
        }
    }

    private fun closeApp() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
    }
}