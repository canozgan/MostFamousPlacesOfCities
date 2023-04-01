package com.canozgan.mostfamousplacesofcities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.canozgan.mostfamousplacesofcities.databinding.ActivityAuthenticationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity {
    ActivityAuthenticationBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void signIn(View view){
        binding.signInButton.setEnabled(false);
        binding.signUpButton.setEnabled(false);
        String email=binding.userEmailText.getText().toString();
        String password =binding.userPasswordText.getText().toString();
        if(email.matches("") || password.matches("")){
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_LONG).show();
            binding.signInButton.setEnabled(true);
            binding.signUpButton.setEnabled(true);
        }
        else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(AuthenticationActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuthenticationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    binding.signInButton.setEnabled(true);
                    binding.signUpButton.setEnabled(true);
                }
            });
        }
    }
    public void signUp(View view){
        binding.signInButton.setEnabled(false);
        binding.signUpButton.setEnabled(false);
        String email=binding.userEmailText.getText().toString();
        String password =binding.userPasswordText.getText().toString();
        if(email.matches("") || password.matches("")){
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_LONG).show();
            binding.signInButton.setEnabled(true);
            binding.signUpButton.setEnabled(true);
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(AuthenticationActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AuthenticationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    binding.signInButton.setEnabled(true);
                    binding.signUpButton.setEnabled(true);
                }
            });
        }
    }
}