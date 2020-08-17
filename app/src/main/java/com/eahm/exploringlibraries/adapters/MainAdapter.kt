package com.eahm.exploringlibraries.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.eahm.exploringlibraries.R
import com.eahm.exploringlibraries.models.MenuElement
import com.eahm.exploringlibraries.ui.MainActivity
import com.google.android.material.snackbar.Snackbar

class MainAdapter(
    private val content : List<MenuElement>
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>(){

    private lateinit var rootView : View

    inner class MainViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(item: MenuElement) {
            title.text = item.title
            subtitle.text = item.subtitle
            description.text = item.description
            card.setOnClickListener{
                if(item.activity != MainActivity::class.java){
                    rootView.context.startActivity(Intent(rootView.context, item.activity))
                }
                else Snackbar.make(rootView, rootView.context.resources.getString(R.string.you_are_in_the_main_menu), Snackbar.LENGTH_SHORT).show()
            }
        }

        private val card : CardView = view.findViewById(R.id.cardMain)
        private val title : TextView = view.findViewById(R.id.tvCardMainTitle)
        private val subtitle : TextView = view.findViewById(R.id.tvCardMainSubtitle)
        private val description : TextView = view.findViewById(R.id.tvCardMainDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return MainViewHolder(rootView)
    }

    override fun getItemCount(): Int = content.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(content[position])
    }
}