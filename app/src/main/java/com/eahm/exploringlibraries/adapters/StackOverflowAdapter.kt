package com.eahm.exploringlibraries.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.models.Answer

class StackOverflowAdapter(
    private val content : List<Answer>
) : RecyclerView.Adapter<StackOverflowAdapter.StackOverflowViewHolder>(){

    private lateinit var rootView : View

    inner class StackOverflowViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(element: Answer) {
            answerId.text = element.answerId.toString()
            answer.text = element.toString()

            cardStack.setOnClickListener{
                Toast.makeText(rootView.context, element.owner.toString(), Toast.LENGTH_LONG).show()
            }
        }

        private val cardStack : CardView = view.findViewById(R.id.cardStack)
        private val answerId : TextView = view.findViewById(R.id.tvStackAnswerId)
        private val answer : TextView = view.findViewById(R.id.tvStackAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackOverflowViewHolder {
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_stackoverflow, parent, false)
        return StackOverflowViewHolder(rootView)
    }

    override fun getItemCount(): Int = content.size

    override fun onBindViewHolder(holder: StackOverflowViewHolder, position: Int) {
        holder.bind(content[position])
    }
}
