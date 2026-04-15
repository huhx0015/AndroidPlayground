package com.huhx0015.androidplayground.feature.android.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huhx0015.androidplayground.model.DataItem
import com.huhx0015.androidplayground.R

class RecyclerViewListAdapter : ListAdapter<DataItem, RecyclerViewListAdapter.RecyclerViewViewHolder>(
  DIFF_CALLBACK
) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {

      // areItemsTheSame(): Checks whether two items represent the same underlying entity.
      override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
        oldItem.id == newItem.id

      // areContentsTheSame(): Checks whether two items have identical displayed content.
      override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
        oldItem == newItem
    }

    private const val VAL_TITLE = "Title: "
    private const val VAL_SUBTITLE = "Subtitle: "
  }

  // onCreateViewHolder(): Inflates the item layout and creates a new view holder.
  override fun onCreateViewHolder(
    viewGroup: ViewGroup,
    viewType: Int
  ): RecyclerViewViewHolder {
    val view: View = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.adapter_recycler_view, viewGroup, false)
    return RecyclerViewViewHolder(view)
  }

  // onBindViewHolder(): Binds the current list item data to the view holder UI.
  override fun onBindViewHolder(viewHolder: RecyclerViewViewHolder, position: Int) {
    val item = getItem(position)
    val title = VAL_TITLE + item.title
    val subtitle = VAL_SUBTITLE + item.subtitle

    viewHolder.titleTextView.text = title
    viewHolder.subtitleTextView.text = subtitle
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
