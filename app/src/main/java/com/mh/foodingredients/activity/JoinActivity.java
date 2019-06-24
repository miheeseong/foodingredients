package com.mh.foodingredients.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mh.foodingredients.FoodIngreInfoApplication;
import com.mh.foodingredients.R;
import com.mh.foodingredients.model.UserItem;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "JoinActivity";

    EditText mNicknameEidtText;
    EditText mEmailEditText;
    EditText mPwEditText;
    EditText mPhoneNumEditText;
    Button mEmailCheckButton;

    FirebaseUser user = FoodIngreInfoApplication.mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mNicknameEidtText = findViewById(R.id.nicknameEditText);
        mEmailEditText =  findViewById(R.id.emailEditText);
        mPwEditText = findViewById(R.id.pwEditText);
        mPhoneNumEditText = findViewById(R.id.phoneNumEditText);
        mEmailCheckButton = findViewById(R.id.emailCheckButton);

        findViewById(R.id.joinButton).setOnClickListener(this);
        findViewById(R.id.cancelButton).setOnClickListener(this);

        //Email중복체크
        mEmailCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mEmailEditText.getText().toString())) {
                    Toast.makeText(JoinActivity.this, "이메일주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                FoodIngreInfoApplication.mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String sEmail = mEmailEditText.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserItem items = snapshot.getValue(UserItem.class);
                            if (sEmail.equals(items.email)) {
                                Toast.makeText(JoinActivity.this, "이미 가입된 이메일주소입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                Toast.makeText(JoinActivity.this, "사용 가능한 이메일주소입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void createAccount(final String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        FoodIngreInfoApplication.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            UserItem item = new UserItem();
                            item.uid = user.getUid();
                            item.email = email;
                            item.nickname = mNicknameEidtText.getText().toString();
                            item.phoneNumber = mPhoneNumEditText.getText().toString();

                            Toast.makeText(JoinActivity.this, user.getEmail()+"님"+getString(R.string.msg_success_join),Toast.LENGTH_SHORT).show();

                            FoodIngreInfoApplication.mDatabase.child("users").child(item.uid).setValue(item);
                            FoodIngreInfoApplication.mUserItem = item;

                            //메인액티비티호출
                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                            startActivity(intent);

                            finish();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(JoinActivity.this, getString(R.string.msg_fail_join), Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailEditText.getText().toString();
        Log.d(TAG, email);

        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError("Required.");
            valid = false;
        } else {
            mEmailEditText.setError(null);
        }

        String password = mPwEditText.getText().toString();
        Log.d(TAG, password);
        if (TextUtils.isEmpty(password)) {
            mPwEditText.setError("Required.");
            valid = false;
        } else {
            mPwEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joinButton :
                //파이어베이스 회원가입
                createAccount(mEmailEditText.getText().toString(), mPwEditText.getText().toString());
                break;

            case R.id.cancelButton :
                //화면닫기
                finish();
                break;
        }
    }
}
