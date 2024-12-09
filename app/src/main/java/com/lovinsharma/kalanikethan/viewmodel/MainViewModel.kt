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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.liveData
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mongodb.kbson.ObjectId

class MainViewModel: ViewModel() {

    private val realm = Database.realm

    // To observe changes in the students that aren't signed in
    private val _unsignedStudents = MutableStateFlow<List<Student>>(emptyList())
    val unsignedStudents: StateFlow<List<Student>> get() = _unsignedStudents


    private val _signedStudents = MutableStateFlow<List<Student>>(emptyList())
    val signedStudents: StateFlow<List<Student>> get() = _signedStudents


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




    // Fetches all families along with related students, parents, and payments as a Flow
    fun getAllFamilies(): Flow<List<Family>> = flow {
        // Query to fetch all families (includes students, parents, and payments)
        val families = realm.query<Family>().find()

        // Emit the result to observers
        emit(families)
    }


    init {
        fetchUnsignedStudents()
        fetchSignedStudents() // Also fetch signed-in students
    }

    // Fetch all unsigned students and update the StateFlow
    private fun fetchUnsignedStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            val students = realm.query<Student>("signedIn == false").find()
            _unsignedStudents.value = students.toList() // Update the state
        }
    }

    // Fetch all signed-in students and update the StateFlow
    private fun fetchSignedStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            val students = realm.query<Student>("signedIn == true").find()
            _signedStudents.value = students.toList() // Update the state
        }
    }


    // Sign the student in
    fun signIn(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                val studentToUpdate = findLatest(student)
                studentToUpdate?.signedIn = true
            }
            fetchUnsignedStudents() // Refresh the list after sign-in
        }
    }
    // Sign the student out
    fun signOut(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                val studentToUpdate = findLatest(student)
                studentToUpdate?.signedIn = false
            }
            fetchSignedStudents() // Refresh the list after sign-out
        }
    }

    fun getFamilyById(id: ObjectId): Family? {
        return realm.query<Family>("_id == $0", id).first().find()
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
