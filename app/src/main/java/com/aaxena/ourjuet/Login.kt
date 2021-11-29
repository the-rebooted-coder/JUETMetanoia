package com.aaxena.ourjuet

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.net.CacheResponse
import kotlin.math.log



lateinit var document: Document
lateinit var mapCookies: Map<String, String>
lateinit var response: Elements
lateinit var captch :String
lateinit var mEnrollment: EditText
lateinit var mPassword: EditText
lateinit var mDob: EditText
lateinit var trygetcookie:Connection.Response
var cookies:Map<String, String>? = null

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
            val loginscope = GlobalScope.launch(Dispatchers.IO) {


                try {
                    trygetcookie = Jsoup.connect("https://webkiosk.juet.ac.in")
                        .method(Connection.Method.GET)
                        .execute()

                     cookies= trygetcookie.cookies()
                    val doc: Document = Jsoup.connect("https://webkiosk.juet.ac.in").get()

                    var id = doc.title()
                    Log.d("success", "$id")

                    loginform = doc.getElementsByClass("noselect")
                    Log.d("sucess", "$loginform")

                    captch= loginform.toString().substring(32,37)
                    Log.d("sucess", captch)


                } catch (e: Exception) {
                    Log.e("er", "$e=.toString())")

                }}

                     val form_filled = GlobalScope.launch (Dispatchers.IO){
                    loginscope.join()


                         val response: Connection.Response = Jsoup.connect("https://webkiosk.juet.ac.in/StudentFiles/StudentPage.jsp")
                             .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (XHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
                             .referrer("https://webkiosk.juet.ac.in")
                                 .method(Connection.Method.POST)
                                 .data("MemberCode", mEnrollment.text.toString())
                                 .data("DATE1", mDob.text.toString())
                                 .data("Password", mPassword.text.toString())
                                 .data("txtcap", captch)
                                 .cookies(cookies)
                                 .followRedirects(true)
                                 .execute()

                         //parse the document from response
                       document  = response.parse()




                         //get cookies
                         mapCookies = response.cookies()







//                    var response:Connection.Response = Jsoup.connect("https://webkiosk.juet.ac.in").data("MemberCode", mEnrollment.text.toString(),
//                            "DATE1", mDob.text.toString(),"Password", mPassword.text.toString(),"txtcap", captch).method(Connection.Method.POST).execute()

                }
                       GlobalScope.launch(Dispatchers.Main){
                           form_filled.join()
                           Log.e("suc", document.baseUri())

//                           Log.e("Successful", mapCookies.toString().substring(0,10))
                           Toast.makeText(this@Login, document.getElementById("table-1").toString(),Toast.LENGTH_SHORT).show()

                       }



        }
    }
}
fun test(){


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