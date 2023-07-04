package com.example.finaluridatabase

import android.os.Bundle
import android.widget.Button
import com.example.finaluridatabase.User
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User

data class User(
    val name: String = "",
    val age: Int = 0
)

class EditUserActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var userId: String

    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("users")

        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        saveButton = findViewById(R.id.saveButton)

        userId = intent.getStringExtra("userId") ?: ""
        if (userId.isNotEmpty()) {
            saveButton.text = "Update"
            loadUserData()
        }

        saveButton.setOnClickListener {
            saveUser()
        }
    }

    private fun loadUserData() {
        userRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(com.example.finaluridatabase.User::class.java)
                user?.let {
                    nameEditText.setText(it.name)
                    ageEditText.setText(it.age.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок при чтении из Firebase
            }
        })
    }

    private fun saveUser() {
        val name = nameEditText.text.toString().trim()
        val age = ageEditText.text.toString().toIntOrNull()

        if (name.isNotEmpty() && age != null) {
            if (userId.isNotEmpty()) {
                val updatedUser = User(userId, name, age)
                userRef.child(userId).setValue(updatedUser)
            } else {
                val newUserRef = userRef.push()
                val newUser = User(newUserRef.key!!, name, age)
                newUserRef.setValue(newUser)
            }

            finish()
        }
    }
}
