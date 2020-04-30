package com.spad.taghvim.ui.preferences.locationathan.athan

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.spad.taghvim.utils.splitIgnoreEmpty

class PrayerSelectPreference(context: Context, attrs: AttributeSet?) :
    DialogPreference(context, attrs) {
    // convert comma separated string to a set
    // convert set to a comma separated string
    var prayers: Set<String>
        get() = getPersistedString("").splitIgnoreEmpty(",").toSet()
        set(prayers) {
            val wasBlocking = shouldDisableDependents()
            persistString(prayers.joinToString(","))
            val isBlocking = shouldDisableDependents()
            if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking)
        }
}
