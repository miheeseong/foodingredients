package com.mh.foodingredients.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private  final String TAG = "LoginActivity";

    TextInputEditText mEmailEditText;
    TextInputEditText mPwEditText;
    CheckBox mAutoLoginCheck;
    Button mLoginButton;
    SharedPreferences auto;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.emailEditText);
        mPwEditText = findViewById(R.id.pwEditText);
        mLoginButton = findViewById(R.id.loginButton);
        mAutoLoginCheck = findViewById(R.id.autoLoginCheck);

        //자동로그인 check
        auto = getSharedPreferences("auto", 0);
        editor = auto.edit();

        if (auto.getBoolean("mAutoLoginCheck", false)) {

            mEmailEditText.setText(auto.getString("email", ""));
            mPwEditText.setText(auto.getString("pw", ""));

            String email = mEmailEditText.getText().toString();
            String pw = mPwEditText.getText().toString();

            mAutoLoginCheck.setChecked(true);

            signIn(email, pw);

        }else {

            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = mEmailEditText.getText().toString();
                    String pw = mPwEditText.getText().toString();

                    Log.d("","auto333"+auto);

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)) {
                        Toast.makeText(LoginActivity.this, getString(R.string.msg_empty_email), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mAutoLoginCheck.isChecked()) {
                        editor.putString("email",email);
                        editor.putString("pw",pw);
                        editor.putBoolean("mAutoLoginCheck", true);

                        editor.commit();

                    }else {
                        editor.clear();
                        editor.commit();
                    }

                    signIn(email, pw);
                }
            });

        }

        //회원가입
        findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);

                }
            });
        }

    private void signIn(String email, String password) {
        Log.d(TAG, "singIn" + email);

        if (!validateForm()) {
            return;
        }

        FoodIngreInfoApplication.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            final FirebaseUser user = FoodIngreInfoApplication.mAuth.getCurrentUser();

                            //user데이터 가져오기
                            FoodIngreInfoApplication.mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                            UserItem item = postSnapshot.getValue(UserItem.class);
                                            if (!TextUtils.isEmpty(item.uid) && item.uid.equals(user.getUid())) {
                                                FoodIngreInfoApplication.mUserItem = item;

                                                //메인액티비티호출
                                                Intent intent = new Intent(LoginActivity.this, MainFragmentActivity.class);
                                                startActivity(intent);

                                                finish();
                                                break;
                                            }
                                        }
                                    }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, getString(R.string.msg_fail_login),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError("Required.");
            valid = false;
        } else {
            mEmailEditText.setError(null);
        }

        String password = mPwEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPwEditText.setError("Required.");
            valid = false;
        } else {
            mPwEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
