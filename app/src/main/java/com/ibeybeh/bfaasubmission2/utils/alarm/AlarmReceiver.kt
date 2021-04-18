package com.ibeybeh.bfaasubmission2.utils.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.presentation.main.MainActivity
import com.ibeybeh.bfaasubmission2.presentation.splash.SplashScreenActivity
import com.ibeybeh.bfaasubmission2.utils.Const.CHANNEL_ID
import com.ibeybeh.bfaasubmission2.utils.Const.CHANNEL_NAME
import com.ibeybeh.bfaasubmission2.utils.Const.EXTRA_MESSAGE
import com.ibeybeh.bfaasubmission2.utils.Const.ID_REPEATING
import com.ibeybeh.bfaasubmission2.utils.Const.NOTIFICATION_ID
import com.ibeybeh.bfaasubmission2.utils.Const.TIME
import com.ibeybeh.bfaasubmission2.utils.Const.TIME_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showAlarmNotification(
            context,
            context.resources.getString(R.string.daily_reminder),
            context.resources.getString(R.string.label_return_to_app)
        )
    }

    private fun showAlarmNotification(context: Context, title: String, message: String) {

        val intent = Intent(context, SplashScreenActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(context)
            .addParentStack(SplashScreenActivity::class.java)
            .addNextIntent(intent)
            .getPendingIntent(ID_REPEATING, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_baseline_notifications_active_24))
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME

            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun setDailyReminder(context: Context, time: String, message: String) {

        if (isDateInvalid(TIME, TIME_FORMAT))
            return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)

        val timeArray = time.split(
            ":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, context.resources.getString(R.string.label_daily_reminder_set_on), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, context.resources.getString(R.string.label_daily_reminder_set_off), Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}