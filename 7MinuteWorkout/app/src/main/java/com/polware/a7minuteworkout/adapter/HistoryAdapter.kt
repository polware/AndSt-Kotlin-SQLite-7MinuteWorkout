package com.polware.a7minuteworkout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.polware.a7minuteworkout.HistoryActivity
import com.polware.a7minuteworkout.R
import com.polware.a7minuteworkout.model.HistoryModel

class HistoryAdapter(val context: Context, val items: ArrayList<HistoryModel>):
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val linearLayoutItemsHistory: LinearLayout = view.findViewById(R.id.linearLayoutItemsHistory)
        val textViewPosition: TextView = view.findViewById(R.id.textViewPosition)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        val imageDelete: ImageView = view.findViewById(R.id.imageDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.textViewPosition.text = (position + 1).toString()
        holder.textViewDate.text = item.date
        if (position % 2 == 0) {
            holder.linearLayoutItemsHistory.setBackgroundColor(ContextCompat
                .getColor(context, R.color.light_gray))
        } else {
            holder.linearLayoutItemsHistory.setBackgroundColor(ContextCompat
                .getColor(context, R.color.light_orange))
        }

        holder.imageDelete.setOnClickListener{
            if (context is HistoryActivity){
                context.deleteDateAlertDialog(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}