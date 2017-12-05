package me.ravitripathi.blaze;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class accountFrag extends Fragment {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    FirebaseAuth firebaseAuth;

    public accountFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        final View v = inflater.inflate(R.layout.fragment_account, container, false);
        Button b = v.findViewById(R.id.logout);
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        TextView t = v.findViewById(R.id.name);
        t.setText(name);
        Uri photo = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        final Context context = getActivity();
        RequestOptions options = new RequestOptions()
                .placeholder(android.R.drawable.sym_def_app_icon);

        CircleImageView c = v.findViewById(R.id.circleImageView);
        Glide.with(getActivity())
                .load(photo)
                .apply(options)
                .into(c);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                linkPhone(credential, v);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "FirebaseAuthInvalidCredentialsException", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(context, "Quota Exceeded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
//                super.onCodeAutoRetrievalTimeOut(s);
                Snackbar.make(v.findViewById(R.id.accountLay), "Timed out, please try again", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
                Snackbar.make(v.findViewById(R.id.accountLay), "Code Sent", Snackbar.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
            }
        };


        final EditText e = v.findViewById(R.id.number);
        Button submit = v.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = e.getText().toString().trim();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
            }
        });
        return v;
    }


    private void linkPhone(PhoneAuthCredential credential, View v) {
        final View view = v;
        firebaseAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar.make(view.findViewById(R.id.accountLay), "Mobile Linked Successfully.",
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        //mVerificationField.setError("Invalid code.");
                        Snackbar.make(view.findViewById(R.id.accountLay), "Invalid Code.",
                                Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(view.findViewById(R.id.accountLay), "signInWithCredential:failure" + task.getException(),
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
