package com.astriex.reflection.adapters

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astriex.reflection.R
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.databinding.ItemNoteRowBinding
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp

class NoteListAdapter(private val context: Context) :
    RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {
    private var adapterNotes = mutableListOf<Note>()

    inner class NoteViewHolder(val binding: ItemNoteRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return adapterNotes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = adapterNotes[position]
        holder.binding.apply {
            tvNoteTitle.text = note.title
            tvNoteContent.text = note.content
            Glide.with(context)
                .load(note.imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(ivNoteImage)
            tvNoteTimestamp.text = getRelativeTimestamp(note.timeAdded!!)
            tvUsername.text = note.username
        }
    }

    private fun getRelativeTimestamp(timestamp: Timestamp): String {
        return DateUtils.getRelativeTimeSpanString(timestamp.seconds.toLong() * 1000).toString()
    }

    fun setNotes(notes: List<Note>) {
        adapterNotes = notes as MutableList<Note>
        notifyDataSetChanged()
    }
}