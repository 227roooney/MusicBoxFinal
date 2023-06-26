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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.musicbox1.MainActivity;
import com.example.musicbox1.R;

public class SignInFragment extends Fragment {

    private TextView dontHaveAnAccount;
    private TextView resetPassword;
    private FrameLayout frameLayout;

    private EditText email;
    private EditText password;
    private Button signInButton;
    private ProgressBar signInProgressBar;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);inflater.inflate(R.layout.fragment_sign_in, container, false);

        dontHaveAnAccount = view.findViewById(R.id.no_tengo_una_cuenta);
        resetPassword = view.findViewById(R.id.reiniciar_contrasena);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        signInButton = view.findViewById(R.id.signInButton);
        signInProgressBar = view.findViewById(R.id.signInProgressBar);

        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new ResetPassword());
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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setEnabled(false);
                signInButton.setTextColor(getResources().getColor(R.color.white));
                sigInWithFirebase();
            }
        });
    }

    private void sigInWithFirebase() {
        if(email.getText().toString().matches("[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+")){
            signInProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            signInProgressBar.setVisibility(View.INVISIBLE);
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }else{
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                signInButton.setEnabled(true);
                                signInButton.setTextColor(getResources().getColor(R.color.white));
                            }
                        }
                    });

        }else{
            email.setError("Formato de email invalido!");
            signInButton.setEnabled(false);
            signInButton.setTextColor(getResources().getColor(R.color.white));

        }
    }

    private void checkInputs() {

        if(!email.getText().toString().isEmpty()){
            if(!password.getText().toString().isEmpty()){
                signInButton.setEnabled(true);
                signInButton.setTextColor(getResources().getColor(R.color.white));
            }else{
                signInButton.setEnabled(false);
                signInButton.setTextColor(getResources().getColor(R.color.white));
            }
        }else{
            signInButton.setEnabled(false);
            signInButton.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_from_left);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}