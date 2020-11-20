package com.schedulemate.schedulemate_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearLayoutId, linearLayoutPW;
    private EditText editTextId, editTextPW;
    private Button buttonLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;
        Activity activity = this;

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        TextView signUpLink = findViewById(R.id.textViewLinkSignUp);

        Linkify.TransformFilter transformFilter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Pattern pattern = Pattern.compile("회원가입");

        Linkify.addLinks(signUpLink, pattern, "signup://load", null, transformFilter);

        linearLayoutId = findViewById(R.id.linearLayoutId);
        linearLayoutPW = findViewById(R.id.linearLayoutPW);
        editTextId = findViewById(R.id.editTextId);
        editTextPW = findViewById(R.id.editTextPW);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                //값 없는 칸은 빨간색으로, 있는 칸은 검은색으로
                //id
                if(editTextId.getText().toString().trim().equals("")){
                    linearLayoutId.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_layout_border_red));
                    flag = false;
                }
                else{
                    linearLayoutId.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_layout_border));
                }
                //pw
                if(editTextPW.getText().toString().trim().equals("")){
                    linearLayoutPW.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_layout_border_red));
                    flag = false;
                }
                else{
                    linearLayoutPW.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_layout_border));
                }

                if(flag){
                    auth.signInWithEmailAndPassword(editTextId.getText().toString(), editTextPW.getText().toString())
                            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignIn", "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignIn", "signInWithEmail:failure", task.getException());
                                updateUI(null);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"로그인되었습니다.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, TimetableActivity.class));
            this.finish();

        }else {
            Toast.makeText(this,"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
        }
    }
}