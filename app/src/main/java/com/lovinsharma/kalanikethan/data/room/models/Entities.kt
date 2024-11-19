package com.lovinsharma.kalanikethan.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


// This is the family_table
@Entity(tableName = "family_table")
data class Family(
    @ColumnInfo(name = "family_id") @PrimaryKey(autoGenerate = true) val familyID: Int=0,
    @ColumnInfo(name = "family_name") val familyName: String,
    @ColumnInfo(name = "payment_date") val paymentDate: String,
)


// This is the students_table
@Entity(
    tableName = "students_table",
    foreignKeys = [ForeignKey(
        entity = Family::class,
        parentColumns = ["family_id"],
        childColumns = ["family_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Student(
    @ColumnInfo(name = "student_id") @PrimaryKey(autoGenerate = true) val studentID: Int=0,
    @ColumnInfo(name = "student_name") var studentName: String,
    @ColumnInfo(name = "student_number") var studentNumber: String,
    @ColumnInfo(name = "student_dob") var birthdate: String,
    @ColumnInfo(name = "can_walk_alone") var canWalkAlone: Boolean = false,
    @ColumnInfo(name = "signed_in") val signedIn: Boolean = false,
    @ColumnInfo(name = "family_id") val familyIDfk: Int,
    @ColumnInfo(name = "payment_id") val paymentID: String,
)

// This is the event table
@Entity(
    tableName = "event_table",
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = ["student_id"],
        childColumns = ["student_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Event(
    @ColumnInfo(name = "event_id") @PrimaryKey(autoGenerate = true) val eventId: Int = 0,
    @ColumnInfo(name = "day") var day: String,
    @ColumnInfo(name = "student_name") var studentName: String,
    @ColumnInfo(name = "sign_in_time") var signIn: String,
    @ColumnInfo(name = "sign_out_time") var signOut: String,
    @ColumnInfo(name = "student_id") var studentIDfk: Int,
    @ColumnInfo(name = "is_Open") var isOpen: Boolean = false,
    @ColumnInfo(name = "is_absent") var isAbsent: Boolean = false,
    @ColumnInfo(name = "absence_reason") var reasonForAbsence: String="",
)



@Entity(
    tableName = "parents_table",
    foreignKeys = [ForeignKey(
        entity = Family::class,
        parentColumns = ["family_id"],
        childColumns = ["family_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Parent(
    @PrimaryKey(autoGenerate = true) val parentID: Int = 0,
    @ColumnInfo(name = "parent_name") val parentName: String,
    @ColumnInfo(name = "parent_number") val parentNumber: String,
    @ColumnInfo(name = "parent_email") val parentEmail: String,
    @ColumnInfo(name = "family_id") val familyIDfk: Int,
)

