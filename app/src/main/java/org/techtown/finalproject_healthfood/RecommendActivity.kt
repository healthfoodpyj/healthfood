package org.techtown.finalproject_healthfood

import android.app.Activity
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.recommend_menu.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL

class RecommendActivity : AppCompatActivity() { // 액티비티 상속받아야 액티비티로 사용가능
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommend_menu) // xml 파일과 연결
        listview1.layoutManager = LinearLayoutManager(this)

        val serviceUrl = "http://211.237.50.150:7080/openapi/sample/xml/Grid_20150827000000000226_1/1/5"
        val serviceKey = "4e1d1a63e918ba9c0500f627b62b7394f6a5a8b70501e1d2e285dd387e690ead"

        // http://211.237.50.150:7080/openapi/sample/xml/Grid_20150827000000000226_1/1/54e1d1a63e918ba9c0500f627b62b7394f6a5a8b70501e1d2e285dd387e690ead
        val requestUrl = serviceUrl + "?serviceKey=" + serviceKey
        fetchXML(requestUrl)

    }

    fun fetchXML(myURL:String){
        lateinit var page : String

        class getDangerGrade : AsyncTask<Void, Void, Void> (){
            override fun doInBackground(vararg p0: Void?): Void? { // url -> xml
                val stream = URL(myURL).openStream()
                val bufreader = BufferedReader(InputStreamReader(stream, "UTF-8"))
                var line = bufreader.readLine()
                page = ""
                while(line !=null){
                    page += line
                    line = bufreader.readLine()
                }
                return null
            }

            override fun onPostExecute(result: Void?) { // xml 파싱
                super.onPostExecute(result)
                var itemList:ArrayList<openRecipe> = arrayListOf()
                var bSet1 = false
                var bSet2 = false
                var bSet3 = false
                var bSet4 = false
                lateinit var FoodName : String
                lateinit var FoodKind : String
                lateinit var Sumry : String
                lateinit var Image_URL : String
                var factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(true)
                var xpp = factory.newPullParser() // XML 파서
                xpp.setInput(StringReader(page))
                var eventType = xpp.eventType
                while(eventType!= XmlPullParser.END_DOCUMENT){
                    if (eventType == XmlPullParser.START_DOCUMENT){}
                    else if(eventType == XmlPullParser.START_TAG){
                        var tag_name = xpp.name
                        if(tag_name.equals("RECIPE_NM_KO")) bSet1 = true
                        if(tag_name.equals("COOKING_TIME")) bSet2 = true
                        else if(tag_name.equals("IMG_URL")) bSet4 = true
                        if(tag_name.equals("SUMRY")) bSet3 = true
                    }
                    if(eventType == XmlPullParser.TEXT) {
                        if(bSet1) {
                            FoodName = xpp.text.toString()
                            bSet1 = false
                        }
                        if(bSet2) {
                            FoodKind = xpp.text.toString()
                            bSet2 = false
                        }
                        if(bSet3) {
                            Sumry = xpp.text.toString()
                            bSet3 = false
                        }
                        if(bSet4) {
                            Image_URL = xpp.text.toString()
                            bSet4 = false
                            var item = openRecipe(Image_URL,  FoodName,"소요시간:"+FoodKind+"\n"+Sumry)
                            itemList.add(item)
                        }
                    }
                    if(eventType == XmlPullParser.END_TAG){}
                    // itemList.add()
                    eventType = xpp.next()
                }
                listview1.adapter = openRecipeAdapter(itemList)
            }
        }

        getDangerGrade().execute()
    }
}