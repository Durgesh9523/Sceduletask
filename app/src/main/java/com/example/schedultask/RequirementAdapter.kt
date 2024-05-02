package com.example.schedultask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RequirementAdapter( private val listener: OnItemClickListener,): RecyclerView.Adapter<RequirementAdapter.RequirementHolder>(){
    private var data: List<Requirement> = emptyList()

    fun setData(newData: List<Requirement>) {
        data = newData
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequirementHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return RequirementHolder(view)
    }
    override fun onBindViewHolder(holder: RequirementHolder, position: Int) {
        val item =data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RequirementHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val title:TextView = itemView.findViewById(R.id.title)
        private val label: TextView =itemView.findViewById(R.id.label)
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Requirement) {
            title.text=item.requirementName
            label.text=item.requirementSpecial
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = data[position]
                listener.onItemClick(item)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(item: Requirement)
    }
}