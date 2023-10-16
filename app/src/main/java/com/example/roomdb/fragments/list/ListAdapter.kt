package com.example.roomdb.fragments.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.R
import com.example.roomdb.model.User

class ListAdapter: RecyclerView.Adapter<ListAdapter.myViewHolder>() {
    private var userList = emptyList<User>()

     class myViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        fun bind(currentItem: User) {

//            itemView.findViewById<TextView>(R.id.id_txt).text = currentItem.id.toString()
            itemView.findViewById<TextView>(R.id.firstName_txt).text = currentItem.firstName
            itemView.findViewById<TextView>(R.id.lastName_txt).text = currentItem.lastName
            itemView.findViewById<TextView>(R.id.age_txt).text = currentItem.age.toString()
            itemView.findViewById<ImageView>(R.id.imageView).setImageBitmap(currentItem.profilePhoto)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(myHolder: myViewHolder, position: Int) {
        val currentItem = userList[position]
        myHolder.bind(currentItem)
        myHolder.itemView.findViewById<LinearLayout>(R.id.rowLayout).setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            myHolder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user : List<User>){
        this.userList = user
        notifyDataSetChanged()
    }

}