package com.example.roadvisor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.emailLogin)
        val passwordEditText = findViewById<EditText>(R.id.passwordLogin)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("email", email)
            jsonObject.put("password", password)

            val client = OkHttpClient()

            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("http://roadvisor.eastus.cloudapp.azure.com:8000/api/v1/accounts/login/")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle the failure
                    Toast.makeText(applicationContext, "Login failed!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    // Handle the response data here
                    val intent = Intent(this@MainActivity, HomescreenActivity::class.java)
                    // Start the activity using the intent
                    this@MainActivity.startActivity(intent)
                }
            })
        }

        val goToRegisterButton = findViewById<Button>(R.id.registerDirectButton)
        goToRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}