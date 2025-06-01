package com.example.managementuser.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.managementuser.R
import com.example.managementuser.data.DataBaseApplication
import com.example.managementuser.data.user.UserRepository
import com.example.managementuser.helper.PrefsHelper
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {
    private lateinit var edtUsername: EditText
    private lateinit var edtFirstName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "My profile"

        edtUsername = findViewById(R.id.edtUsername)
        edtFirstName = findViewById(R.id.edtFirstName)
        edtLastName = findViewById(R.id.edtLastName)
        radioGroup = findViewById(R.id.radioGenDer)
        btnSave = findViewById(R.id.btnSave)
        btnLogout = findViewById(R.id.btnLogout)

        val prefs = PrefsHelper(this)
        val userLogin = prefs.getUser()

        val dao = DataBaseApplication.getInstance(this).userDao()
        val repository = UserRepository(dao, prefs)
        lifecycleScope.launch {
            val user = dao.getUserById(userLogin?.id ?: 0)
            user?.let { userLogin ->
                {
                    // Handle gender selection
                    when (userLogin.gender?.lowercase()) {
                        "female" -> radioGroup.check(R.id.radio_female)
                        "male" -> radioGroup.check(R.id.radio_male)
                        "other" -> radioGroup.check(R.id.radio_other)
                    }

                    btnSave.setOnClickListener {
                        val selectedGender =
                            findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
                        val updatedUser = userLogin.copy(
                            userName = edtUsername.text.toString(),
                            firstName = edtFirstName.text.toString(),
                            lastName = edtLastName.text.toString(),
                            gender = selectedGender
                        )
                        lifecycleScope.launch {
                            dao.updateUser(updatedUser)
                            finish()
                        }
                    }

                    btnLogout.setOnClickListener {
                        lifecycleScope.launch {
                            repository.logout()
                            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
