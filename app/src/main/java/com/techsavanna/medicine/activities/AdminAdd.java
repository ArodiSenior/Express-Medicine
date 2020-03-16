package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.techsavanna.medicine.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminAdd extends AppCompatActivity {
    Spinner Category;
    EditText ItemName;
    String upitemname;
    String upitemprice;
    EditText ItemPrice;
    EditText ItemDescription;
    String upitemdescription;
    String upcategory;
    Button AddItemButton;
    ImageView PicImage;
    FirebaseDatabase admindatabase;
    StorageReference storageReference;
    DatabaseReference adminreference;
    ProgressBar progressbarAdminAdd;

    private Uri imageUri = null;
    private StorageTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_add);


        PicImage = findViewById(R.id.PicImage);
        PicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4,3)
                        .start(AdminAdd.this); }
        });
        ItemName = findViewById(R.id.ItemName);
        ItemDescription = findViewById(R.id.ItemDescription);
        ItemPrice = findViewById(R.id.ItemPrice);
        AddItemButton = findViewById(R.id.AddItemButton);
        progressbarAdminAdd = findViewById(R.id.progressbarAdminAdd);
        admindatabase = FirebaseDatabase.getInstance();
        adminreference = FirebaseDatabase.getInstance().getReference();
        adminreference = admindatabase.getReference("Food").child("Food").push();
        AddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });


        Category = findViewById(R.id.Category);

        List<String> list = new ArrayList<>();
        list.add("Total");
        list.add("Harshi");
        list.add("Pro-Gas");
        list.add("K-Gas");
        list.add("Oilibya");
        list.add("Mpishi");
        list.add("G-Gas");
        list.add("Jiji-Gas");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(adapter);
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemvalue = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void AddItem(){
        initialize();
        if(!validate()) {

        }
        else {
            onAddItemSuccess();

        }
    }
    public boolean validate(){
        boolean valid = true;
        if (upitemname.isEmpty()){
            ItemName.setError("Please enter Item Name");
            valid = false;
        }
        else if (upitemprice.isEmpty()){
            ItemPrice.setError("Please enter Item Price");
            valid = false;
        }
        else if (upitemdescription.isEmpty()){
            ItemDescription.setError("Please enter Item Description");
            valid = false;
        }

        return valid;
    }

    public void onAddItemSuccess(){

        progressbarAdminAdd.setVisibility(View.VISIBLE);


        if (imageUri != null){
            storageReference = FirebaseStorage.getInstance().getReference("images/" + System.currentTimeMillis() + "jpg");

            uploadTask = storageReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        String mUri = downloadUrl.toString();

                        String string = adminreference.getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("upimage", mUri);
                        hashMap.put("upgitemname", upitemname);
                        hashMap.put("upitemprice", upitemprice);
                        hashMap.put("upitemdescription", upitemdescription);
                        hashMap.put("upcategory", upcategory);
                        hashMap.put("upitemid", string);
                        adminreference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressbarAdminAdd.setVisibility(View.GONE);
                                    Toast.makeText(AdminAdd.this, "Item Added", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                    finish();

                                }else {
                                    Toast.makeText(AdminAdd.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });



                    }
                    else {
                        progressbarAdminAdd.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressbarAdminAdd.setVisibility(View.GONE);

                }
            });

        }else {

            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
            progressbarAdminAdd.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                PicImage.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }


    public void initialize(){
        upitemname = ItemName.getText().toString().trim();
        upitemprice = ItemPrice.getText().toString().trim();
        upitemdescription = ItemDescription.getText().toString().trim();
        upcategory = Category.getSelectedItem().toString();
    }
    public  void onBackPressed(){
        super.onBackPressed();
    }
}
