package me.ravitripathi.blaze;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class uploadFrag extends Fragment {

    Button button;
    ProgressBar progressBar;
    ImageView imageView;
    TextView link;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

    int FILE_CODE = 2222;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    public uploadFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        Button pick = (Button) view.findViewById(R.id.pick);


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });


        button = (Button) view.findViewById(R.id.button);
        link = (TextView) view.findViewById(R.id.link);
        imageView = (ImageView) view.findViewById(R.id.image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                imageView.setDrawingCacheEnabled(false);
                byte[] data = baos.toByteArray();

                //random file name
                String path = "arcfire/" + UUID.randomUUID() + ".jpg";
                StorageReference arcfireRef = storage.getReference(path);

                //Add custom meta data
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setCustomMetadata("upload_date", DateFormat.getDateTimeInstance().format(new Date()))
                        .build();

                //Lets you monitor the status of transfer
                UploadTask uploadTask = arcfireRef.putBytes(data, metadata);

                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                    }
                });
                //The upload task is bound to this activity. If this activity stops it stops too
                uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);

                        Uri url = taskSnapshot.getDownloadUrl();
                        link.setText(url.toString());

                        ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(url.toString());
                    }
                });
            }
        });

        return view;
    }


    private void openFilePicker() {
        Intent i = new Intent(getActivity(), FilePickerActivity.class);
        startActivityForResult(i, FILE_CODE);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
//            for (Uri uri: files) {
//                File file = Utils.getFileForUri(uri);
//                // Do something with the result...
//            }
            Toast.makeText(getActivity(), files.get(0).toString(), Toast.LENGTH_SHORT).show();
            updateView(files.get(0));
        }


    }


    private void updateView(Uri path) {
//        String file = photoPaths.get(0);
//        Bitmap myBitmap = BitmapFactory.decodeFile(path.toString());
//        imageView.setImageBitmap(myBitmap);
        imageView.setImageURI(path);
    }
}
