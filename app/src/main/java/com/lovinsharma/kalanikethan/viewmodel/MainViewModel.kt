package com.lovinsharma.kalanikethan.viewmodel

import android.provider.SyncStateContract.Helpers.insert
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
import androidx.room.util.query
import com.lovinsharma.kalanikethan.models.SignInEvent
import com.lovinsharma.kalanikethan.screens.getFormattedDay
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.util.Calendar
import java.util.Date

class MainViewModel: ViewModel() {

    private val realm = Database.realm

    // To observe changes in the students that aren't signed in
    private val _unsignedStudents = MutableStateFlow<List<Student>>(emptyList())
    val unsignedStudents: StateFlow<List<Student>> get() = _unsignedStudents
    // Mutable state for the search query
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery
    // To observe the students list with the search query applied
    @OptIn(ExperimentalCoroutinesApi::class)
    val studentsFlow: Flow<List<Student>> = _searchQuery.flatMapLatest { query ->
        getStudentsFlow(query)
    }

    // Function to update the search query
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


    private val _signedStudents = MutableStateFlow<List<Student>>(emptyList())
    val signedStudents: StateFlow<List<Student>> get() = _signedStudents


    private val _paymentsFlow = MutableStateFlow<List<Family>>(emptyList())
    val paymentsFlow: StateFlow<List<Family>> get() = _paymentsFlow

    init {
        // Initialize a listener on the payments query
        observePayments()
    }

    private fun observePayments() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentTime = System.currentTimeMillis()

                // Query families with due payments
                val query = realm.query<Family>("paymentDate <= $0", currentTime)

