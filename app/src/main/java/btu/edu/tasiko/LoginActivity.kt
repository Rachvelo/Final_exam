package btu.edu.tasiko

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var submitButton: MaterialButton
    private lateinit var registerButton: MaterialTextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()

        submitButton.setOnClickListener {
            signIn()
        }

        registerButton.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initialize() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        submitButton = findViewById(R.id.submitButton)
        registerButton = findViewById(R.id.register_btn)
        auth = FirebaseAuth.getInstance()
    }

    private fun signIn() {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            return Toast.makeText(
                baseContext, "გთხოვთ შეავსეთ ყველა ველი",
                Toast.LENGTH_LONG
            ).show()
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    baseContext, "შეცდომა:  ${it.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}