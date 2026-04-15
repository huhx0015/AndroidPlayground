package com.huhx0015.androidplayground.feature.android.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huhx0015.androidplayground.model.DataItem
import com.huhx0015.androidplayground.R

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {

    companion object {
        private const val VAL_TITLE = "Title: "
        private const val VAL_SUBTITLE = "Subtitle: "
    }

    val dataItemList: MutableList<DataItem> = mutableListOf()

    // onCreateViewHolder(): Inflates the item layout and creates a new view holder.
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerViewViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_recycler_view, viewGroup, false)
        return RecyclerViewViewHolder(view)
    }

    // onBindViewHolder(): Binds title and subtitle values to the current list item view.
    override fun onBindViewHolder(
        viewHolder: RecyclerViewViewHolder,
        position: Int
    ) {
        val title = VAL_TITLE + dataItemList[position].title
        val subtitle = VAL_SUBTITLE + dataItemList[position].subtitle

        viewHolder.titleTextView.text = title
        viewHolder.subtitleTextView.text = subtitle
    }

    // getItemCount(): Returns the total number of items in the adapter data list.
    override fun getItemCount(): Int {
        return dataItemList.size
    }

    // updateList(): Replaces the adapter data with the provided list.
    internal fun updateList(list: List<DataItem>) {
        dataItemList.clear()
        dataItemList.addAll(list)
    }

    class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var titleTextView: TextView
        lateinit var subtitleTextView: TextView

        init {
            initView(view = itemView)
        }

        // initView(): Finds and stores references to the title and subtitle text views.
        private fun initView(view: View) {
            titleTextView = view.findViewById(R.id.recyclerview_title)
            subtitleTextView = view.findViewById(R.id.recyclerview_subtitle)
        }
    }
}