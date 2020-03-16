package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.techsavanna.medicine.models.ItemModel;
import com.techsavanna.medicine.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class EditItem extends AppCompatActivity {

    TextView EditName;
    String upeditname;
    TextView EditPrice;
    String upeditprice;
    TextView EditCategory;
    String upeditcategory;
    TextView EditDescription;
    String upeditdescription;
    Button EditButton;
    ImageView EditImage;
    ProgressBar progressbarAdminEdit;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ImageView EditImageIcon;

    Intent intent;

    String edititem;


    private Uri imageUri = null;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_item);

        EditCategory = findViewById(R.id.EditCategory);
        EditName = findViewById(R.id.EditName);
        EditDescription = findViewById(R.id.EditDescription);
        EditButton = findViewById(R.id.EditButton);
        EditPrice = findViewById(R.id.EditPrice);
        EditImage = findViewById(R.id.EditImage);
        progressbarAdminEdit =  findViewById(R.id.progressbarAdminEdit);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseDatabase = FirebaseDatabase.getInstance();

        EditImageIcon = findViewById(R.id.EditImageIcon);

        intent = getIntent();
        edititem = intent.getStringExtra("ItemEditid");
        databaseReference = FirebaseDatabase.getInstance().getReference("Food").child("Food").child(edititem);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ItemModel itemModel = dataSnapshot.getValue(ItemModel.class);

                if (itemModel != null) {
                    EditName.setText(itemModel.getUpgitemname());
                    EditPrice.setText(itemModel.getUpitemprice());
                    EditDescription.setText(itemModel.getUpitemdescription());
                    EditCategory.setText(itemModel.getUpcategory());
                    Picasso.with(EditItem.this).load(itemModel.getUpimage()).into(EditImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditInformation();

            }
        });


        EditImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4,3)
                        .start(EditItem.this);


            }
        });
    }


    private void saveEditInformation(){

        upeditname = EditName.getText().toString().trim();
        upeditprice = EditPrice.getText().toString().trim();
        upeditdescription = EditDescription.getText().toString().trim();
        upeditcategory = EditCategory.getText().toString().trim();

        progressbarAdminEdit.setVisibility(View.VISIBLE);

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

                        HashMap<String, Object> map = new HashMap<>();

                        map.put("upgitemname", upeditname);
                        map.put("upimage", mUri);
                        map.put("upitemprice", upeditprice);
                        map.put("upitemdescription", upeditdescription);
                        map.put("upcategory", upeditcategory);
                        map.put("upitemid", edititem);
                        databaseReference.updateChildren(map);
                        progressbarAdminEdit.setVisibility(View.GONE);

                        Toast.makeText(EditItem.this, "Item updated", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();


                    }
                    else {
                        progressbarAdminEdit.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressbarAdminEdit.setVisibility(View.GONE);

                }
            });

        }else {

            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
            progressbarAdminEdit.setVisibility(View.GONE);
        }






    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                EditImage.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }

}
