package com.dexterlab.sahil.otp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText editPhoneNumber,editOTPRecieved;
    Button sendOTP,verifyOTP;
    String verificationCode;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editOTPRecieved = (EditText) findViewById(R.id.editOTPRecieved);

        mAuth = FirebaseAuth.getInstance();

        sendOTP = (Button) findViewById(R.id.SendOTP);
        verifyOTP = (Button) findViewById(R.id.verifyOTP);

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(MainActivity.this,"Code sent to the number",Toast.LENGTH_LONG).show();
            }
        };

    }
    public void send_sms(View view) {
        String number = editPhoneNumber.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS,MainActivity.this,mcallback);
    }

    public void signInWithPhone(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"User Signed in successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void verify(View view){
        String input_code = editOTPRecieved.getText().toString();
        verifyPhoneNumber(verificationCode,input_code);
            

    }

    public void verifyPhoneNumber(String verifyCode, String input_code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode,input_code);
        signInWithPhone(credential);
    }
}
