package uz.gita.todolist.Screens

import android.annotation.SuppressLint
import android.app.*
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.gita.todolist.R
import uz.gita.todolist.adapter.ViewPagerAdapter
import uz.gita.todolist.data.TaskEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.dialogs.AddTaskDialog
import uz.gita.todolist.pages.DoingPage
import uz.gita.todolist.pages.DonePage
import uz.gita.todolist.pages.TodoPage
import uz.gita.todolist.reciever.MyReceiver
import java.util.*
import kotlin.collections.ArrayList

class MainScreen : Fragment(R.layout.menu_layout) {

    private lateinit var adapter : ViewPagerAdapter
    private val taskDao = TaskDatabase.getDatabase().getDao()
    private var data = ArrayList<TaskEntity>()
    private val toDoPage = TodoPage()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar: Calendar

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val doingPage = DoingPage()
        val donePage = DonePage()
        adapter = ViewPagerAdapter(requireActivity(), toDoPage, doingPage, donePage)
        notificationChannel()

        val viewpager: ViewPager2 = view.findViewById(R.id.pager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val addBtn: LottieAnimationView = view.findViewById(R.id.addList)
        val menuBtn: ImageView = view.findViewById(R.id.btn_menu)
        val drawerLayout: DrawerLayout = view.findViewById(R.id.drawerlayout)
        val navView: NavigationView = view.findViewById(R.id.nav_drawer)
        tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.main_color), ContextCompat.getColor(requireContext(), R.color.main_color))

        viewpager.adapter = adapter
        TabLayoutMediator(tabLayout, viewpager)  { tab, position ->
            when(position){
                0 -> {
                    tab.text = "ToDo"

                }
                1 ->{
                    tab.text = "Doing"
                }
                else -> {
                    tab.text = "Done"
                }
            }
        }.attach()
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position==0) addBtn.visibility = View.VISIBLE
                else addBtn.visibility = View.GONE


            }
        })
        menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.contacts ->{
                        findNavController().navigate(R.id.action_fragmentMain_to_screenContacts)
                    }
                    R.id.birthdays -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_birthdayScreen)
                    }
                    R.id.emails ->{
                        findNavController().navigate(R.id.action_fragmentMain_to_emailScreen)
                    }
                    R.id.websites -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_websiteScreen)
                    }
                    R.id.bankAccounts -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_bankAccountScreen)
                    }
                    R.id.appointments -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_appointmentScreen)
                    }
                    R.id.myBooks -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_bookScreen)
                    }
                    R.id.articles -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_articleScreen)
                    }
                }
                true
            }
        }
        addBtn.setOnClickListener {
            val dialog = AddTaskDialog()
            dialog.setDialogListener {
                taskDao.insert(it)
                toDoPage.loadData()
                data.add(it)
                toDoPage.backgroundNotes(data)
                deadlineTime(data)
            }
            dialog.show(childFragmentManager,"dialog")
        }
        toDoPage.setListener {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            bundle.putInt("page", 0)
            findNavController().navigate(R.id.action_fragmentMain_to_infoScreen2,bundle)
        }
        doingPage.setListener {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            bundle.putInt("page", 1)
            findNavController().navigate(R.id.action_fragmentMain_to_infoScreen2, bundle)
        }
        donePage.setListener {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            bundle.putInt("page", 2)
            findNavController().navigate(R.id.action_fragmentMain_to_infoScreen2, bundle)
        }
        toDoPage.setUpdateDoingPageListener {
            doingPage.loadData()

        }
        doingPage.setUpdateDonePageListener {
            donePage.loadData()

        }
        doingPage.setUpdateToDoPageListener {
            toDoPage.loadData()

        }
        donePage.setUpdateDonePageListener {
            doingPage.loadData()

        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setReminder(deadline : Long){
         alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), MyReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline, pendingIntent)

    }
    private fun cancelAlarm(){
        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), MyReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
    private fun notificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val name: CharSequence = "todoAppChannel"
            val description = "Channel for alarm manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("todoapp", name, importance)
            channel.description = description

            getSystemService(
                requireContext(), NotificationManager::class.java
            )?.createNotificationChannel(channel)
        }
    }
    private fun deadlineTime(list:ArrayList<TaskEntity>){
     if (list.size !=0) {
         for (i in list){
             if (i.reminder<3){
                 val endDateDay = i.deadline
                 val format1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                     SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
                 } else {
                     TODO("VERSION.SDK_INT < N")
                 }
                 val endDate = format1.parse(endDateDay)
                 var diffTime =1L
                 when(i.reminder){
                     0->{
                         diffTime *= 1000 * 60 * 60
                     }
                     1->{
                         diffTime *= (1000 * 60 * 60) * 2
                     }
                     2->{
                         diffTime *= (1000 * 60 * 60) * 3
                     }
                 }
                 setReminder(endDate.time -diffTime)
             }
         }
     }


    }



}