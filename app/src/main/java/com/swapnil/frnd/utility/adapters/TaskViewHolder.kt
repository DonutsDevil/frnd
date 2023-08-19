package com.swapnil.frnd.utility.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Resources
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.swapnil.frnd.R
import com.swapnil.frnd.model.Task
import com.swapnil.frnd.model.TaskDetails

class TaskViewHolder(
    itemView: View,
    private val dialogListener: TaskItemAdapter.CalendarEventsClickListener,
    private val taskList: List<Task>
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var res: Resources
    private var mCardView: MaterialCardView
    private var llExpandCardView: LinearLayout

    private var tvEditText: TextView
    private var tvDeleteText: TextView
    private var tvTitle: TextView

    private var etDescription: EditText
    private var editingState: TaskItemAdapter.Companion.Editing? = null

    init {
        res = itemView.resources
        mCardView = itemView.findViewById(R.id.cardView_Event)
        llExpandCardView = itemView.findViewById(R.id.ll_expanded_carView)
        tvEditText = itemView.findViewById(R.id.tv_event_edit)
        tvDeleteText = itemView.findViewById(R.id.tv_event_delete)
        tvTitle = itemView.findViewById(R.id.tv_event_title)
        etDescription = itemView.findViewById(R.id.et_event_description)
        mCardView.setOnClickListener(this)
        tvEditText.setOnClickListener(this)
        tvDeleteText.setOnClickListener(this)
    }

    fun setData(task: Task, editing: TaskItemAdapter.Companion.Editing) {
        editingState = editing
        tvTitle.text = task.taskDetail!!.title
        if (editing.isEditing) {
            etDescription.setText(editing.changingText)
            tvEditText.visibility = View.VISIBLE
        } else {
            etDescription.setText(task.taskDetail.description)
            tvEditText.visibility = View.GONE
        }
    }

    fun setExpandedCardViewVisibility(state: Int) {
        if (state != View.VISIBLE && state != View.GONE && state != View.INVISIBLE) {
            return
        }
        llExpandCardView.visibility = state
    }

    fun setCardViewBackgroundColor(bgColor: Int) {
        mCardView.setCardBackgroundColor(res.getColor(bgColor))
    }


    private fun buildEditCompleteAlertDialog(
        position: Int,
        descriptionEt: EditText
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(itemView.context.applicationContext)
        builder.setTitle("Done Editing?")
        builder.setMessage("The edited text will replace your current text, You sure?")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val newDescription = descriptionEt.text.toString()
                    val task = taskList[position]
                    val taskDetails =
                        TaskDetails(task.taskDetail!!.title, newDescription, task.taskDetail.date)
                    val newTask = Task(task.id, task.taskId, taskDetails)
                    // convert list with new task or set it with notifyItemChange as position
                    dialogListener.onDialogEditingClick(this, position, newTask)
                }
            })
            .setNegativeButton("Cancel") { dialog, which ->
                if (editingState != null) {
                    descriptionEt.setText(editingState?.ogText)
                }
            }
            .setCancelable(false)
        builder.create()
        return builder
    }

    private fun buildDeleteCompleteAlertDialog(position: Int): AlertDialog.Builder? {
        val builder = AlertDialog.Builder(itemView.context.applicationContext)
        builder.setTitle("Delete?")
        builder.setMessage("You won't be able to recover this after deleting, You sure want to Delete? ")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val task = taskList[position]
                    dialogListener.onDialogDeleteClick(this, task, position)
                }
            })
            .setNegativeButton(
                "Cancel"
            ) { dialog, which -> }
            .setCancelable(false)
        builder.create()
        return builder
    }

    override fun onClick(v: View?) {
        if (v is TextView && v.getId() == R.id.tv_event_edit) {
            buildEditCompleteAlertDialog(layoutPosition, etDescription).show()
        } else if (v is TextView && v.getId() == R.id.tv_event_delete) {
            buildDeleteCompleteAlertDialog(layoutPosition)!!.show()
        } else {
            if (llExpandCardView.visibility == View.GONE) {
                setExpandedCardViewVisibility(View.VISIBLE)
            } else {
                setExpandedCardViewVisibility(View.GONE)
            }
        }
    }

}

