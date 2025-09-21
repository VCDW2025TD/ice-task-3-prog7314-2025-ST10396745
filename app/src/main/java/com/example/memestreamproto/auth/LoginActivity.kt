package com.example.memestreamproto.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.memestreamproto.MainActivity
import com.example.memestreamproto.R
import com.example.memestreamproto.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var gsc: GoogleSignInClient

    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val cred = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(cred).addOnCompleteListener { t ->
                if (t.isSuccessful) {
                    if (canUseBiometrics()) {
                        promptBiometric()
                    } else {
                        goHome()
                    }
                } else {
                    Log.e("Login", "Firebase sign in failed", t.exception)
                    Toast.makeText(this, "Auth failed", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("Login", "Google sign in failed", e)
            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        // already signed in
        auth.currentUser?.let {
            if (canUseBiometrics()) {
                promptBiometric()
                return
            } else {
                goHome()
                return
            }
        }

        binding.googleSignInButton.setOnClickListener {
            googleLauncher.launch(gsc.signInIntent)
        }
    }

    private fun goHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun canUseBiometrics(): Boolean {
        val mgr = BiometricManager.from(this)
        val res = mgr.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        return res == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun promptBiometric() {
        val executor = ContextCompat.getMainExecutor(this)
        val prompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    goHome()
                }
                override fun onAuthenticationError(code: Int, msg: CharSequence) {
                    super.onAuthenticationError(code, msg)
                    Toast.makeText(this@LoginActivity, "Biometric cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock MemeStream")
            .setSubtitle("Use fingerprint or device PIN")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(info)
    }
}
