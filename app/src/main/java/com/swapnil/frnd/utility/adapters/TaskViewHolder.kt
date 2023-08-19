package com.swapnil.frnd.utility.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
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
    private val taskList: List<Task>,
    private val holderStateMap: MutableMap<Int, TaskItemAdapter.Companion.HolderState>,
    private val holderEditorState: MutableMap<Int, TaskItemAdapter.Companion.Editing>
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var res: Resources
    private var mCardView: MaterialCardView
    private var llExpandCardView: LinearLayout

    private var tvEditText: TextView
    private var tvDeleteText: TextView
    private var tvTitle: TextView

    private var etDescription: EditText

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

        etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val editorState = holderEditorState[layoutPosition]
                if (editorState?.ogText != s.toString()) {
                    tvEditText.visibility = View.VISIBLE
                    editorState?.isEditing = true
                    editorState?.changingText = s.toString()
                } else {
                    tvEditText.visibility = View.GONE
                    editorState.isEditing = false
                }
                editorState?.let {
                    holderEditorState.put(layoutPosition, it)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun setData(task: Task, editing: TaskItemAdapter.Companion.Editing) {
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


    private fun buildEditCompleteAlertDialog(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Done Editing?")
        builder.setMessage("The edited text will replace your current text, You sure?")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val newDescription = etDescription.text.toString()
                    val task = taskList[layoutPosition]
                    val taskDetails =
                        TaskDetails(task.taskDetail!!.title, newDescription, task.taskDetail.date)
                    val newTask = Task(task.id, task.taskId, taskDetails)
                    // convert list with new task or set it with notifyItemChange as position
                    dialogListener.onDialogEditingClick(this, layoutPosition, newTask)
                }
            })
            .setNegativeButton("Cancel") { dialog, which ->
                val editorState = holderEditorState[layoutPosition]
                if (editorState != null) {
                    etDescription.setText(editorState.ogText)
                }
            }
            .setCancelable(false)
        builder.create()
        return builder
    }

    private fun buildDeleteCompleteAlertDialog(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Delete?")
        builder.setMessage("You won't be able to recover this after deleting, You sure want to Delete? ")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val task = taskList[layoutPosition]
                    dialogListener.onDialogDeleteClick(this, task, layoutPosition)
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
            // Show Edit Dialog
            buildEditCompleteAlertDialog().show()
        } else if (v is TextView && v.getId() == R.id.tv_event_delete) {
            // show Delete Dialog
            buildDeleteCompleteAlertDialog().show()
        } else {
            // Save Card View State
            val holderState: TaskItemAdapter.Companion.HolderState? = holderStateMap[layoutPosition]
            if (llExpandCardView.visibility == View.GONE) {
                setExpandedCardViewVisibility(View.VISIBLE)
                holderState?.isCardExpanded = true
            } else {
                setExpandedCardViewVisibility(View.GONE)
                holderState?.isCardExpanded = false
            }
            holderState?.let {
                holderStateMap[layoutPosition] = it
            }
        }
    }

}

