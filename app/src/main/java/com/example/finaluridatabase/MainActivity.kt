import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.finaluridatabase.EditUserActivity
import com.example.finaluridatabase.UserAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User

class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var userList: MutableList<User>
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("users")

        userList = mutableListOf()
        userAdapter = UserAdapter(this, userList)

        val userListView: ListView = findViewById(R.id.userListView)
        userListView.adapter = userAdapter

        userListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedUser = userList[position]
                val intent = Intent(this, EditUserActivity::class.java)
                intent.putExtra("userId", selectedUser.id)
                startActivity(intent)
            }

        userListView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                val selectedUser = userList[position]
                userRef.child(selectedUser.id).removeValue()
                true
            }

        val addUserButton: FloatingActionButton = findViewById(R.id.addUserButton)
        addUserButton.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        it.id = userSnapshot.key.toString()
                        userList.add(it)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок при чтении из Firebase
            }
        })
    }
}
