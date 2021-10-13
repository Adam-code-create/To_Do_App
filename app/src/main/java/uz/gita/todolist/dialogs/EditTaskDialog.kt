package uz.gita.todolist.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import uz.gita.todolist.R
import uz.gita.todolist.data.TaskEntity
import java.util.*

class EditTaskDialog  : DialogFragment(R.layout.dialog_edit) {
    private var listener : ((TaskEntity) -> Unit)? = null
    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editTitle : TextInputEditText = view.findViewById(R.id.editTitle)
        val editDescription : TextInputEditText = view.findViewById(R.id.editDescription)
        val editDeadline : TextInputEditText = view.findViewById(R.id.deadlineEdit)
        val editReminder : TextInputEditText = view.findViewById(R.id.reminderEdit)
        val btnCancel : AppCompatButton = view.findViewById(R.id.cancelEdit)
        val btnSave : AppCompatButton = view.findViewById(R.id.saveEdit)
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendar.get(Calendar.MINUTE)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        var pos = 0
        var reminderNumber = 3
        val options = arrayOf(
            "1 hour ago", "2 hours ago", "3 hours ago", "It is not necessary to remind")
        arguments?.let {
            val data = it.getSerializable("data") as TaskEntity
            pos = data.id
            editTitle.setText(data.title)
            editDescription.setText(data.description)
            editDeadline.setText(data.deadline)
            editReminder.setText(options[data.reminder])
        }

        editDeadline.setOnClickListener {
            var dateText = ""
            val datePickerDialog = DatePickerDialog(requireContext(),{datepicker, year, month,day->
                val m= month+1
                dateText = "$day/$m/$year"
                val timePickerDialog = TimePickerDialog(requireContext(), { timePicker, hour, minutes ->
                    val t = String.format("%02d:%02d",hour, minutes)
                    editDeadline.setText("$dateText $t:00")
                    editDeadline.error = null

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

        editReminder.setOnClickListener {

            val dialog = AlertDialog.Builder(requireContext())
            dialog.setCancelable(false)
            dialog.setTitle("Choose the time")
            dialog.setSingleChoiceItems(options,-1) { dialogInterface, i: Int ->
                editReminder.setText(options[i])
                reminderNumber = i
                dialogInterface.dismiss()
            }
            dialog.setNeutralButton("Cancel"){listener, j :Int ->
                editReminder.setText(options[3])
                reminderNumber = 3
                listener.dismiss()
                editReminder.error = null
            }
            dialog.create().show()
        }

        btnSave.setOnClickListener {

            if (editTitle.text.toString() == "") editTitle.error = "Empty"
            else if (editDescription.text.toString() == "") editDescription.error = "Empty"
            else if (editDeadline.text.toString() == "") editDeadline.error ="Set deadline"
            else if (editReminder.text.toString() == "") {
                editReminder.error ="Empty"
            }
            else {
                editTitle.error = null
                editDescription.error = null
                editDeadline.error = null
                editReminder.error = null
                listener?.invoke(TaskEntity(0, editTitle.text.toString(), editDescription.text.toString(),editDeadline.text.toString(), reminderNumber, 0))
                dismiss()
            }
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }
    fun setEditDialog (f : (TaskEntity) -> Unit){
        listener = f
    }
}