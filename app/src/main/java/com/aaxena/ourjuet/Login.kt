package com.aaxena.ourjuet

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

var captch = ""
lateinit var mEnrollment: EditText
lateinit var mPassword: EditText
lateinit var mDob: EditText

class Login: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mEnrollment = findViewById(R.id.enrNumber)
        mPassword = findViewById(R.id.password)
        mDob = findViewById(R.id.dob)
    }


    fun connect(view: View) {
        if (validate()){
            var loginform: Elements? = null
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val doc: Document = Jsoup.connect("https://webkiosk.juet.ac.in").get()
                    var id = doc.title()
                    Log.d("success", "$id")
                    loginform = doc.getElementsByClass("noselect")
                    Log.d("sucess", "$loginform")
                    captch= loginform.toString().substring(32,37)
                    Log.d("sucess", "$loginform,${captch.length}")
                } catch (e: Exception) {
                    Log.e("er", "$e=.toString())")
                }}
        }
    }
}

    fun validate(): Boolean {
        if (mEnrollment.text.toString().isEmpty()) {
            mEnrollment.error = "Enrollment Number is Required"
           // vibrateDeviceError()
            return false
        } else if (mDob.text.toString().isEmpty()) {
            mDob.error = "Date of Birth is Required"
           // vibrateDeviceError()
            return false
        } else if (mPassword.text.toString().isEmpty()) {
            mPassword.error = "Password is Required"
           // vibrateDeviceError()
            return false
        }
        return true
    }

/*
    private fun vibrateDeviceError() {
        val v3 = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 25, 50, 35, 100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            v3.vibrate(pattern, -1)
        }
    }

 */
/*
//    var dialog: ProgressDialog? = null
//    fun showProgress() {
//        dialog = ProgressDialog(this)
//        dialog!!.setMessage("Logging In...")
//        dialog!!.setProgressPercentFormat(null)
//        dialog!!.setProgressNumberFormat(null)
//        dialog!!.setCancelable(false)
//        dialog!!.setCanceledOnTouchOutside(false)
//        dialog!!.show()
//    }
//
//    fun dismissProgress() {
//        if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
//    }
 */