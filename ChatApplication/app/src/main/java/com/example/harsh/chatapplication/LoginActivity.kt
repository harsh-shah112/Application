package com.example.harsh.chatapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var user: String
    private lateinit var loginLayout: View
    private lateinit var dialog: AlertDialog

    private data class UserData(val id: Int, val name: String, val token: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginLayout = findViewById(R.id.login)

    loginUserButton.setOnClickListener{
        user = userName.text.toString()
        loginUser(user)
    }
        /*loginUserButton.setOnClickListener {

        }*/

        closeApp.setOnClickListener {
            finish()
        }
    }

    private fun loginUser(user: String) {

        if (validateUserName(user)) {
            Thread(Runnable {

                this@LoginActivity.runOnUiThread {
                    dialog = Utils().createDialog(this,layoutInflater,"Logging in...")
                    dialog.show()
                }

                val response = ChatService().loginUser(user)

                when (response.responseCode) {
                    200 -> {
                        this@LoginActivity.runOnUiThread { dialog.dismiss() }
                        val user = Gson().fromJson<UserData>(response.responseMessage, UserData::class.java)

                        var userIntent = Intent(this, UserActivity::class.java)
                        userIntent.putExtra("id",user.id)
                        userIntent.putExtra("name",user.name)
                        userIntent.putExtra("token",user.token)
                        startActivity(userIntent)
                        finish()
                    }
                    else -> {
                        this@LoginActivity.runOnUiThread { dialog.dismiss() }
                        Snackbar.make(loginLayout, response.responseMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
            }).start()
        } else {
            Snackbar.make(loginLayout, "Empty User Name or Removes spaces from User Name", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun validateUserName(user: String): Boolean {
        if (user.isEmpty() || user.contains(" ")) {
            return false
        }
        return true
    }
}
