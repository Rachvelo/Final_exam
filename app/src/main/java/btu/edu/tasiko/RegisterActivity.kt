package btu.edu.tasiko

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var repeatPasswordInput: EditText
    private lateinit var submitButton: MaterialButton
    private lateinit var registerButton: MaterialTextView
    private lateinit var goBack: ImageView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialize()

        goBack.setOnClickListener { onBackPressed() }
        submitButton.setOnClickListener { onSubmit() }
        registerButton.setOnClickListener { onBackPressed() }
    }

    private fun initialize() {
        goBack = findViewById(R.id.go_back)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        repeatPasswordInput = findViewById(R.id.repeatPasswordInput)
        submitButton = findViewById(R.id.submitButton)
        registerButton = findViewById(R.id.login_btn)

        reference = FirebaseDatabase.getInstance().reference
    }

    private fun onSubmit() {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val repeatPassword = repeatPasswordInput.text.toString()

        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(applicationContext, "შეავსეთ ყველა ველი!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != repeatPassword) {
            Toast.makeText(
                applicationContext,
                "პაროლები ერთმანეთს არ ემთხვევა!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, repeatPassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.signOut()
                    onBackPressed()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "შეცდომა:  ${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun uploadInfo() {

    }
}