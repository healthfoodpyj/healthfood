package org.techtown.finalproject_healthfood

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.*
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class DiaryActivity : Activity() {
    lateinit var dp : DatePicker
    lateinit var edtDiary : EditText
    lateinit var btnWrite : Button
    lateinit var fileName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary) // xml 파일과 연결

        dp = findViewById<DatePicker>(R.id.datePicker1)
        edtDiary = findViewById<EditText>(R.id.edtDiary)
        btnWrite = findViewById<Button>(R.id.btnWrite)

        var cal = Calendar.getInstance()
        var cYear = cal.get(Calendar.YEAR)
        var cMonth = cal.get(Calendar.MONTH) // 1~12월 => 0~11이 실제 => + 1 필수
        var cDay = cal.get(Calendar.DAY_OF_MONTH)

        fileName = (Integer.toString(cYear) + "_"
                + Integer.toString(cMonth+1) + "_"
                + Integer.toString(cDay) + ".txt")
        var str = readDiary(fileName)
        edtDiary.setText(fileName)

        dp.init(cYear,cMonth,cDay){ view, year, monthOfYear, dayOfMonth ->
            fileName = (Integer.toString(year) + "_"
                    + Integer.toString(monthOfYear+1) + "_"
                    + Integer.toString(dayOfMonth) + ".txt")
            str = readDiary(fileName)
            edtDiary.setText(fileName)
            edtDiary.setText(str)
            //btnWrite.isEnabled = true // datePicker 움직이고나서 실행될 때 코드
        }

        btnWrite.setOnClickListener{
            // 파일 쓰기
            var outFs = openFileOutput(fileName, Context.MODE_PRIVATE)
            var str = edtDiary.text.toString()
            outFs.write(str.toByteArray())
            outFs.close() // write가 끝나면 반드시 close 해주기
            Toast.makeText(applicationContext, "$fileName 이 저장됨", Toast.LENGTH_SHORT).show()
        }

    }
    fun readDiary(fName:String) : String?{
        var diaryStr : String? = null
        var inFs : FileInputStream
        try{ // 파일이 있을 때
            inFs = openFileInput(fName)
            var txt = ByteArray(500)
            inFs.read(txt)
            inFs.close()
            diaryStr = txt.toString(Charsets.UTF_8).trim()
        }
        catch (e: IOException){ // 파일이 없을 때
            var inputS = resources.openRawResource(R.raw.android12)
            var txt = ByteArray(inputS.available())
            inputS.read(txt)
            edtDiary.hint = txt.toString(Charsets.UTF_8) // "일기 없음" 대신 android12파일에 저장된 내용 읽어와서 보여줌
            btnWrite.text = "새로 저장"
            inputS.close()
        }
        return diaryStr
    }
}