package me.ravitripathi.blaze;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


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
//        Button b = (Button) v.findViewById(R.id.subscribe);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseMessaging.getInstance().subscribeToTopic("arc");
//                Toast.makeText(getActivity(),"Subscribed",Toast.LENGTH_SHORT).show();
//            }
//        });

        String l = FirebaseInstanceId.getInstance().getToken();
        TextView t = (TextView) v.findViewById(R.id.text);
        if (l != null)
            t.setText(l);
        return v;
    }

}
