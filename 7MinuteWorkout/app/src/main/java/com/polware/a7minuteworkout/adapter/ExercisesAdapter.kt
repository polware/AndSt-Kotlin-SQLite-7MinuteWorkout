package com.polware.a7minuteworkout.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.polware.a7minuteworkout.R
import com.polware.a7minuteworkout.model.ExercisesModel

class ExercisesAdapter(val items: ArrayList<ExercisesModel>, val context: Context):
    RecyclerView.Adapter<ExercisesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewItem: TextView = view.findViewById(R.id.textViewItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_exercise, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExercisesModel = items[position]
        holder.textViewItem.text = model.getId().toString()
        if (model.getIsSelected()){
            holder.textViewItem.background = ContextCompat.getDrawable(context,
                    R.drawable.item_exercise_number)
            holder.textViewItem.setTextColor(Color.parseColor("#212121"))
        }
        else if (model.getIsCompleted()) {
            holder.textViewItem.background = ContextCompat.getDrawable(context,
                R.drawable.layout_timer_background)
            holder.textViewItem.setTextColor(Color.parseColor("#FFFFFF"))
        }
        else {
            holder.textViewItem.background = ContextCompat.getDrawable(context,
                R.drawable.item_circular)
            holder.textViewItem.setTextColor(Color.parseColor("#212121"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}