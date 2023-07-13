package com.clinton.rescuelocatorapp.ui.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.clinton.rescuelocatorapp.R
import com.clinton.rescuelocatorapp.databinding.ActivitySigninBinding
import com.clinton.rescuelocatorapp.models.User
import com.clinton.rescuelocatorapp.ui.fragments.ForgotFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "SignInActivity"

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignIn.setOnClickListener {

            binding.progressBarSignIn.isVisible = true

            when {
                binding.editTextEmailSignIn.text.toString().isNullOrEmpty() -> {
                    binding.editTextEmailSignIn.error = "Required Email"
                    return@setOnClickListener
                }
                binding.editTextTextPasswordSignIn.text.toString().isNullOrEmpty() -> {
                    binding.editTextTextPasswordSignIn.error = "Required Phone Number"
                    return@setOnClickListener
                }
                else -> {
                    val email = binding.editTextEmailSignIn.text.toString()
                    val password = binding.editTextTextPasswordSignIn.text.toString()


                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (firebaseAuth.currentUser!!.isEmailVerified){
                                    binding.progressBarSignIn.isVisible = false
                                    startActivity(Intent(this, MainActivity::class.java))
                                    Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else{
                                    binding.progressBarSignIn.isVisible = false
                                    Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                //Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                                binding.progressBarSignIn.isVisible = false
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBarSignIn.isVisible = false
                        }

                }
            }
        }
        binding.textViewDontHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.textViewForgotPassword.setOnClickListener {
            ForgotFragment().show(supportFragmentManager,"forgotFragment")
        }
    }
}