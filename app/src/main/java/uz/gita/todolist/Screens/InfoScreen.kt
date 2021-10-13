package uz.gita.todolist.Screens

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat

import androidx.navigation.fragment.findNavController
import uz.gita.todolist.R
import uz.gita.todolist.data.TaskEntity
import uz.gita.todolist.databinding.ScreenInfoBinding
import java.util.*

class InfoScreen : androidx.fragment.app.Fragment(R.layout.screen_info) {
    private var _viewBinding : ScreenInfoBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var remainTime : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = ScreenInfoBinding.bind(view)
        remainTime = view.findViewById(R.id.remainTime)
        arguments?.let {
            val data = it.getSerializable("data") as TaskEntity
            val pageNumber = it.getSerializable("page") as Int
            viewBinding.textTitleInfo.text = data.title
            viewBinding.textDescriptionInfo.text = data.description
            viewBinding.deadlineInfo.text = data.deadline
            statusOfItem(pageNumber)
            reminderStatus(data.reminder)
            setBgColor(pageNumber)
            setBottomLineColor(pageNumber)
           printDifferenceDateForHours(data)
        }

        viewBinding.back.setOnClickListener {
            countDownTimer.cancel()
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                countDownTimer.cancel()
                findNavController().popBackStack()
            }
        })
    }
    @SuppressLint("SetTextI18n")
    private fun statusOfItem(pos :Int){
        when(pos){
            0->viewBinding.statusInfo.text = "You haven't begin yet"
            1->viewBinding.statusInfo.text = "You are doing the task"
            2->viewBinding.statusInfo.text = "You have done the task"
        }
    }
    @SuppressLint("SetTextI18n")
    private fun reminderStatus(pos:Int){
        when(pos){
            0->viewBinding.reminderInfo.text = "1 hour before deadline"
            1->viewBinding.reminderInfo.text = "2 hour before deadline"
            2->viewBinding.reminderInfo.text = "3 hour before deadline"
            3->viewBinding.reminderInfo.text = "It is not necessary to remind"
        }
    }
    private fun setBgColor(pos:Int){
        when(pos){
            0->viewBinding.bottomSide.setBackgroundResource(R.drawable.bottom_bg)
            1->viewBinding.bottomSide.setBackgroundResource(R.drawable.bottom_bg1)
            2->viewBinding.bottomSide.setBackgroundResource(R.drawable.bottom_bg2)
        }
    }

    private fun setBottomLineColor(pos :Int){
        when(pos){
            0->viewBinding.bottomLine.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.todo_color))
            1->viewBinding.bottomLine.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.doing_color))
            2->viewBinding.bottomLine.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.done_color))

        }
    }

    private fun printDifferenceDateForHours(data:TaskEntity) {

        val currentTime = Calendar.getInstance().time
        val endDateDay = data.deadline
        val format1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val endDate = format1.parse(endDateDay)

        //milliseconds
        var different = endDate.time - currentTime.time
        countDownTimer = object : CountDownTimer(different, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

               remainTime.text = "$elapsedDays days $elapsedHours:" +
                       "$elapsedMinutes:$elapsedSeconds"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                remainTime.text = "Time is over"
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding  = null
    }

}