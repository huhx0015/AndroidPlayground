package com.huhx0015.androidplayground.feature.android.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.model.DataItem
import com.huhx0015.androidplayground.R

class RecyclerViewListAdapter : ListAdapter<DataItem, RecyclerViewListAdapter.RecyclerViewViewHolder>(
  DIFF_CALLBACK
) {

  override fun onCreateViewHolder(
    viewGroup: ViewGroup,
    viewType: Int
  ): RecyclerViewViewHolder {
    val view: View = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.adapter_recycler_view, viewGroup, false)
    return RecyclerViewViewHolder(view)
  }

  override fun onBindViewHolder(viewHolder: RecyclerViewViewHolder, position: Int) {
    val item = getItem(position)
    viewHolder.titleTextView.text = item.title
    viewHolder.subtitleTextView.text = item.subtitle
  }

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
      override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
        oldItem == newItem
    }
  }

  class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var titleTextView: TextView
    lateinit var subtitleTextView: TextView

    init {
      initView(view = itemView)
    }

    private fun initView(view: View) {
      titleTextView = view.findViewById(R.id.recyclerview_title)
      subtitleTextView = view.findViewById(R.id.recyclerview_subtitle)
    }
  }
}
