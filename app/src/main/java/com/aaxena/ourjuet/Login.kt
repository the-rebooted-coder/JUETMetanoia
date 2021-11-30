package com.aaxena.ourjuet

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


var ashu:Pair<Connection.Response?,Connection.Response?>? = null
lateinit var  shrey:Job
var res1: Connection.Response? = null
var captcha: String? = ""
var res: Connection.Response? = null
var res_doc:Document? = null
lateinit var document: Document
var attd:Document? = null
var attd_test:Document? = null
var ATTENDENCE_LIST: String? = null
lateinit var sahil_ash: Job
lateinit var mapCookies: Map<String, String>
const val JSOUP_TIMEOUT = 50 * 1000
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
            sahil_ash = GlobalScope.launch(Dispatchers.IO) {
               ashu = login(this@Login)
               attd_test= test()
          }

         GlobalScope.launch(Dispatchers.IO) {
             sahil_ash.join()
            attd_test= test()
        }

        try {
            Log.i("suc",ashu!!.second!!.body().toString())
            Log.i("suc", attd_test!!.toString())

        }catch (e:Exception){
            Log.i("e","$e" )

        }

    }

    suspend fun test(): Document? {
        sahil_ash.join()
        GlobalScope.launch(Dispatchers.IO) {   val connection = Jsoup
                .connect("https://webkiosk.juet.ac.in/StudentFiles/Academic/StudentAttendanceList.jsp")
                .timeout(JSOUP_TIMEOUT)
                .cookies(res!!.cookies())
                .method(Connection.Method.GET).execute()
            attd = connection.parse() }
        return attd
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

    fun login(
        mContext: Context?,
    ): Pair<Connection.Response?, Connection.Response?>? {
        var res1: Connection.Response? = null
        var res: Connection.Response? = null
        res1 = Jsoup
            .connect("https://webkiosk.juet.ac.in")
            .method(Connection.Method.GET)
            .execute()
        val doc = res1.parse()
        var captcha: String? = ""
        try {
            captcha = doc.select(".noselect").first().text()
        } catch (ignored: java.lang.Exception) {
        }
        res = Jsoup
            .connect("https://webkiosk.juet.ac.in/CommonFiles/UserAction.jsp")
            .cookies(res1.cookies())
            .data(
                "txtInst",
                "Institute",
                "InstCode",
                "JUET",
                "x",
                "",
                "txtuType",
                "Member+Type",
                "UserType",
                "S",
                "txtCode",
                "Enrollment+No",
                "MemberCode",
                mEnrollment.text.toString(),
                "DOB",
                "DOB",
                "DATE1",
               mDob.text.toString(),
                "txtPin",
                "Password%2FPin",
                "Password",
                mPassword.text.toString(),
                "txtCodecaptcha",
                "Enter Captcha",
                "txtcap",
                captcha,
                "BTNSubmit",
                "Submit"
            )
            .method(Connection.Method.POST)
            .execute()
        return Pair(res1, res)
    }
}


    private fun vibrateDeviceError(context: Context) {
        val v3 = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 25, 50, 35, 100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            v3.vibrate(pattern, -1)
        }
    }
