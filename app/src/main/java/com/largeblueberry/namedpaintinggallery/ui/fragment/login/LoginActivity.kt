package com.largeblueberry.namedpaintinggallery.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.largeblueberry.namedpaintinggallery.R
import com.largeblueberry.namedpaintinggallery.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // UI 요소 연결
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.btnlogin)
        val googleLoginButton: Button = findViewById(R.id.btngooglelogin)
        val forgotPasswordText: TextView = findViewById(R.id.tv_forgot_password)
        val registerText: TextView = findViewById(R.id.tv_register)
        val progressBar: ProgressBar = findViewById(R.id.loading)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ProgressBar 표시
            progressBar.visibility = View.VISIBLE

            // 로그인 처리 (예: Firebase Authentication)
            loginUser(email, password) {
                progressBar.visibility = View.GONE
                if (it) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // 다음 액티비티로 이동
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "로그인 실패. 이메일 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 구글 로그인 버튼 클릭 이벤트
        googleLoginButton.setOnClickListener {
            Toast.makeText(this, "구글 로그인 기능을 구현하세요.", Toast.LENGTH_SHORT).show()
            // 구글 로그인 로직 추가
        }

        // 비밀번호 찾기 클릭 이벤트 추후 추가
        

        // 회원가입 클릭 이벤트 추후 추가
   
    }

    private fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        // 예시: Firebase Authentication (비동기)
        // FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        //     .addOnCompleteListener { task ->
        //         callback(task.isSuccessful)
        //     }

        // 테스트용 로그인 성공 콜백
        callback(email == "test@example.com" && password == "password")
    }
}