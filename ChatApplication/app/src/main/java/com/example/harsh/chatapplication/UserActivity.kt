package com.example.harsh.chatapplication

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private var userId: Int = 0
    private lateinit var dialog: AlertDialog
    private lateinit var usersList: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userBundle = intent.extras

        if(userBundle != null){
            title = "Welcome " + userBundle.getString("name") + ","
            userId = userBundle.getInt("id")
            getAllUsers(userBundle.getString("token"))
        }

        userList.setOnItemClickListener {
            adapterView, view, position, id ->


        }
    }

    private fun getAllUsers(token: String){
        Thread(Runnable {


            this@UserActivity.runOnUiThread {
                dialog = Utils().createDialog(this,layoutInflater,"Loading Users...")
                dialog.show()
            }


            when (users.responseCode) {
                200 -> {
                    this@UserActivity.runOnUiThread {
                        usersList = users.responseMessage
                        userList.adapter = UserListAdapter(this, usersList)
                        dialog.dismiss()
                    }
                }
                else -> {
                    dialog.dismiss()
                    this@UserActivity.runOnUiThread {
                        Toast.makeText(applicationContext, "Fetching of users failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }).start()
    }
}
