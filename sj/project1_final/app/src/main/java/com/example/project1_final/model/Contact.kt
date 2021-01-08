package com.example.project1_final.model
import android.database.Cursor
import android.provider.ContactsContract

class Contact {
    var lookup_key: String = "NULL"
    var id: String = "NULL"
    var name: String = "NULL"
    var phoneNumber: String = "NULL"
    var email: String = "NULL"
    var group: String = "NULL"
    var image: String = "NULL"

    companion object {
        fun fromCursor(cursor: Cursor?): Contact {
            val contact = Contact()
            contact.lookup_key = cursor!!.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
            contact.id = cursor!!.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
            contact.name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            contact.phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contact.group = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP))
            return contact
        }
    }
}