                // Add a listener to the query
                query.asFlow().collect { result ->
                    val familiesWithDuePayments = result.list
                    _paymentsFlow.value = familiesWithDuePayments

                    // Log for debugging
                    Log.d("observePayments", "Payments updated: ${familiesWithDuePayments.size}")
                }
            } catch (e: Exception) {
                Log.e("observePayments", "Error observing payments", e)
            }
        }
    }

    fun confirmPurchaseButton(familyId: ObjectId) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                // Find the family object by ID
                val family = query(Family::class, "_id == $0", familyId).first().find()

                if (family != null) {
                    // Create a Calendar instance from the current paymentDate
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = family.paymentDate

                    // Add one month to the current date
                    calendar.add(Calendar.MONTH, 1)

                    // Update the paymentDate in the Realm object
                    family.paymentDate = calendar.timeInMillis
                } else {
                    Log.e("confirmPurchaseButton", "Family not found with ID: $familyId")
                }
            }
        }
    }

    fun sendReminderButton(familyID: ObjectId) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {

                val family = query(Family::class, "_id == $0", familyID).first().find()

                if (family != null) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = family.paymentDate

                    calendar.add(Calendar.DAY_OF_MONTH, 3)

                    family.paymentDate = calendar.timeInMillis
                }


            }



        }
    }

    fun IncorrectAmountPaidButton(familyID: ObjectId) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {

                val family = query(Family::class, "_id == $0", familyID).first().find()

                if (family != null) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = family.paymentDate

                    calendar.add(Calendar.DAY_OF_MONTH, 3)

                    family.paymentDate = calendar.timeInMillis
                }


            }



        }
    }





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
                    paymentAmount = family.paymentAmount.toString()
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


    fun deleteFamily(
        familyID: ObjectId
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                // Query the family itself
                val family = query<Family>("_id == $0", familyID).first().find()

                if (family != null) {
                    // Delete all students and parents associated with the family
                    delete(family.students)
                    delete(family.parents)

                    // Delete the family
                    delete(family)
                }
            }
        }
    }


    fun signInStudent(studentID: ObjectId) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                // Find the student to sign in
                val student = query<Student>("_id == $0", studentID).first().find()

                if (student != null) {
                    // Create a new sign-in event
                    val signInStudent = SignInEvent().apply {
                        day = getFormattedDay(Date()) // Format the current date
                        this.student = student
                        signIn = System.currentTimeMillis() - (System.currentTimeMillis() % 60000) // Current time in milliseconds
                        signOut = null // Not signed out yet
                        live = true // Event is active
                    }

                    copyToRealm(signInStudent)

                }
            }
        }
    }

    fun getUniqueDays(): List<String> {
        return realm.query<SignInEvent>()
            .distinct("day") // Fetch unique values of 'day'
            .find()
            .map { it.day } // Extract the 'day' property
    }


    fun getEventsForDay(day: String): List<SignInEvent> {
        return realm.query<SignInEvent>("day == $0", day)
            .find()
    }

    fun signOutStudent(studentID: ObjectId) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                // Find the active sign-in event for the student
                val signInEvent = query<SignInEvent>(
                    "student._id == $0 AND live == true",
                    studentID
                ).first().find()

                if (signInEvent != null) {
                    // Update the event with sign-out time and mark as inactive
                    signInEvent.signOut = System.currentTimeMillis() - (System.currentTimeMillis() % 60000)
                    signInEvent.live = false
                }
            }
        }
    }



    private fun updateFamily(
        familyID: ObjectId,
        updatedFamily: FamilyUI,
        updatedParents: List<ParentUI>,
        updatedStudents: List<StudentUI>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                // Query inside the write block to ensure the transaction is active
                val family = query<Family>("_id == $0", familyID).first().find()

                // Only modify the family if it was found
                if (family != null) {
                    // Perform the updates inside the write block
                    family.familyName = updatedFamily.familyName
                    family.familyEmail = updatedFamily.familyEmail
                    family.paymentDate = updatedFamily.paymentDate
                    family.paymentID = updatedFamily.paymentID
                    family.paymentAmount = updatedFamily.paymentAmount
                }

                // Step 2: Get the list of updated students, excluding students with null IDs
                val updatedStudentIds = updatedStudents.filter { it._id != null }.map { it._id }

                // Step 3: Find students that are in the family but not in the updated list
                val studentsToRemove = family?.students?.filter { it._id !in updatedStudentIds }

                // Step 4: Remove each student that is not in the updated list
                studentsToRemove?.forEach { student ->
                    // Remove the student from the family (if necessary)
                    family.students.remove(student)

                    delete(student)
                }


                val updatedParentIds = updatedParents.filter { it._id != null }.map { it._id }

                val parentsToRemove = family?.parents?.filter { it._id !in updatedParentIds }


                parentsToRemove?.forEach { parent ->
                    family.parents.remove(parent)
                    delete(parent)
                }


                updatedStudents.forEach { studentUI ->
                    if (studentUI._id != null) {
                        val student = query<Student>("_id == $0", studentUI._id).first().find()

                        if (student != null) {
                            student.studentName = studentUI.studentName
                            student.studentNumber = studentUI.studentNumber
                            student.birthdate = studentUI.birthdate
                            student.additionalInfo = studentUI.additionalInfo
                            student.canWalkAlone = studentUI.canWalkAlone
                        }
                    } else {
                        val newStudent = Student().apply {
                            studentName = studentUI.studentName
                            studentNumber = studentUI.studentNumber
                            birthdate = studentUI.birthdate
                            additionalInfo = studentUI.additionalInfo
                            canWalkAlone = studentUI.canWalkAlone
                        }

                        family?.students?.add(newStudent)
                    }

                }


                updatedParents.forEach { parentUI ->
                    if (parentUI._id != null) {
                        val parent = query<Parent>("_id == $0", parentUI._id).first().find()

                        if (parent != null) {
                            parent.parentName = parentUI.parentName
                            parent.parentNumber = parentUI.parentNumber
                        }
                    } else {
                        val newParent = Parent().apply {
                            parentName = parentUI.parentName
                            parentNumber = parentUI.parentNumber

                        }
                        family?.parents?.add(newParent)
                    }

                }



            }
        }
    }







    fun getStudentsFlow(searchQuery: String? = null): Flow<List<Student>> = flow {
            val query: RealmQuery<Student> = if (searchQuery.isNullOrBlank()) {
                // Fetch all students if no search query is provided
                realm.query(Student::class)
            } else {
                // Fetch students whose name contains the search query (case-insensitive)
                realm.query(Student::class, "studentName LIKE $0", "*${searchQuery}*")
            }

            // Emit the query results as a list
            emit(query.find())

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

    fun onSaveButtonPressed(
        family: FamilyUI,
        familyId: ObjectId,
        parents: MutableList<ParentUI>,
        students: MutableList<StudentUI>
    ) {
        updateFamily(familyId, family, parents, students)
    }

}
