package com.example.ajaykhanna.chatt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout edtRegDisplayName;
    private TextInputLayout edtRegEmail;
    private TextInputLayout edtRegPassword;
    private Button btnCreateAccount;
    private FirebaseAuth mAuth;
    private Toolbar mToolBar;
    private ProgressDialog mRegProgress;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolBar=(Toolbar)findViewById(R.id.regToolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Create Account ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress=new ProgressDialog(this);

        edtRegDisplayName=(TextInputLayout)findViewById(R.id.edtRegName);
        edtRegEmail=(TextInputLayout)findViewById(R.id.edtLoginEmail);
        edtRegPassword=(TextInputLayout)findViewById(R.id.edtRegPassword);
        btnCreateAccount=(Button) findViewById(R.id.btnRegAccount);
        mAuth=FirebaseAuth.getInstance();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName=edtRegDisplayName.getEditText().getText().toString();
                String email=edtRegEmail.getEditText().getText().toString();
                String password=edtRegPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
                {
                    mRegProgress.setTitle("Registering user");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(displayName,email,password);
                }


            }
        });
    }

    private void register_user(final String displayName, String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
                    String uId=currentUser.getUid();
                    mDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
                    HashMap<String,String> usersMap=new HashMap<>();
                    usersMap.put("name",displayName);
                    usersMap.put("status","hi there! I'm using Chatt app");
                    usersMap.put("image","default");
                    usersMap.put("thumb_image","default");

                    mDatabaseReference.setValue(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mRegProgress.dismiss();
                                Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });


                }
                else
                {
                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this,"Cannot sign in. please check the form and try again"
                            ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
