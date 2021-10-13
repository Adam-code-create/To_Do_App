package uz.gita.todolist.reciever


import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uz.gita.todolist.R
import uz.gita.todolist.Screens.MainScreen


class MyReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, MainScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntend = PendingIntent.getActivity(context, 0, i, 0)

        val builder = NotificationCompat.Builder(context, "todoapp")
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle("Your task manager")
            .setContentText("Your task deadline will end soon...")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntend)
        val notificationManager= NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())


    }


}