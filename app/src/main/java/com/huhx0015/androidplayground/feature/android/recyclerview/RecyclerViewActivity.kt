package com.huhx0015.androidplayground.feature.android.recyclerview

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huhx0015.androidplayground.R
import com.huhx0015.androidplayground.databinding.ActivityRecyclerViewBinding
import com.huhx0015.androidplayground.model.DataItem
import kotlinx.coroutines.launch

class RecyclerViewActivity : AppCompatActivity() {

    private val viewModel: RecyclerViewViewModel by viewModels()

    private lateinit var binding: ActivityRecyclerViewBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerViewListAdapter: RecyclerViewListAdapter

    companion object {
        private const val KEY_INSTANCE_LIST_POSITION = "list_position"
        private const val VAL_LIST_FIRST_POSITION = 0
    }

    // onCreate(): Initializes the screen, starts observers, and triggers initial RecyclerView setup.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observe()
        viewModel.sendIntent(RecyclerViewIntent.InitRecyclerViewIntent)
    }

    // onSaveInstanceState(): Persists the first visible RecyclerView item position across recreation.
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_INSTANCE_LIST_POSITION, linearLayoutManager.findFirstVisibleItemPosition())
        super.onSaveInstanceState(outState)
    }

    // onRestoreInstanceState(): Restores and applies the saved RecyclerView scroll position.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val listPosition = savedInstanceState.getInt(KEY_INSTANCE_LIST_POSITION)
        updateRecyclerViewPosition(position = listPosition)
    }

    // onOptionsItemSelected(): Handles toolbar item actions and closes the screen when home is pressed.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // observe(): Collects state and event flows while the activity is in the started lifecycle state.
    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        onStateChanged(state)
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        onEvent(event)
                    }
                }
            }
        }
    }

    // onStateChanged(): Applies the latest state by setting adapter, data, and toolbar title.
    private fun onStateChanged(state: RecyclerViewState) {
        updateRecyclerViewAdapter(adapterType = state.adapterType)
        updateRecyclerViewData(adapterType = state.adapterType, dataList = state.dataList)
        updateToolbarTitle(adapterType = state.adapterType)
    }

    // onEvent(): Handles one-time UI events such as scrolling to the top after a refresh.
    private fun onEvent(event: RecyclerViewEvent) {
        when (event) {
            RecyclerViewEvent.RecyclerViewRefreshEvent -> {
                updateRecyclerViewPosition(position = VAL_LIST_FIRST_POSITION)
            }
        }
    }

    // initView(): Sets up all initial UI components for the RecyclerView screen.
    private fun initView() {
        initScreen()
        initToolbar()
        initButtons()
        initRecyclerView()
    }

    // initScreen(): Inflates the view binding, sets the content view, and applies system bar insets.
    private fun initScreen() {
        enableEdgeToEdge()
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // initToolbar(): Configures the toolbar title, color, and back navigation behavior.
    private fun initToolbar() {
        binding.recyclerViewToolbar.title = getString(R.string.recycler_view)
        binding.recyclerViewToolbar.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(binding.recyclerViewToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // initButtons(): Registers click listeners to switch between RecyclerView adapter implementations.
    private fun initButtons() {
        binding.recyclerViewButton.setOnClickListener {
            viewModel.sendIntent(
                RecyclerViewIntent.RecyclerViewButtonClickIntent(
                    adapterType = RecyclerViewAdapterType.RECYCLER_VIEW
                )
            )
        }
        binding.listAdapterButton.setOnClickListener {
            viewModel.sendIntent(
                RecyclerViewIntent.RecyclerViewButtonClickIntent(
                    adapterType = RecyclerViewAdapterType.LIST_ADAPTER
                )
            )
        }
    }

    // initRecyclerView(): Creates and attaches the selected adapter type to the RecyclerView.
    private fun initRecyclerView() {
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager
        updateRecyclerViewAdapter()
        updateRecyclerViewPaginationBehavior()
    }

    // updateRecyclerViewAdapter(): Updates the RecyclerView adapter based on the adapterType.
    // Default value is `RecyclerViewAdapterType.RECYCLER_VIEW` if no value provided for adapterType.
    private fun updateRecyclerViewAdapter(
        adapterType: RecyclerViewAdapterType = RecyclerViewAdapterType.RECYCLER_VIEW
    ) {
        val currentAdapter = binding.recyclerView.adapter
        when (adapterType) {
            RecyclerViewAdapterType.LIST_ADAPTER -> {
                if (currentAdapter is RecyclerViewListAdapter) return
                recyclerViewListAdapter = RecyclerViewListAdapter()
                binding.recyclerView.adapter = recyclerViewListAdapter
            }
            RecyclerViewAdapterType.PAGING_DATA_ADAPTER -> {}
            RecyclerViewAdapterType.RECYCLER_VIEW -> {
                if (currentAdapter is RecyclerViewAdapter) return
                recyclerViewAdapter = RecyclerViewAdapter()
                binding.recyclerView.adapter = recyclerViewAdapter
            }
        }
    }

    // updateRecyclerViewPaginationBehavior(): Implements the infinite scroll behavior to load and
    // show more data items in the RecyclerView.
    private fun updateRecyclerViewPaginationBehavior() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val isLoadingMore = viewModel.state.value.isLoadingMore
                val currentDataItemSize = viewModel.state.value.dataList.size
                if (!isLoadingMore) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == currentDataItemSize - 1) {
                        viewModel.sendIntent(RecyclerViewIntent.LoadMoreDataIntent)
                    }
                }
            }
        })
    }

    // updateRecyclerViewData(): Updates the data list given the adapter type and updates the appropriate adapter.
    private fun updateRecyclerViewData(
        adapterType: RecyclerViewAdapterType,
        dataList: List<DataItem>
    ) {
        when (adapterType) {
            RecyclerViewAdapterType.LIST_ADAPTER ->
                recyclerViewListAdapter.submitList(dataList)
            RecyclerViewAdapterType.PAGING_DATA_ADAPTER -> {}
            RecyclerViewAdapterType.RECYCLER_VIEW -> {
                recyclerViewAdapter.updateList(dataList)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    // updateRecyclerViewPosition(): Scrolls to the the designated position in the RecyclerView.
    private fun updateRecyclerViewPosition(position: Int) {
        if (recyclerViewAdapter.itemCount > 0) {
            binding.recyclerView.scrollToPosition(position)
        }
    }

    // updateToolbarTitle(): Updates the toolbar title to match the currently selected adapter type.
    private fun updateToolbarTitle(adapterType: RecyclerViewAdapterType) {
        binding.recyclerViewToolbar.title = when (adapterType) {
            RecyclerViewAdapterType.LIST_ADAPTER -> getString(R.string.list_adapter)
            RecyclerViewAdapterType.PAGING_DATA_ADAPTER -> getString(R.string.paging_data_adapter)
            RecyclerViewAdapterType.RECYCLER_VIEW -> getString(R.string.recycler_view)
        }
    }
}