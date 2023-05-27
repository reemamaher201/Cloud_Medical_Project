package com.teamRTL.cloudmedicalproject.UIs.doctors;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.teamRTL.cloudmedicalproject.R;
import com.teamRTL.cloudmedicalproject.databinding.ActivityAddArticlesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddArticlesActivity extends AppCompatActivity {
    ActivityAddArticlesBinding binding;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    public Dialog pic_image_dialog;
    String image2, video;
    ArrayAdapter<String> adapter;
    List<String> itemsList;
    ActivityResultLauncher<String> al1;
    ActivityResultLauncher<String> al3;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pic_image_dialog = new Dialog(this);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        itemsList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        clickListener();
        logoImage();
        video();

        populateSpinnerData();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void clickListener() {
        binding.logo.setOnClickListener(View -> {
            requestStoragePermission();
            al1.launch("image/*");
        });

        binding.saveBtn.setOnClickListener(View -> {
            AddData();
        });
    }

    private void AddData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> map = new HashMap<>();
        map.put("title", binding.name.getText().toString());
        map.put("name", binding.doctorName.getText().toString());
        map.put("description", binding.description.getText().toString());
        map.put("logo", image2);
        map.put("video", video);
        map.put("Category", binding.spinner.getSelectedItem().toString());


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Articles");

        collectionRef.add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference itemsRef = db.collection("Articles");

                        DocumentReference itemRef = itemsRef.document(documentReference.getId());
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("item_id", documentReference.getId());
                        itemRef.update(updatedData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle errors here
                                    }
                                });

                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void logoImage() {
        al1 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            Log.e("lllllllllllllllllll", result.getPath());
                            binding.logoProgressBar.setVisibility(View.VISIBLE);

                            storageReference = firebaseStorage.getReference("images/" + result.getLastPathSegment());
                            StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.putFile(result);
                            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    return storageReference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    binding.logoProgressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        image2 = task.getResult().toString();
                                        Glide.with(getBaseContext()).load(image2).transform(new RoundedCorners(8)).error(R.drawable.doctor_ann_marie_navar_2).into(binding.logo);
                                        Log.e("UploadActivity1", image2);
                                    } else {
                                        Toast.makeText(getBaseContext(), "Image Upload Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // معالجة حالة القيمة الـ null
                        }
                    }
                });
    }

    private void video() {
        al3 = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            Log.d("TAG", "Selected video URI: " + uri.toString());

                            uploadVideoToStorage(uri);
                        } else {
                            // معالجة حالة القيمة الـ null
                        }
                    }
                });

        binding.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al3.launch("video/*");
            }
        });
    }

    private void uploadVideoToStorage(Uri file) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String filename = System.currentTimeMillis() + "_" + file.getLastPathSegment();

        StorageReference videoRef = storageRef.child("videos/" + filename);

        binding.videoProgressBar.setVisibility(View.VISIBLE);

        videoRef.putFile(file)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return videoRef.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        binding.videoProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            video = task.getResult().toString();
                            Toast.makeText(AddArticlesActivity.this, "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddArticlesActivity.this, "Video upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void populateSpinnerData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Categories");

        collectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String itemName = document.getString("title");


                                itemsList.add(itemName);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
