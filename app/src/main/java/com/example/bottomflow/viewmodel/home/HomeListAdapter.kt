package com.example.bottomflow.viewmodel.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomflow.R

class HomeListAdapter(
    private val items: ArrayList<String>,
    private val onButtonClick: (String) -> Unit
) : RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(view) { onButtonClick(items[it]) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View, onItemViewClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onItemViewClick(absoluteAdapterPosition)
            }
        }

        private val button: Button = view.findViewById(R.id.btn_list)
        fun bind(item: String) {
            button.text = item
            button.setOnClickListener {
                onButtonClick(item)
            }
        }
    }
}