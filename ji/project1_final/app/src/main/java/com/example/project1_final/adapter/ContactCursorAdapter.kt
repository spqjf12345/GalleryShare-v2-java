package com.example.project1_final.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1_final.ContactActivity
import com.example.project1_final.R
import com.example.project1_final.model.Contact
import kotlinx.android.synthetic.main.item_contact.view.*


class ContactCursorAdapter(private val context : Context, cursor: Cursor)
    :CursorRecyclerViewAdapter<ContactCursorAdapter.ViewHolder?>(context, cursor) {

    class ViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        val _view: View
        val image: ImageView
        val name: TextView
        val phone_number: TextView

        fun setItem(item: Contact, position: Int) {
            // image.?? = item?.image ?: "default.png"
            name.text = item?.name ?: "None"
            phone_number.text = item?.phoneNumber ?: "None"
            itemView.setOnClickListener {
                val nextIntent = Intent(_view.context, ContactActivity::class.java)
                val requestCode: Int = 1 // EDIT
                nextIntent.putExtra("operation", "edit")
                nextIntent.putExtra("position", position)
                (_view.context as Activity).startActivityForResult(nextIntent, requestCode)
            }
            itemView.contact_call.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:"+item.phoneNumber)
                (_view.context as Activity).startActivityForResult(callIntent, 0)
            }
        }

        init {
            _view = view
            image = view.findViewById(R.id.contact_image)
            name = view.findViewById(R.id.contact_name)
            phone_number = view.findViewById(R.id.contact_phone_number)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, cursor: Cursor?) {
        val contact: Contact = Contact.fromCursor(cursor)
        viewHolder!!.setItem(contact, cursor!!.position)
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }


}