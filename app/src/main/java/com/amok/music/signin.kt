package com.amok.music

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class signin : AppCompatActivity() {

    private lateinit var editTextPassword : EditText
    private lateinit var editTextLogin : EditText
    private lateinit var buttonRegistration : AppCompatButton

    private val PREFERENCES_FILE = "com.amok.music.PREFERENCES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextPassword = findViewById(R.id.password)
        editTextLogin = findViewById(R.id.login)
        buttonRegistration = findViewById(R.id.enter)
        val forbiddenCharsPattern = "[^a-zA-Zа-яА-Я0-9]".toRegex()

        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (forbiddenCharsPattern.containsMatchIn(it)) {
                        editTextPassword.error = "Запрещены специальные символы. Разрешены только буквы и цифры."
                    } else {
                        editTextPassword.error = null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buttonRegistration.setOnClickListener {
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()
            if (login.isEmpty() || password.isEmpty()) {
                showSnackbar("Логин и пароль не должны быть пустыми")
                return@setOnClickListener
            }
            if (forbiddenCharsPattern.containsMatchIn(password)) {
                //showSnackbar("Запрещены специальные символы в пароле")
                return@setOnClickListener
            }
            if (password.length < 4) {
                showSnackbar("Пароль должен содержать минимум 4 символа")
                return@setOnClickListener
            }
            val sharedPreferences = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
            if (sharedPreferences.contains(login)) {
                showSnackbar("Такой логин уже существует")
                return@setOnClickListener
            }
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(login, password)
            editor.apply()

            showSnackbar("Регистрация успешна!")
        }
    }
    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)

        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_VERTICAL
        snackbarView.layoutParams = params

        snackbar.show()
    }
}