package com.lovinsharma.kalanikethan.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.math.BigDecimal

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.PrimaryKey







class Family: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var familyName: String = ""
    var familyEmail: String? = null
    var students: RealmList<Student> = realmListOf()
    var parents: RealmList<Parent> = realmListOf()
    var paymentDate: Long = 0L // Next payment deadline
    var paymentHistory: RealmList<Payment> = realmListOf()
    var paymentAmount: String = ""
    var paymentID: String = ""
}

class Student: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var studentName: String = ""
    var studentNumber: String = ""
    var birthdate: String = ""
    var additionalInfo: String? = null
    var canWalkAlone: Boolean = false
    var signedIn: Boolean = false
}

class Parent: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var parentName: String = ""
    var parentNumber: String = ""
}

class SignInEvent: RealmObject {
    @Index var day: String = ""
    var student: Student? = null
    var signIn: Long = 0L
    var signOut: Long? = null
    var live: Boolean = false
}

class Payment: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var paymentDate: Long = 0L // Date of this specific payment
    var amountPaid: String = ""// Amount of payment made
}



