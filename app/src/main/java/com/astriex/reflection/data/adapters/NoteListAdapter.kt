package com.astriex.reflection.data.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.astriex.reflection.R
import com.astriex.reflection.data.models.Note
import com.astriex.reflection.databinding.ItemNoteRowBinding
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp

interface OnItemClickListener {
    fun onItemClick(note: Note)
}

class NoteListAdapter(
    private val context: Context,
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {
    var adapterNotes = mutableListOf<Note>()

    inner class NoteViewHolder(val binding: ItemNoteRowBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val currentNote = adapterNotes[adapterPosition]
            clickListener.onItemClick(
                Note(
                    currentNote.title,
                    currentNote.content,
                    currentNote.imageUrl,
                    currentNote.userId,
                    currentNote.timeAdded,
                    currentNote.username
                )
            )
        }
    }

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
        return DateUtils.getRelativeTimeSpanString(timestamp.seconds * 1000).toString()
    }

    fun setNotes(notes: List<Note>) {
        adapterNotes = notes as MutableList<Note>
        notifyDataSetChanged()
    }
}