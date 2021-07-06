package org.techtown.finalproject_healthfood

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.nangjang.*

class NangJangActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nangjang) // xml 파일과 연결
        pager.adapter = VpagerAdapter(this)
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager.offscreenPageLimit = 3

        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                //Toast.makeText(application, "${position+1} 페이지 선택됨", Toast.LENGTH_SHORT).show()
            }
        })
        button.setOnClickListener {
            pager.currentItem = 0
        }
        button2.setOnClickListener {
            pager.currentItem = 1
        }
        button3.setOnClickListener {
            pager.currentItem = 2
        }

    }
}