package com.swapnil.frnd.utility.adapters

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.frnd.R
import com.swapnil.frnd.model.Task

class TaskItemAdapter(
    private val dialogListener: CalendarEventsClickListener,
    private var tasksList: List<Task>
) : RecyclerView.Adapter<TaskViewHolder>() {

    private var backgroundColorPosition = 0
    private val mapNormalEventBg = mutableMapOf<Int, HolderState>() // Maintains Background color of events
    private val mapForEventInEditingText = mutableMapOf<Int, Editing>() // Maintains editing state of a event
    private val colorArray = intArrayOf(R.color.bg_ofOrange, R.color.bg_ofGreen, R.color.bg_ofPink, R.color.bg_lightBlue)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_task_item, parent, false)
        return TaskViewHolder(view, dialogListener, tasksList)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]

        var editingState = mapForEventInEditingText[holder.layoutPosition]
        // First Time setting the editing state of the card.
        if (editingState == null) {
            editingState = Editing(task.taskDetail!!.description, false)
            mapForEventInEditingText[holder.layoutPosition] = editingState
        }

        // get the Card State, i.e BG Color, Card Expanded or not
        var holderState = mapNormalEventBg[holder.layoutPosition]

        holder.setData(task, editingState)

        if (holderState == null) {
            // 1st Time setting the card holder state
            val colorPosition: Int = backgroundColorPosition % colorArray.size
            holder.setExpandedCardViewVisibility(View.GONE)
            holderState = HolderState(colorArray[colorPosition], false)
            mapNormalEventBg[holder.layoutPosition] = holderState
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
        notifyDataSetChanged()
    }


    companion object {
        /**
         * This class will hold state of that particular view holder
         * i.e background and whether the cardview is expanded
         */
        private data class HolderState(var color: Int, var isCardExpanded: Boolean)

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

    interface CalendarEventsClickListener {
        fun onDialogEditingClick(
            positiveDialogCallback: DialogInterface.OnClickListener?,
            position: Int,
            event: Task?
        )

        fun onDialogDeleteClick(
            deleteDialogCallback: DialogInterface.OnClickListener?,
            event: Task?,
            position: Int
        )
    }

}