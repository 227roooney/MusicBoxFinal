package com.example.musicbox1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.musicbox1.MainActivity;
import com.example.musicbox1.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private TextView yaTienes_Cuenta;
    private FrameLayout frameLayout;

    private EditText userName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpButton;
    private ProgressBar signUpProgressBar;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        yaTienes_Cuenta = view.findViewById(R.id.yaTienes_Cuenta);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);

        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpButton);
        signUpProgressBar = view.findViewById(R.id.signUpProgressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yaTienes_Cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });


        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.setEnabled(false);
                signUpButton.setTextColor(getResources().getColor(R.color.white));
                signUpWithFirebase();
            }
        });
    }

    private void signUpWithFirebase() {
        if(email.getText().toString().matches("[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+")){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                signUpProgressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()){
                            Map<String, Object> user = new HashMap<>();
                            user.put("userName", userName.getText().toString());
                            user.put("emailId", email.getText().toString());
                            db.collection("users")

                                    .document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            getActivity().startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            signUpButton.setEnabled(true);
                                            signUpButton.setTextColor(getResources().getColor(R.color.white));
                                        }
                                    });

                        }else{
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            signUpButton.setEnabled(true);
                            signUpButton.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                });

            }else{
                confirmPassword.setError("Las contraseñas no coinciden");
                signUpButton.setEnabled(false);
                signUpButton.setTextColor(getResources().getColor(R.color.white));
            }
        }else{
            email.setError("Formato de email invalido!");
            signUpButton.setEnabled(false);
            signUpButton.setTextColor(getResources().getColor(R.color.white));

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){

        if(!userName.getText().toString().isEmpty()){
            if(!email.getText().toString().isEmpty()){
                if(!password.getText().toString().isEmpty() && password.getText().toString().length() >=6){
                    if(!confirmPassword.getText().toString().isEmpty()){
                        signUpButton.setEnabled(true);
                        signUpButton.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        signUpButton.setEnabled(false);
                        signUpButton.setTextColor(getResources().getColor(R.color.white));
                    }
                }else{
                    signUpButton.setEnabled(false);
                    signUpButton.setTextColor(getResources().getColor(R.color.white));
                }
            }else{
                signUpButton.setEnabled(false);
                signUpButton.setTextColor(getResources().getColor(R.color.white));
            }
        }else{
            signUpButton.setEnabled(false);
            signUpButton.setTextColor(getResources().getColor(R.color.white));
        }
    }
}