package uz.gita.todolist.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import uz.gita.todolist.R
import uz.gita.todolist.data.TaskEntity
import java.util.*

class AddTaskDialog : DialogFragment(R.layout.dialog_add_task) {
    private var listener :((TaskEntity) -> Unit)? = null

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
        val addNoteBtn : AppCompatButton = view.findViewById(R.id.add)
        val cancelNoteBtn : AppCompatButton = view.findViewById(R.id.cancel)
        val addNoteTitle : TextInputEditText = view.findViewById(R.id.addTitle)
        val addNoteDescription : TextInputEditText = view.findViewById(R.id.addDescription)
        val deadline : TextInputLayout = view.findViewById(R.id.deadlineOutline)
        val setDeadline :TextInputEditText = view.findViewById(R.id.deadlineInput)
        val setReminder :TextInputEditText = view.findViewById(R.id.reminderInput)
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendar.get(Calendar.MINUTE)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        var reminderNumber = -1

        addNoteBtn.setOnClickListener {

            if (addNoteTitle.text.toString() == "") addNoteTitle.error = "Empty"
            else if (addNoteDescription.text.toString() == "") addNoteDescription.error = "Empty"
            else if (setDeadline.text.toString() == "") setDeadline.error ="Set deadline"
            else if (setReminder.text.toString() == "") {
                setReminder.error ="Empty"
            }
            else {
                addNoteTitle.error = null
                addNoteDescription.error = null
                setDeadline.error = null
                setReminder.error = null
                listener?.invoke(TaskEntity(0, addNoteTitle.text.toString(), addNoteDescription.text.toString(),setDeadline.text.toString(), reminderNumber, 0))
                dismiss()
            }
        }
        cancelNoteBtn.setOnClickListener {
            dismiss()
        }

        setDeadline.setOnClickListener {
            var dateText = ""
            val datePickerDialog = DatePickerDialog(requireContext(),R.style.TimePickerTheme,{datepicker, year, month,day->
                val m= month+1
                dateText = "$day/$m/$year"
                val timePickerDialog = TimePickerDialog(requireContext(),R.style.TimePickerTheme, { timePicker, hour, minutes ->
                    val t = String.format("%02d:%02d",hour, minutes)
                    deadline.editText?.setText("$dateText $t:00")
                    setDeadline.error = null
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

        setReminder.setOnClickListener {
            val options = arrayOf(
            "1 hour ago", "2 hours ago", "3 hours ago", "It is not necessary to remind")
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setCancelable(false)
            dialog.setTitle("Choose the time")
            dialog.setSingleChoiceItems(options,-1) { dialogInterface, i: Int ->
             setReminder.setText(options[i])
                reminderNumber = i
                dialogInterface.dismiss()
            }
            dialog.setNeutralButton("Cancel"){listener, j :Int ->
                setReminder.setText(options[3])
                reminderNumber = 3
                listener.dismiss()
            }
            setReminder.error = null
            dialog.create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_NEUTRAL)
                        .setTextColor(ContextCompat.getColor(
                        requireContext(),
                        R.color.main_color))
                }
            }
                dialog.show()
        }
    }
    fun setDialogListener (f : (TaskEntity)-> Unit){
        listener = f
    }

}