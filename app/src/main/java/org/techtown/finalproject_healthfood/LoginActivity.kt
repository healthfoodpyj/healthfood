package org.techtown.finalproject_healthfood

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.login.*

class LoginActivity : AppCompatActivity() {
    var TAG = "kakaologin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        btn_kakao_login.setOnClickListener {
        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                UserApiClient.instance.me{ user, error ->
                    if(error!=null){
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    }
                    else if(user != null){
                        var scopes = mutableListOf<String>()
                        if(user.kakaoAccount?.email != null) {
                            Log.i("KAKAO_API","email:${user.kakaoAccount?.email.toString()}")
                        }
                        else if(user.kakaoAccount?.emailNeedsAgreement == true){
                            // 동의 요청 후 이메일 획득 가능
                            scopes.add("account_email")
                            UserApiClient.instance.loginWithNewScopes(applicationContext, scopes){ token, error ->
                                if(error!=null) {
                                    Log.e(TAG, "사용자 추가 동의 실패")
                                }
                                else {
                                    UserApiClient.instance.me { user, error ->
                                        if(error!=null) {
                                            Log.e(TAG,"사용자 정보 요청 실패", error)
                                        }
                                        else if(user!=null) {
                                            Log.i(TAG, "사용자 정보 요청 성공 : ${user.kakaoAccount?.email}")
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            // 이메일 획득 불가
                            Toast.makeText(applicationContext, "이메일 획득 불가", Toast.LENGTH_SHORT).show()
                        }
                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("email", user.kakaoAccount?.email.toString())
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
            UserApiClient.instance.loginWithKakaoTalk(applicationContext, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
        }
        }
        // 로그인 버튼
        btn_login.setOnClickListener {

            //editText로부터 입력된 값을 받아온다
            var id = edit_id.text.toString()
            var pw = edit_pw.text.toString()

            // 쉐어드로부터 저장된 id, pw 가져오기
            val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
            val savedId = sharedPreference.getString("id", "")
            val savedPw = sharedPreference.getString("pw", "")

            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
            if(id == savedId && pw == savedPw){
                // 로그인 성공 다이얼로그 보여주기
                dialog("success")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                // 로그인 실패 다이얼로그 보여주기
                dialog("fail")
            }
        }

        // 회원가입 버튼
        btn_register.setOnClickListener {
            val intent = Intent(this, Join::class.java)
            startActivity(intent)
        }

    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공!")
            dialog.setMessage("환영합니다.")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    //DialogInterface.BUTTON_POSITIVE ->
                        //Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}