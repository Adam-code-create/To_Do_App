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
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.ContactEntity
import java.util.*

class EditBirthdayDialog :DialogFragment(R.layout.dialog_edit_birthday) {
private var listener : ((BirthdayEntity) -> Unit)? = null
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
        val btnCancel : AppCompatButton = view.findViewById(R.id.cancelEditBirthday)
        val btnSave : AppCompatButton = view.findViewById(R.id.saveEditBirthday)
        val editName : TextInputEditText = view.findViewById(R.id.editBirthName)
        val editDate : TextInputEditText = view.findViewById(R.id.editBirthdayDate)
        val editAge : TextInputEditText = view.findViewById(R.id.editAgeInput)
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("birthday") as BirthdayEntity
            pos = data.id
            editName.setText(data.name)
            editDate.setText(data.date)
            editAge.setText(data.age)
        }

        editDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(),{datepicker, year, month,day->
                val m= month+1
                editDate.setText("$day/$m/$year")
            }, currentYear, currentMonth, currentDay)
            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color))
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            listener?.invoke(BirthdayEntity(pos, editName.text.toString(), editDate.text.toString(), editAge.text.toString()))
            dismiss()
        }
    }

    fun setEditListener(f :(BirthdayEntity) -> Unit){
        listener = f
    }
}