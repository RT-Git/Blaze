package me.ravitripathi.blaze;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class accountFrag extends Fragment {


    public accountFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        Button b = v.findViewById(R.id.logout);
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        TextView t = v.findViewById(R.id.name);
        t.setText(name);
        Uri photo = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

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
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }
        });
        return v;
    }

}
