package com.spad.taghvim.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.spad.taghvim.utils.setChangeDateWorker
import com.spad.taghvim.utils.update
import com.spad.taghvim.utils.updateStoredPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.Main) {
        try {
            setChangeDateWorker(context)
            updateStoredPreference(applicationContext)
            update(applicationContext, true)
            Result.success()
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
