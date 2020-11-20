package com.schedulemate.schedulemate_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPW, editTextPWConfirm, editTextNickName;
    private Spinner univSpinner;
    private Spinner majorSpinner;
    private Button button;
    private List<String> univList = new ArrayList<>();
    private Map<String, String> majorMap = new HashMap<>();
    private List<String> majorList = new ArrayList<>();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Context context = this;
        Activity activity = this;

        ArrayAdapter<String> univAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, univList);
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, majorList);

        univSpinner = findViewById(R.id.spinnerUniv);
        majorSpinner = findViewById(R.id.spinnerMajor);

        univSpinner.setAdapter(univAdapter);
        majorSpinner.setAdapter(majorAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("info").child("university");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                univList.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    univList.add(child.getKey());
                }
                univAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        univSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String univ = univList.get(position);
                DatabaseReference univRef = ref.child(univ).child("major");
                univRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        majorMap.clear();
                        majorList.clear();
                        for (DataSnapshot child : snapshot.getChildren()){
                            majorMap.put(child.getValue().toString(), child.getKey());
                        }
                        majorList.addAll(majorMap.keySet());
                        majorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPW = findViewById(R.id.editTextPW);
        editTextPWConfirm = findViewById(R.id.editTextPWConfirm);
        editTextNickName = findViewById(R.id.editTextNickName);

        auth = FirebaseAuth.getInstance();

        button = findViewById(R.id.buttonSignUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                //값 없는 칸은 빨간색으로, 있는 칸은 회색으로
                //이름
                if(editTextName.getText().toString().trim().equals("")){
                    editTextName.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    flag = false;
                }
                else{
                    editTextName.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border));
                }
                //이메일
                if(editTextEmail.getText().toString().trim().equals("")){
                    editTextEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    flag = false;
                }
                else{
                    editTextEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border));
                }
                //비밀번호
                if(editTextPW.getText().toString().trim().equals("")){
                    editTextPW.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    flag = false;
                }
                else{
                    editTextPW.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border));
                }
                //비밀번호 확인
                if(editTextPWConfirm.getText().toString().trim().equals("")){
                    editTextPWConfirm.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    flag = false;
                }
                else{
                    editTextPWConfirm.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border));
                }
                //닉네임
                if(editTextNickName.getText().toString().trim().equals("")){
                    editTextNickName.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    flag = false;
                }
                else{
                    editTextNickName.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border));
                }

                //비밀번호 일치 검사
                if(!editTextPW.getText().toString().equals(editTextPWConfirm.getText().toString())){
                    editTextPW.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    editTextPWConfirm.setBackground(ContextCompat.getDrawable(context, R.drawable.edit_text_border_red));
                    flag = false;
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

                if (flag){
                    auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPW.getText().toString())
                            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SIGNUP", "createUserWithEmail:success");

                                        FirebaseUser user = auth.getCurrentUser();
                                        DatabaseReference userRef = database.getReference("user").child(user.getUid());
                                        userRef.child("name").setValue(editTextName.getText().toString());
                                        userRef.child("nickName").setValue(editTextNickName.getText().toString());
                                        userRef.child("university").setValue(univList.get(univSpinner.getSelectedItemPosition()));
                                        userRef.child("major").setValue(majorList.get(majorSpinner.getSelectedItemPosition()));

                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("SIGNUP", "createUserWithEmail:failure" + ((FirebaseAuthException)task.getException()).getErrorCode());

                                        switch (((FirebaseAuthException)task.getException()).getErrorCode()){
                                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                                Toast.makeText(context, "중복된 이메일입니다.", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "ERROR_WEAK_PASSWORD":
                                                Toast.makeText(context, "비밀번호 길이가 6자 이하입니다.", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(context, "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                        updateUI(null);
                                    }
                            }
                    });
                }
            }
        });
    }

    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"회원가입되었습니다.",Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }
}