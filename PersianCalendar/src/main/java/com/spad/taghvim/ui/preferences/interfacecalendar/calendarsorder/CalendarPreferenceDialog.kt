package com.spad.taghvim.ui.preferences.interfacecalendar.calendarsorder

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.edit
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spad.taghvim.PREF_MAIN_CALENDAR_KEY
import com.spad.taghvim.PREF_OTHER_CALENDARS_KEY
import com.spad.taghvim.R
import com.spad.taghvim.ui.MainActivity
import com.spad.taghvim.utils.appPrefs
import com.spad.taghvim.utils.getEnabledCalendarTypes
import com.spad.taghvim.utils.getOrderedCalendarEntities
import com.spad.taghvim.utils.updateStoredPreference

class CalendarPreferenceDialog : AppCompatDialogFragment() {

    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as MainActivity

        val values = ArrayList<String>()
        val titles = ArrayList<String>()
        val enabled = ArrayList<Boolean>()

        updateStoredPreference(activity)
        val enabledCalendarTypes = getEnabledCalendarTypes()
        val orderedCalendarTypes = getOrderedCalendarEntities(activity)
        orderedCalendarTypes.forEach {
            values.add(it.type.toString())
            titles.add(it.toString())
            enabled.add(it.type in enabledCalendarTypes)
        }
        val adapter = RecyclerListAdapter(this, activity, titles, values, enabled)
        val recyclerView = RecyclerView(activity).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            this.adapter = adapter
        }

        val callback = SimpleItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback).apply {
            attachToRecyclerView(recyclerView)
        }

        return AlertDialog.Builder(activity).apply {
            setView(recyclerView)
            setTitle(R.string.calendars_priority)
            setNegativeButton(R.string.cancel, null)
            setPositiveButton(R.string.accept) { _, _ ->
                val ordering = adapter.result
                activity.appPrefs.edit {
                    if (ordering.isNotEmpty()) {
                        putString(PREF_MAIN_CALENDAR_KEY, ordering[0])
                        putString(
                            PREF_OTHER_CALENDARS_KEY,
                            ordering.subList(1, ordering.size).joinToString(",")
                        )
                    }
                }
            }
        }.create()
    }

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper?.startDrag(viewHolder)
    }
}
