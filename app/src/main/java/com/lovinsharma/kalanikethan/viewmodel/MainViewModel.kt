package com.lovinsharma.kalanikethan.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lovinsharma.kalanikethan.models.Database
import com.lovinsharma.kalanikethan.models.Family
import com.lovinsharma.kalanikethan.models.FamilyUI
import com.lovinsharma.kalanikethan.models.Parent
import com.lovinsharma.kalanikethan.models.ParentUI
import com.lovinsharma.kalanikethan.models.Student
import com.lovinsharma.kalanikethan.models.StudentUI
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val realm = Database.realm


    private fun createFamily(
        family: FamilyUI,
        parents: List<ParentUI>,
        students: List<StudentUI>
    ) {
        // Make a copy of the lists to ensure thread safety
        val parentsCopy = parents.toList()
        val studentsCopy = students.toList()

        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                // Create a new family
                val newFamily = Family().apply {
                    familyName = family.familyName
                    familyEmail = family.familyEmail
                    paymentDate = family.paymentDate
                    paymentID = family.paymentID
                }

                // Add parents to the family
                val realmParents = realmListOf<Parent>()
                parentsCopy.forEach { parentUI ->
                    val parent = Parent().apply {
                        parentName = parentUI.parentName
                        parentNumber = parentUI.parentNumber
                    }
                    realmParents.add(parent)
                }
                newFamily.parents.addAll(realmParents)

                // Add students to the family
                val realmStudents = realmListOf<Student>()
                studentsCopy.forEach { studentUI ->
                    val student = Student().apply {
                        studentName = studentUI.studentName
                        studentNumber = studentUI.studentNumber
                        birthdate = studentUI.birthdate
                        additionalInfo = studentUI.additionalInfo
                        canWalkAlone = studentUI.canWalkAlone
                    }
                    realmStudents.add(student)
                }
                newFamily.students.addAll(realmStudents)

                // Save the new family to Realm
                copyToRealm(newFamily)
            }
        }
    }



    // Fetch all students from the database
    fun getStudents(): List<Student> {
        // Query all Student objects in the Realm database
        val students = realm.query<Student>().find()
        return students.toList() // Convert to immutable list
    }

    // You can call this function in your UI when the button is pressed
    fun onAddFamilyButtonPressed(
        family: FamilyUI,
        familyName: MutableState<String>,
        parents: MutableList<ParentUI>,
        students: MutableList<StudentUI>,
        addState: MutableState<Boolean>
    ) {

        createFamily(family, parents, students)

        // Clear the UI data after saving
        familyName.value = ""
        parents.clear()
        students.clear()
        addState.value = true
    }

}
