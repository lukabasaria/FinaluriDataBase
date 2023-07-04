package com.example.finaluridatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.firestore.auth.User

class UserAdapter(private val context: Context, private val userList: List<User>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val user = userList[position]
        viewHolder.nameTextView.text = user.name
        viewHolder.ageTextView.text = user.age.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return userList.size
    }

    private class ViewHolder(view: View) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val ageTextView: TextView = view.findViewById(R.id.ageTextView)
    }
}
