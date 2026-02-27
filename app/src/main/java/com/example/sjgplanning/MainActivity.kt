package com.example.sjgplanning

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

        val editUserid = findViewById<EditText>(R.id.editUserid)
        val editUname = findViewById<EditText>(R.id.editUname)
        val editDept = findViewById<EditText>(R.id.editDept)
        val editPw = findViewById<EditText>(R.id.editPw)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnClose = findViewById<Button>(R.id.btnClose)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val textStatus = findViewById<TextView>(R.id.textStatus)

        btnSave.setOnClickListener {
            val userid = editUserid.text.toString().trim()
            val uname = editUname.text.toString().trim()
            val dept = editDept.text.toString().trim()
            val pw = editPw.text.toString().trim()

            if (userid.isEmpty() || uname.isEmpty() || dept.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            textStatus.text = "저장 중..."
            thread {
                try {
                    val baseUrl = "http://172.17.36.15:3000"
                    val client = OkHttpClient()
                    val payload = JSONObject().apply {
                        put("userid", userid)
                        put("uname", uname)
                        put("dept", dept)
                        put("pw", pw)
                    }.toString()
                    val body = payload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                    val req = Request.Builder().url("$baseUrl/api/users").post(body).build()
                    val resp = client.newCall(req).execute()
                    val success = resp.isSuccessful
                    runOnUiThread {
                        if (success) {
                            textStatus.text = "저장 완료"
                            Toast.makeText(this, "저장 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            textStatus.text = "저장 실패: ${resp.code}"
                            Toast.makeText(this, "저장 실패: ${resp.code}", Toast.LENGTH_SHORT).show()
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

        btnLogin.setOnClickListener {
            val userid = editUserid.text.toString().trim()
            if (userid.isEmpty()) {
                Toast.makeText(this, "사용자 ID를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            textStatus.text = "로그인 확인 중..."
            thread {
                try {
                    val baseUrl = "http://172.17.36.15:3000"
                    val client = OkHttpClient()
                    val req = Request.Builder()
                        .url(baseUrl + "/api/users/exists?userid=" + java.net.URLEncoder.encode(userid, "utf-8"))
                        .get()
                        .build()
                    val resp = client.newCall(req).execute()
                    val body = resp.body?.string() ?: ""
                    val exists = try {
                        JSONObject(body).optBoolean("exists", false)
                    } catch (e: Exception) {
                        false
                    }
                    runOnUiThread {
                        if (exists) {
                            val intent = android.content.Intent(this, WelcomeActivity::class.java)
                            intent.putExtra("name", userid)
                            startActivity(intent)
                        } else {
                            textStatus.text = "사용자 미등록"
                            Toast.makeText(this, "사용자 미등록", Toast.LENGTH_SHORT).show()
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

        btnClose.setOnClickListener {
            finish()
        }
    }
}
