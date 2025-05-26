package org.iesharia.seguimientodehabitos.utils

import android.content.Context
import androidx.work.*
import org.iesharia.seguimientodehabitos.worker.ReminderWorker
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object ReminderService {
    fun programarRecordatorio(context: Context, hora: Int = 19, minuto: Int = 0) {
        val now = LocalDateTime.now()
        var scheduledTime = now.withHour(hora).withMinute(minuto).withSecond(0)
        if (scheduledTime.isBefore(now)) {
            scheduledTime = scheduledTime.plusDays(1)
        }

        //val delay = Duration.between(now, scheduledTime).toMillis()
        val delay = 5000L // 5 segundos
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("recordatorio_habitos")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}