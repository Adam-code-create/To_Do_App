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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import uz.gita.todolist.R
import uz.gita.todolist.data.BookEntity
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.ContactEntity
import java.util.*

class EditBookDialog :DialogFragment(R.layout.dialog_edit_book) {
private var listener : ((BookEntity) -> Unit)? = null
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
        val btnCancel : AppCompatButton = view.findViewById(R.id.cancelEditBook)
        val btnSave : AppCompatButton = view.findViewById(R.id.saveEditBook)
        val name : TextInputEditText = view.findViewById(R.id.editBookName)
        val startDate : TextInputEditText = view.findViewById(R.id.editStartDate)
        val finishDate : TextInputEditText = view.findViewById(R.id.editFinishDate)
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendar.get(Calendar.MINUTE)
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("book") as BookEntity
            pos = data.id
            name.setText(data.name)
            startDate.setText(data.startDate)
            finishDate.setText(data.endDate)
        }


        startDate.setOnClickListener {
            var dateText = ""
            val datePickerDialog = DatePickerDialog(requireContext(),{datepicker, year, month,day->
                val m= month+1
                dateText = "$day/$m/$year"
                val timePickerDialog = TimePickerDialog(requireContext(), { timePicker, hour, minutes ->
                    val t = String.format("%02d:%02d",hour, minutes)
                    startDate.setText("$dateText $t:00")
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

        finishDate.setOnClickListener {
            var dateText = ""
            val datePickerDialog = DatePickerDialog(requireContext(),{datepicker, year, month,day->
                val m= month+1
                dateText = "$day/$m/$year"
                val timePickerDialog = TimePickerDialog(requireContext(), { timePicker, hour, minutes ->
                    val t = String.format("%02d:%02d",hour, minutes)
                    finishDate.setText("$dateText $t:00")
                } ,currentHour, currentMinutes, true)
                timePickerDialog.show()
            }, currentYear, currentMonth, currentDay)
            datePickerDialog.show()
        }


        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            listener?.invoke(BookEntity(pos, name.text.toString(), startDate.text.toString(), finishDate.text.toString()))
            dismiss()
        }
    }

    fun setListener(f :(BookEntity) -> Unit){
        listener = f
    }
}