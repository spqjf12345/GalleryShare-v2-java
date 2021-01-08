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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1_final.R
import com.example.project1_final.model.Record


class RecordCursorAdapter(private val context : Context, cursor: Cursor)
    :CursorRecyclerViewAdapter<RecordCursorAdapter.ViewHolder?>(context, cursor) {

    class ViewHolder constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        val _view: View
        var rank: TextView? = null
        val name: TextView
        val time: TextView
        val score: TextView

        fun setItem(item: Record, position: Int) {
            rank?.text = (position+1).toString()
            name.text = item?.name ?: "None"
            score.text = item?.score.toString() ?: "None"
            time.text = "${item?.time/60}:${(item?.time).rem(60)}" ?: "None"
        }

        init {
            _view = view
            rank = view.findViewById(R.id.record_rank)
            name = view.findViewById(R.id.record_name)
            time = view.findViewById(R.id.record_time)
            score = view.findViewById(R.id.record_score)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, cursor: Cursor?) {
        val record: Record = Record.fromCursor(cursor)
        viewHolder!!.setItem(record, cursor!!.position)
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    private fun millisecFormatter(millisec: Int){
        "${millisec/1000} : ${millisec.rem(1000)}"
    }


}