package com.aodev.cmchat.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.aodev.cmchat.auth.LoginActivity
import com.aodev.cmchat.R
import com.aodev.cmchat.adapter.LatestMessage
import com.aodev.cmchat.adapter.UserItem
import com.aodev.cmchat.auth.RegisterActivity
import com.aodev.cmchat.data.ChatMessage
import com.aodev.cmchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

private const val TAG = "LatestMessagesActivity"

class LatestMessagesActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessageMap = HashMap<String, ChatMessage>()

    companion object {
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        recyclerview_latest_message.adapter = adapter
        recyclerview_latest_message.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val row = item as LatestMessage

            val intent = Intent(view.context,ChatLogActivity::class.java)
            intent.putExtra(NewMessageActivity.USER_KEY,row.user)
            startActivity(intent)
            Log.e(TAG,"your clicked item")
        }
        listenLatestMessage()
        fetchCurrentUser()
        verifyUserIsLoggedIn()
    }

    private fun listenLatestMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                if (chatMessage != null) {
                    latestMessageMap[snapshot.key!!] = chatMessage
                    refreshRecyclerViewMessage()
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                if (chatMessage != null) {
                    latestMessageMap[snapshot.key!!] = chatMessage
                    refreshRecyclerViewMessage()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun refreshRecyclerViewMessage() {
        adapter.clear()
        latestMessageMap.values.onEach {
            adapter.add(LatestMessage(it,applicationContext))
        }
    }


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

        })
    }

    private fun verifyUserIsLoggedIn() {
        val uuid = FirebaseAuth.getInstance().uid
        if (uuid.isNullOrEmpty()) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, item.toString())
        when (item.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}