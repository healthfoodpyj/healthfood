package org.techtown.finalproject_healthfood

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 sdk 초기화
        KakaoSdk.init(this, "3ffa004794fa262f0a997bc8635679d9")

    }
}