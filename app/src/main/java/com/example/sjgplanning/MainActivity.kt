package com.example.sjgplanning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editUserid = findViewById<EditText>(R.id.loginUserid)
        val editPw = findViewById<EditText>(R.id.loginPw)
        val btnLogin = findViewById<Button>(R.id.btnLoginSubmit)
        val btnClose = findViewById<Button>(R.id.btnClose)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val textStatus = findViewById<TextView>(R.id.loginStatus)

        btnLogin.setOnClickListener {
            val userid = editUserid.text.toString().trim()
            val pw = editPw.text.toString().trim()
            if (userid.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            textStatus.text = "로그인 중..."
            thread {
                try {
                    val baseUrl = getString(R.string.base_url)
                    val client = OkHttpClient()
                    val payload = JSONObject().apply {
                        put("userid", userid)
                        put("pw", pw)
                    }.toString()
                    val body = payload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                    val req = Request.Builder().url("$baseUrl/api/login").post(body).build()
                    val resp = client.newCall(req).execute()
                    val bodyText = resp.body?.string() ?: ""
                    val json = try { JSONObject(bodyText) } catch (e: Exception) { JSONObject() }
                    val ok = resp.isSuccessful && json.optBoolean("ok", false)
                    val match = json.optBoolean("match", false)
                    runOnUiThread {
                        if (ok && match) {
                            editPw.setText("")
                            val intent = Intent(this, WelcomeActivity::class.java)
                            intent.putExtra("name", userid)
                            startActivity(intent)
                        } else if (ok && !match) {
                            textStatus.text = "암호가 일치하지 않습니다"
                            Toast.makeText(this, "암호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            textStatus.text = "로그인 실패"
                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        textStatus.text = "예외 발생: ${e.message}"
                        Toast.makeText(this, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }
}
