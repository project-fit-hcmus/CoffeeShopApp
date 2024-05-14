package com.example.myapp.View

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.myapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivtiy: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var txtUsername: TextInputEditText
    private lateinit var  txtPassword: TextInputEditText
    private lateinit var txtRepeatPassword: TextInputEditText
    private lateinit var  txtMail: TextInputEditText
    private lateinit var  btnRegister: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.register_screen)

        auth = FirebaseAuth.getInstance()
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword)
        txtMail = findViewById(R.id.txtEmail)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(View.OnClickListener {
            fun onClick(view: View){
                val username:String = txtUsername.toString()
                val password:String = txtPassword.toString()
                val mail:String = txtMail.toString()
                val repeatPass:String = txtRepeatPassword.toString()
                if(username.isEmpty()){
                    txtUsername.setError("Username can not be empty")
                }
                else if(password.isEmpty()){
                    txtPassword.setError("Password can not be empty")
                }
                else if(mail.isEmpty()){
                    txtMail.setError("Mail can not be empty")
                }
                else if(!repeatPass.equals(password)){
                    txtRepeatPassword.setError("Password is not compability")
                }else{
                    auth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(
                        OnCompleteListener<AuthResult> {
                            fun onComplete(@NonNull task: Task<AuthResult>) {
                                Toast.makeText(this,"Sign in Success", Toast.LENGTH_LONG).show()
                                val intent = Intent(this,LoginActivity::class.java)
                                startActivity(intent)
                            }
                        })
                }
            }
        })

    }


}

//reference here: https://viblo.asia/p/su-dung-firebase-trong-viec-dang-nhap-va-xac-thuc-dang-ky-thanh-vien-MLzGOxlLepq
