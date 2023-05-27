package com.teamRTL.cloudmedicalproject.UIs.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamRTL.cloudmedicalproject.MainActivity;
import com.teamRTL.cloudmedicalproject.Models.Doctors;
import com.teamRTL.cloudmedicalproject.Models.Patients;

import com.teamRTL.cloudmedicalproject.Models.Users;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivitySignUpBinding;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    FirebaseAuth auth;
    DatabaseReference reference,reference1;
    String name, date, address, password, email, phone;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);


        /////////BirthDate///////////////
        binding.birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDate();

            }
        });


        /////////////signup///////////
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Objects.requireNonNull(binding.nameSignup.getText()).toString();
                date = Objects.requireNonNull(binding.birthdate.getText()).toString();
                address = Objects.requireNonNull(binding.address.getText()).toString();
                email = Objects.requireNonNull(binding.email.getText()).toString();
                phone = Objects.requireNonNull(binding.phoneNumber.getText()).toString();
                password = Objects.requireNonNull(binding.passwordSign.getText()).toString();
                String passwordConfirm = Objects.requireNonNull(binding.confirmPassword.getText()).toString();

//            if(isValidSignUpDetails()){
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "all fields are required", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "password must be at least 6 character", Toast.LENGTH_SHORT).show();

                } else {
                    signUp(name, date, address, email, phone, password);
                }


            }
        });


    }

    private void userDate() {
        // Create a Calendar object to store the selected date
        Calendar calendar = Calendar.getInstance();
// Create a DatePickerDialog and set its OnDateSetListener
        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the Calendar object with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Update the birth date field in your sign-up form
                        updateBirthDateField(calendar.getTime());
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

// Set the maximum date to today's date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// Show the DatePickerDialog
        datePickerDialog.show();
    }

    ////////birthdate/////
    private void updateBirthDateField(Date date) {
        // Format the date as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(date);

        // Update the birth date field in your sign-up form with the selected date
        EditText birthDateField = findViewById(R.id.birthdate);
        birthDateField.setText(dateString);
    }


    private void signUp(String name, String date, String address, String email, String phone, String password) {


        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userUid = firebaseUser.getUid();
                            if (binding.doctorCheck.isChecked()) {
                                reference = FirebaseDatabase.getInstance().getReference("Doctors").child(userUid);
                                reference1 = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
                                Users users = new Users(userUid,name,"",email) ;
                               reference1.setValue(users);
                                Doctors doctors = new Doctors(userUid, name, date, address, email, phone, "", "");
                                reference.setValue(doctors).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.cancel();
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("Doctors",firebaseUser.getEmail());
                                            Log.e("d",intent.toString());
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                });

                            } else if (binding.patientCheck.isChecked()) {
                                reference = FirebaseDatabase.getInstance().getReference("Patients").child(userUid);
                                reference1 = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
                                Users users = new Users(userUid,name,"",email) ;
                                reference1.setValue(users);
                                Patients patients = new Patients(userUid, name, date, address, email, phone,"");
                                reference.setValue(patients).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.cancel();
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("Patients",firebaseUser.getEmail());
                                            Log.e("p",intent.toString());
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                });

                            }


                        } else {
                            Toast.makeText(SignUpActivity.this, "signup failed", Toast.LENGTH_SHORT).show();
                            Log.e("ffff",task.toString());
                            progressDialog.cancel();
                        }

                    }


                });

    }
}


