package com.huhx0015.androidplayground.feature.android.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.huhx0015.androidplayground.R

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerViewViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_recycler_view, viewGroup, false)
        return RecyclerViewViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerViewViewHolder,
        viewType: Int
    ) {
        // TODO: Implement changes to each row.
        viewHolder.titleTextView.setText("Title Name")
        viewHolder.subtitleTextView.setText("Subtitle Name")
    }

    override fun getItemCount(): Int {
        // TODO: Return number of items.
        return 3
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