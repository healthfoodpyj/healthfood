package org.techtown.finalproject_healthfood

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.recipe_main.*
import java.util.*

class RecipeMain  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_main)

        // 이제 메인액티비티와 동일하게 작업 시작
        var intent = intent
        var Pic = arrayOf(R.drawable.salad, R.drawable.food2, R.drawable.food3, R.drawable.food4)
        var Name = arrayOf("에그 샐러드", "연어 아보카도 덮밥","연어 스테이크","카레밥")
        var ImagePic = intent.getIntExtra("Image", 0) //putExtra의 첫번째 인자와 같아야 연결이 가능
        var recipeWay1 = arrayOf("1. 계란을 20분동안 끓는 물에 삶는다.\n2. 드레싱은 올리브유와 발사믹을 3:1의 비율로 섞는다\n3. 머스타드, 요거트 등 취향에 맞게 넣어준다.\n4. 준비한 채소와 함께 섞어 먹는다.",
            "1. 양파를 간장에 졸여 볶는다.\n2. 아보카도를 2등분 한다.\n3. 껍질을 벗긴 아보카도에 소금으로 밑간을 한다.\n4. 연어를 먹기좋게 손질하고 계란후라이를 올려 완성한다.",
            "1. 연어에 허브솔트를 뿌려 준비한다.\n2. 그릴에 앞뒤로 구워준다.\n3. 부추나 양파를 고춧가루, 매실액, 참기름 등으로 버무려준다.\n4. 파슬리를 뿌려 마무리한다.\n" ,
            "1. 당근, 양파, 감자 등 야채를 손질한다.\n2. 끓는 물에 카레 가루를 풀고 준비한 야채와 함께 끓인다.\n3. 밥 위에 뿌려 마무리한다.\n" )

        recipeMainImg.setImageResource(Pic[ImagePic])
        recipeMainText.setText(Name[ImagePic])
        recipeWay.setText(recipeWay1[ImagePic])
    }
}