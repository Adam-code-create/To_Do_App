package uz.gita.todolist.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import uz.gita.todolist.R
import uz.gita.todolist.data.AppointmentEntity
import java.util.*

class EditAppointmentDialog :DialogFragment(R.layout.dialog_edit_appontment) {
private var listener : ((AppointmentEntity) -> Unit)? = null
    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnCancel : AppCompatButton = view.findViewById(R.id.cancelEditAppointment)
        val btnSave : AppCompatButton = view.findViewById(R.id.saveEditAppointment)
        val name : TextInputEditText = view.findViewById(R.id.editAppointName)
        val place : TextInputEditText = view.findViewById(R.id.editPlace)
        val date : TextInputEditText = view.findViewById(R.id.editTime)
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendar.get(Calendar.MINUTE)



        date.setOnClickListener {
            var dateText = ""
            val datePickerDialog = DatePickerDialog(requireContext(),{datepicker, year, month,day->
                val m= month+1
                dateText = "$day/$m/$year"
                val timePickerDialog = TimePickerDialog(requireContext(), { timePicker, hour, minutes ->
                    val t = String.format("%02d:%02d",hour, minutes)
                    date.setText("$dateText $t:00")
                } ,currentHour, currentMinutes, true)
                timePickerDialog.show()
                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color))
            }, currentYear, currentMonth, currentDay)
            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color))
        }
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("appointment") as AppointmentEntity
            pos = data.id
            name.setText(data.name)
            place.setText(data.place)
            date.setText(data.date)
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            listener?.invoke(AppointmentEntity(pos, name.text.toString(), place.text.toString(), date.text.toString()))
            dismiss()
        }
    }

    fun setListener(f :(AppointmentEntity) -> Unit){
        listener = f
    }
}