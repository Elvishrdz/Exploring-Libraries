package com.eahm.exploringlibraries.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.eahm.exploringlibraries.R

class DialogCredential : DialogFragment() {

    private lateinit var listener : IDialogCredentialListener

    private lateinit var username : EditText
    private lateinit var password : EditText


    interface IDialogCredentialListener {
        fun onDialogPossitive(username : String, password : String)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is IDialogCredentialListener) {
            listener = activity as IDialogCredentialListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = activity!!.layoutInflater.inflate(R.layout.dialog_credentials, null)

        username = view.findViewById(R.id.etDCUsername) as EditText
        password = view.findViewById(R.id.etDCPassword) as EditText

        username.setText(arguments!!.getString("username"))
        password.setText(arguments!!.getString("password"))

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            .setView(view)
            .setTitle("Credentials")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Continue",
                DialogInterface.OnClickListener { _, _ ->
                    listener.onDialogPossitive(
                        username.text.toString(),
                        password.text.toString()
                    )
                })
        return builder.create()
    }

}