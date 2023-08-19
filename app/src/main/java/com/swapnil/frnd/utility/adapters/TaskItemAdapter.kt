package com.swapnil.frnd.utility.adapters

import android.app.AlertDialog
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.swapnil.frnd.R
import com.swapnil.frnd.model.Task
import com.swapnil.frnd.model.TaskDetails

class TaskItemAdapter(
    private val dialogListener: CalendarEventsClickListener,
    private var tasksList: List<Task>
) : RecyclerView.Adapter<TaskItemAdapter.TaskViewHolder>() {

    private var backgroundColorPosition = 0

    val holderStateMap = mutableMapOf<Int, HolderState>() // Maintains Background color of events
    val editorStateMap = mutableMapOf<Int, Editing>() // Maintains editing state of a event

    private val colorArray =
        intArrayOf(R.color.bg_ofOrange, R.color.bg_ofGreen, R.color.bg_ofPink, R.color.bg_lightBlue)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_task_item, parent, false)
        return TaskViewHolder(view, dialogListener, tasksList)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]

        var editingState = editorStateMap[holder.layoutPosition]
        // First Time setting the editing state of the card.
        if (editingState == null) {
            editingState = Editing(task.taskDetail!!.description, false)
            editorStateMap[holder.layoutPosition] = editingState
        }

        // get the Card State, i.e BG Color, Card Expanded or not
        var holderState = holderStateMap[holder.layoutPosition]

        holder.setData(task, editingState)

        if (holderState == null) {
            // 1st Time setting the card holder state
            val colorPosition: Int = backgroundColorPosition % colorArray.size
            holder.setExpandedCardViewVisibility(View.GONE)
            holderState = HolderState(colorArray[colorPosition], false)
            holderStateMap[holder.layoutPosition] = holderState
            backgroundColorPosition++
        } else if (holderState.isCardExpanded) {
            holder.setExpandedCardViewVisibility(View.VISIBLE)
        } else {
            holder.setExpandedCardViewVisibility(View.GONE)
        }
        holder.setCardViewBackgroundColor(holderState.color)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun submit(list: List<Task>) {
        tasksList = list
        holderStateMap.clear()
        editorStateMap.clear()
        notifyDataSetChanged()
    }


    companion object {
        /**
         * This class will hold state of that particular view holder
         * i.e background and whether the cardview is expanded
         */
        data class HolderState(var color: Int, var isCardExpanded: Boolean)

        /**
         * Class that maintains the editing state of events
         */
        data class Editing(val ogText: String, var isEditing: Boolean) {
            var changingText = ""

            override fun toString(): String {
                return "Editing{" +
                        "ogText='" + ogText + '\'' +
                        ", isEditing=" + isEditing +
                        ", changingText='" + changingText + '\'' +
                        '}'
            }
        }

    }

    inner class TaskViewHolder(
        itemView: View,
        private val dialogListener: CalendarEventsClickListener,
        private val taskList: List<Task>,
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
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val editorState = editorStateMap[layoutPosition]
                    if (editorState?.ogText != s.toString()) {
                        tvEditText.visibility = View.VISIBLE
                        editorState?.isEditing = true
                        editorState?.changingText = s.toString()
                    } else {
                        tvEditText.visibility = View.GONE
                        editorState.isEditing = false
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }

        fun setData(task: Task, editing: Editing) {
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
                .setPositiveButton("Yes"
                ) { _, _ ->
                    val newDescription = etDescription.text.toString()
                    val task = taskList[layoutPosition]
                    val taskDetails =
                        TaskDetails(
                            task.taskDetail!!.title,
                            newDescription,
                            task.taskDetail.date
                        )
                    val newTask = Task(/*task.id,*/ task.taskId, taskDetails)
                    // convert list with new task or set it with notifyItemChange as position
                    dialogListener.onDialogEditingClick(newTask)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    val editorState = editorStateMap[layoutPosition]
                    editorState?.let {
                        etDescription.setText(it.ogText)
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
                .setPositiveButton("Yes"
                ) { _, _ ->
                    val task = taskList[layoutPosition]
                    dialogListener.onDialogDeleteClick(task)
                }
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
                val holderState = holderStateMap[layoutPosition]
                // Save Card View State
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

    interface CalendarEventsClickListener {
        fun onDialogEditingClick(task: Task?)

        fun onDialogDeleteClick(task: Task?)
    }

}