package com.example.roadvisor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val url = "http://roadvisor.eastus.cloudapp.azure.com:8000/api/v1/accounts/register/"

        val emailEditText = findViewById<EditText>(R.id.emailRegister)
        val firstNameEditText = findViewById<EditText>(R.id.firstName)
        val lastNameEditText = findViewById<EditText>(R.id.lastName)
        val passwordEditText = findViewById<EditText>(R.id.passwordRegister)
        val passwordVerifyEditText = findViewById<EditText>(R.id.passwordRegisterVerify)

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val passwordVerify = passwordVerifyEditText.text.toString()

            val text1 = password.trim()
            val text2 = passwordVerify.trim()

            if (text1 != text2) {
                Toast.makeText(applicationContext, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            } else {
                val jsonObject = JSONObject()
                jsonObject.put("email", email)
                jsonObject.put("password", password)
                jsonObject.put("last_name", lastName)
                jsonObject.put("first_name", firstName)

                val json = jsonObject.toString()

                val request = Request.Builder()
                    .url(url)
                    .post(json.toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        Log.d(TAG, "Response: ${response.body?.string()}")
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Error: ${e.message}")
                    }
                })
                Toast.makeText(applicationContext, "Registration Successful$json", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
