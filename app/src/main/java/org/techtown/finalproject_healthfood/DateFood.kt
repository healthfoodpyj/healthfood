package org.techtown.finalproject_healthfood

import com.google.firebase.database.Exclude

data class DateFood(
    var objectId: String, // 키값, 자동생성됌
    var fName: String,
    var fDate : String,
    var fKind : String,
    var Nang : String,
) {

    @Exclude
    fun toMap(): HashMap<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["objectId"] = objectId
        result["fName"] = fName
        result["fDate"] = fDate
        result["fKind"] = fKind
        result["Nang"] = Nang
        return result
    }
}