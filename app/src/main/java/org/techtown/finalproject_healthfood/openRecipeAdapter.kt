package org.techtown.finalproject_healthfood

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recipe.view.*
import java.net.URI
import java.net.URL
import java.net.URLConnection

class openRecipeAdapter(var items : ArrayList<openRecipe>) : RecyclerView.Adapter<openRecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): openRecipeAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: openRecipeAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount() = items.count()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun setItem(item:openRecipe){
            itemView.listview_text2.text = item.recipeNM
            itemView.listview_text3.text = item.cookTime

            Glide.with(itemView).load(Uri.parse(item.imgUrl)).into(itemView.list_Image)
            itemView.list_Image.setImageURI(Uri.parse(item.imgUrl))
        }
    }

}