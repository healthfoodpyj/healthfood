package org.techtown.finalproject_healthfood

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.datefood.view.*

class DateFoodAdapter : RecyclerView.Adapter<DateFoodAdapter.ViewHolder>(){
    var items = ArrayList<DateFood>()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun setItem(item : DateFood){
            itemView.item1.text = item.fName

            if(item.fKind=="단백질"){
                itemView.foodImg.setImageResource(R.drawable.turkey)
            }
            else if(item.fKind=="탄수화물"){
                itemView.foodImg.setImageResource(R.drawable.bread)
            }
            else if(item.fKind=="과일(비타민)"){
                itemView.foodImg.setImageResource(R.drawable.fruits)
            }
            else if(item.fKind=="기타"){
                itemView.foodImg.setImageResource(R.drawable.breakfast)
            }

            itemView.setOnClickListener {
                Toast.makeText(itemView?.context, "유통기한 : "+item.fDate+"\n분류 : "+item.fKind,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DateFoodAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.datefood, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DateFoodAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

}