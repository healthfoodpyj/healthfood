package org.techtown.finalproject_healthfood

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nangjang.*
import kotlinx.android.synthetic.main.recipe_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter : DateFoodAdapter
    private lateinit var adapter2 : DateFoodAdapter
    private lateinit var databaseRef : DatabaseReference
    var check = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView2.layoutManager = GridLayoutManager(this, 4)
        adapter = DateFoodAdapter()

        recyclerView2.adapter = adapter

        recyclerView9.layoutManager = GridLayoutManager(this, 4)
        adapter2 = DateFoodAdapter()

        recyclerView9.adapter = adapter2

        recipeBtn.setOnClickListener {
            var intent = Intent(applicationContext, RecommendActivity::class.java)
            startActivityForResult(intent, 0)
        }

        // 메인 레시피 추천 부분
        //1. 변수 선언
        val random = Random()
        val num = random.nextInt(4)

        // 4개의 이미지 배열
        var image = arrayOf(R.drawable.salad, R.drawable.food2, R.drawable.food3, R.drawable.food4)
        var imageName = arrayOf("에그 샐러드", "연어 아보카도 덮밥","연어 스테이크","카레밥")

        mainFood.setImageResource(image[num])
        recommendFood.setText("\n"+imageName[num])
        recipe.setOnClickListener {
            // RecipeMain 이동, 연결.  Intent를 통해서 이동
            var intent = Intent(applicationContext, RecipeMain::class.java) // 두번째 인자에 이동할 액티비티
            intent.putExtra("Image", num) //putExtra(첫번째 인자 넘겨받을 데이터 이름 , 두번째 인자 넘길데이터변수이름)
            //intent.putExtra("ImageName",imageName[num])
            startActivityForResult(intent, 0) // 두방향, intent전달하고 두번째 인자는 일반적으로 0
        }
        // 레시피 추천부분 끝
        databaseRef = FirebaseDatabase.getInstance().reference

        databaseRef.orderByKey().limitToFirst(10).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("test", "loadItem:onCancelled : ${error.toException()}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadCommentList(snapshot)
            }
        })
        //메뉴바 코드 시작
        setSupportActionBar(toolbar)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.item1 -> {
                    with(supportFragmentManager.beginTransaction()) {
                        var intent = Intent(applicationContext, MainActivity::class.java) // 두번째 인자에 이동할 액티비티
                        startActivityForResult(intent, 0)
                        commit()
                    }
                }
                R.id.item2 -> {
                    with(supportFragmentManager.beginTransaction()) {
                        var intent = Intent(applicationContext, NangJangActivity::class.java) // 두번째 인자에 이동할 액티비티
                        startActivityForResult(intent, 0)
                        commit()
                    }
                }
                R.id.item3 -> {
                    with(supportFragmentManager.beginTransaction()) {
                        var intent = Intent(applicationContext, RecommendActivity::class.java) // 두번째 인자에 이동할 액티비티
                        startActivityForResult(intent, 0)
                        commit()
                    }
                }
                R.id.item4 ->{
                    with(supportFragmentManager.beginTransaction()) {
                        var intent = Intent(applicationContext, DiaryActivity::class.java) // 두번째 인자에 이동할 액티비티
                        startActivityForResult(intent, 0)
                        commit()
                    }
                }
                R.id.item5 ->{
                    Toast.makeText(applicationContext, "이용해주셔서 감사합니다.",Toast.LENGTH_SHORT).show()
                    // 로그아웃
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            //Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                        }
                        else {
                            //Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                        }
                    }
                    var intent = Intent(applicationContext, LoginActivity::class.java) // 두번째 인자에 이동할 액티비티
                    startActivityForResult(intent, 0) // 두방향, intent전달하고 두번째 인자는 일반적으로 0
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
        //메뉴바 코드 끝
    }

    // firebase에 저장된 데이터 가져와서 adapter에 넣기
    fun loadCommentList(dataSnapshot: DataSnapshot){
        val collectionIterator = dataSnapshot!!.children.iterator()
        if(collectionIterator.hasNext()){
            adapter.items.clear()
            val comments = collectionIterator.next()
            val itemsIterator = comments.children.iterator()
            var a = arrayOf(0,0,0,0,0)
            val b = arrayOf("단백질","탄수화물","채소","과일(비타민)","골고루")
            while(itemsIterator.hasNext()){
                val currentItem = itemsIterator.next()
                val map = currentItem.value as HashMap<String, Any>
                val objectId = map["objectId"].toString()
                val fName = map["fName"] as String
                val fKind = map["fKind"] as String
                val Nang = map["Nang"] as String
                val fDate = map["fDate"] as String
                if(fKind=="단백질") a[0]=1;if(fKind=="탄수화물") a[1]=1;if(fKind=="채소") a[2]=1;if(fKind=="과일(비타민)") a[3]=1;if(fKind=="기타") a[4]=1;
                if(differ(fDate)<0){
                    adapter2.items.add(DateFood(objectId, fName, fDate, fKind, Nang))
                }
                else if(differ(fDate)<=3){
                    adapter.items.add(DateFood(objectId, fName, fDate, fKind, Nang))
                }
                recyclerView9.adapter = adapter2
                recyclerView2.adapter = adapter
            }
            var i=0
            while(i<5){
                if((a[i]==0) and (check==0)){
                    Toast.makeText(applicationContext,"오늘은 "+b[i]+" 섭취하세요!!",Toast.LENGTH_LONG).show()
                    check=1
                }
                i++
            }
        }
        // 데이터 변화 adapterd에 알리기
        adapter.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        var mInflater = menuInflater
        mInflater.inflate(R.menu.category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.homeMN -> {
                var intent = Intent(applicationContext, MainActivity::class.java) // 두번째 인자에 이동할 액티비티
                startActivityForResult(intent, 0)
                return true}
            R.id.nangjangMN -> {
                var intent = Intent(applicationContext, NangJangActivity::class.java) // 두번째 인자에 이동할 액티비티
                startActivityForResult(intent, 0)
                return true}
            R.id.recipeMN -> {
                var intent = Intent(applicationContext, RecommendActivity::class.java) // 두번째 인자에 이동할 액티비티
                startActivityForResult(intent, 0)
                return true}
            R.id.diaryMN -> {
                var intent = Intent(applicationContext, DiaryActivity::class.java) // 두번째 인자에 이동할 액티비티
                startActivityForResult(intent, 0)
                return true}
            R.id.logout -> {
                Toast.makeText(applicationContext, "이용해주셔서 감사합니다.",Toast.LENGTH_SHORT).show()
                // 로그아웃
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        //Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    }
                    else {
                       //Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    }
                }
                var intent = Intent(applicationContext, LoginActivity::class.java) // 두번째 인자에 이동할 액티비티
                startActivityForResult(intent, 0) // 두방향, intent전달하고 두번째 인자는 일반적으로 0

            }
        }
        return false
    }*/
    fun differ(endDay:String): Int {
        Log.d(endDay, "일자 확인")
        val cal = Calendar.getInstance()
        var mon = (Integer.parseInt(cal.get(Calendar.MONTH).toString())+1).toString()
        var d = cal.get(Calendar.DATE).toString()
        if(cal.get(Calendar.MONTH)<10){
            mon = "0"+mon
        }
        if(Integer.parseInt(cal.get(Calendar.DATE).toString())<10){
            d = "0"+cal.get(Calendar.DATE).toString()
        }
        val day = cal.get(Calendar.YEAR).toString()+mon+d
        Log.d(day,"오늘 날짜")

        return Integer.parseInt(endDay)-Integer.parseInt(day)
    }
}