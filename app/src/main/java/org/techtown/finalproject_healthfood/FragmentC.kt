package org.techtown.finalproject_healthfood

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_c.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentC.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentC : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var dlgEdtName : EditText
    lateinit var dlgEdtKind : String
    lateinit var dlgEdtDate : EditText
    lateinit var rdoGroup : RadioGroup
    private lateinit var adapter : DateFoodAdapter
    lateinit var databaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_c, container, false)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView4 = view.findViewById(R.id.recyclerView5) as RecyclerView
        recyclerView4.layoutManager = GridLayoutManager(activity,4)
        adapter = DateFoodAdapter()

        // 플로팅 버튼 (음식 추가)
        view.floatingBtn3.setOnClickListener {
            var dig = AlertDialog.Builder(activity)

            var dialogView = View.inflate(activity, R.layout.dialog1, null) // 커스텀 되어져 있는 뷰 만들어줌
            dig.setView(dialogView) // 커스텀해 만든 뷰가 대화상자에 설정 됨
            //dig.setTitle("사용자 정보 입력")
            dig.setPositiveButton("확인"){ dialog, which->
                dlgEdtName = dialogView.findViewById(R.id.dlgName) // 연결, dialogView에서 찾아야된다는 것 명시
                dlgEdtDate = dialogView.findViewById(R.id.dlgdate)
                //dlgEdtKind = dialogView.findViewById(R.id.dlgKind)
                rdoGroup = dialogView.findViewById<RadioGroup>(R.id.rGroup)
                when(rdoGroup.checkedRadioButtonId){
                    R.id.r1 -> dlgEdtKind = "단백질"
                    R.id.r2 -> dlgEdtKind = "탄수화물"
                    R.id.r3 -> dlgEdtKind = "채소"
                    R.id.r4 -> dlgEdtKind = "과일(비타민)"
                    R.id.r5 -> dlgEdtKind = "기타"
                    else -> Snackbar.make(it, "분류를 먼저 선택하세요", Snackbar.LENGTH_LONG).show()
                }
                //adapter.items.add(DateFood(dlgEdtName.text.toString()))
                SaveOneFood(dlgEdtName.text.toString(), dlgEdtDate.text.toString(), dlgEdtKind, "냉동")
                recyclerView4.adapter = adapter
            }
            dig.setNegativeButton("취소") { dialog, which->
                Snackbar.make(it, "취소했습니다", Snackbar.LENGTH_LONG).show()
            }
            dig.show()
        }
        recyclerView4.adapter = adapter

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
    }
    fun loadCommentList(dataSnapshot: DataSnapshot){
        val collectionIterator = dataSnapshot!!.children.iterator()
        if(collectionIterator.hasNext()){
            adapter.items.clear()
            val comments = collectionIterator.next()
            val itemsIterator = comments.children.iterator()
            while(itemsIterator.hasNext()){
                val currentItem = itemsIterator.next()
                val map = currentItem.value as HashMap<String, Any>
                val objectId = map["objectId"].toString()
                val fName = map["fName"] as String
                val fDate = map["fDate"] as String
                val fKind = map["fKind"] as String
                val Nang = map["Nang"] as String
                if(Nang=="냉동") {
                    adapter.items.add(DateFood(objectId,fName, fDate, fKind, Nang))
                }

            }
        }
        adapter.notifyDataSetChanged()
    }
    fun SaveOneFood(fName: String, fDate: String, fKind:String, Nang:String) {
        var key : String? = databaseRef.child("foods").push().key
        var onefood = DateFood(key!!, fName, fDate, fKind, Nang)
        val onefoodValues : HashMap<String, Any> = onefood.toMap()
        val childUpdates : MutableMap<String, Any> = HashMap()
        childUpdates["/foods/$key"] = onefoodValues
        databaseRef.updateChildren(childUpdates)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentC.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FragmentC().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}