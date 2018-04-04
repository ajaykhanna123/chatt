package com.example.ajaykhanna.chatt;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputLayout mStatus;
    private Button btnSaveStatus;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser currentUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mToolbar=(Toolbar)findViewById(R.id.settingStatusToolbar);
        mStatus=(TextInputLayout)findViewById(R.id.edtStatus);
        btnSaveStatus=(Button)findViewById(R.id.btnSaveStatus);

        String statusValue=getIntent().getStringExtra("statusValue");
        mProgress=new ProgressDialog(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Status Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String uId=currentUser.getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
        mStatus.getEditText().setText(statusValue);
        btnSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.setTitle("Updating status");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();
                String status=mStatus.getEditText().getText().toString();

                mDatabaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"status Updated",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error Ocurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
