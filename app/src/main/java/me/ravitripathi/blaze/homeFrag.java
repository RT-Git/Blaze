package me.ravitripathi.blaze;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFrag extends Fragment {

    ProgressBar progressBar;
    private List<photoItem> photoItemList = new ArrayList<>();
    ValueEventListener urlListener;

    public homeFrag() {
        // Required empty public constructor
    }


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = v.findViewById(R.id.pro);

        Context c = getActivity();


        final RecyclerView recyclerView = v.findViewById(R.id.picsV);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(c, 1));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(c, 2));
        }
        final photoViewAdapter pVAdapter = new photoViewAdapter(photoItemList);

        recyclerView.setAdapter(pVAdapter);

        if (photoItemList.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference urlReference = databaseReference.child("users").child(UID);

        urlListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photoItemList.clear();
                for (DataSnapshot photoSnapshot : dataSnapshot.getChildren()) {
                    String url = photoSnapshot.getValue().toString();
//                    String test = photoSnapshot.getValue(String a);'
                    photoItem item = new photoItem();
                    item.setUri(url);
                    photoItemList.add(item);
                    if (!photoItemList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    pVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        urlReference.addValueEventListener(urlListener);

        return v;
    }

}
