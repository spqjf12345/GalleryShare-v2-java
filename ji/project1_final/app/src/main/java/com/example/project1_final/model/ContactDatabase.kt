package com.example.project1_final.model

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

class ContactDatabase {
    var cursor: Cursor? = null
    var uri: Uri? = null
    var projection: Array<String>? = null
    var selectionClause: String? = null
    var selectionArgs: Array<String>? = null
    var sortOrder: String? = null

    companion object {
        var INSTANCE: ContactDatabase? = null

        fun getInstance(context: Context): ContactDatabase? {
            if (INSTANCE == null) {
                // Cursor
                val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                val projection = arrayOf(
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP
                )
                val selectionClause = null
                val selectionArgs = emptyArray<String>()
                val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
                val cursor = context?.contentResolver?.query(
                    uri,
                    projection,
                    selectionClause,
                    selectionArgs,
                    sortOrder
                )
                INSTANCE = ContactDatabase()
                INSTANCE!!.cursor = cursor
                INSTANCE!!.uri = uri
                INSTANCE!!.projection = projection
                INSTANCE!!.selectionClause = selectionClause
                INSTANCE!!.selectionArgs = selectionArgs
                INSTANCE!!.sortOrder = sortOrder
            }
            return INSTANCE
        }
        fun updateInstance(context: Context){
            // Cursor
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.IN_VISIBLE_GROUP
            )
            val selectionClause = null
            val selectionArgs = emptyArray<String>()
            val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            val cursor = context?.contentResolver?.query(
                    uri,
                    projection,
                    selectionClause,
                    selectionArgs,
                    sortOrder
            )
            INSTANCE = ContactDatabase()
            INSTANCE!!.cursor = cursor
            INSTANCE!!.uri = uri
            INSTANCE!!.projection = projection
            INSTANCE!!.selectionClause = selectionClause
            INSTANCE!!.selectionArgs = selectionArgs
            INSTANCE!!.sortOrder = sortOrder
        }

        fun accessItemList(context: Context){
            val contactData = arrayListOf<ContentValues>()
            val editIntent = Intent(Intent.ACTION_INSERT_OR_EDIT).apply {
                // Sets the MIME type
                type = ContactsContract.Contacts.CONTENT_ITEM_TYPE

            }

            // Sends the Intent with an request ID
            editIntent.putExtra("finishActivityOnSaveCompleted", true)
            context.startActivity(editIntent)
        }

        fun insertItem(context: Context, contact: Contact) {
            val contactData = arrayListOf<ContentValues>()
            val insertIntent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                // Sets the MIME type to the one expected by the insertion activity
                type = ContactsContract.RawContacts.CONTENT_TYPE

                // Sets the new contact name
                putExtra(ContactsContract.Intents.Insert.NAME, contact.name)
                putExtra(ContactsContract.Intents.Insert.PHONE, contact.phoneNumber)
                putExtra(ContactsContract.Intents.Insert.EMAIL, contact.email)

                putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData)
            }
            insertIntent.putExtra("finishActivityOnSaveCompleted", true)
            context.startActivity(insertIntent)
        }

        fun editItem(context: Context, cursor: Cursor?, position: Int, newContact: Contact) {
            // The Cursor that contains the Contact row
            cursor?.moveToPosition(position)
            // The index of the lookup key column in the cursor
            var lookupKeyIndex: Int = 0
            // The index of the contact's _ID value
            var idIndex: Int = 0
            // The lookup key from the Cursor
            var currentLookupKey: String? = null
            // The _ID value from the Cursor
            var currentId: Long = 0
            // A content URI pointing to the contact
            var selectedContactUri: Uri? = null

            cursor?.apply {
                // Gets the lookup key column index
                lookupKeyIndex = getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
                // Gets the lookup key value
                currentLookupKey = getString(lookupKeyIndex)
                // Gets the _ID column index
                idIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                currentId = getLong(idIndex)
                selectedContactUri =
                    ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey)
            }

            // Creates a new Intent to edit a contact
            val editIntent = Intent(Intent.ACTION_EDIT).apply {
                setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
            }

            editIntent.putExtra("finishActivityOnSaveCompleted", true)
            context.startActivity(editIntent)
        }
    }
}
