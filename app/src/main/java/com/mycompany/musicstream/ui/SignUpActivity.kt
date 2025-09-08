package com.mycompany.musicstream.ui

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mycompany.musicstream.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)) {
                binding.emailEditText.error = "Invalid email"
                return@setOnClickListener
            }

            if (password.length < 6){
                binding.passwordEditText.error = "Length should be at least 6 characters"
                return@setOnClickListener
            }

            if (password != confirmPassword){
                binding.confirmPasswordEditText.error = "The passwords must match!"
                return@setOnClickListener
            }

            createAccount(email, password)
        }

        binding.goToLoginTextView.setOnClickListener {
            finish()
        }
    }

    private fun createAccount(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            setInProgress(false)
            Toast.makeText(applicationContext, "Account created successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            setInProgress(false)
            Toast.makeText(applicationContext, "Failed to create account: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun setInProgress(inProgress: Boolean){
        if (inProgress){
            binding.createAccountButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.createAccountButton.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}