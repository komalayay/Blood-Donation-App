package com.example.bloodred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

     private TextView AlreadyHaveAnAccount1;



     private CircleImageView profileImage;

     private TextInputEditText registerName,registerPhoneNo,registerEmail,registerPassword;

     private Spinner registerBloodGroups;

     private Button registerButton;

     private Uri ImageUri;

     private ProgressDialog Loading;

     private FirebaseAuth mAuth;
     private DatabaseReference UserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        AlreadyHaveAnAccount1=findViewById(R.id.AlreadyHaveAnAccount1);
        AlreadyHaveAnAccount1.setOnClickListener(view -> {
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        });


        profileImage=findViewById(R.id.profileImage);
        registerName=findViewById(R.id.registerName);
        registerBloodGroups=findViewById(R.id.registerBloodGroup);
        registerEmail=findViewById(R.id.registerEmail);
        registerPassword=findViewById(R.id.registerPassword);
        registerPhoneNo=findViewById(R.id.registerPhoneNo);
        registerButton=findViewById(R.id.registerButton);
        Loading=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();




        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=registerEmail.getText().toString().trim();
                final String name=registerName.getText().toString().trim();
                final String phoneNo=registerPhoneNo.getText().toString().trim();
                final String password=registerPassword.getText().toString().trim();
                final String bloodGroup=registerBloodGroups.getSelectedItem().toString().trim();

                if(TextUtils.isEmpty(email)){
                    registerEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    registerName.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is Required");
                    return;
                }
                if(bloodGroup.equals("Select Your BloodGroup")){
                    Toast.makeText(RegisterActivity.this, "Select Your Blood Group", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    Loading.setMessage("We are registering You");
                    Loading.setCanceledOnTouchOutside(false);
                    Loading.show();


                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful())
                            {
                                String error=task.getException().toString();
                                Toast.makeText(RegisterActivity.this,"Error" + error,Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String currentUserId=mAuth.getCurrentUser().getUid();
                                UserDatabase= FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);

                                HashMap userInformation=new HashMap();
                                userInformation.put("id",currentUserId);
                                userInformation.put("name",name);
                                userInformation.put("email",email);
                                userInformation.put("phoneno",phoneNo);
                                userInformation.put("bloodgroup",bloodGroup);
                                userInformation.put("type","donor");
                                userInformation.put("search","donor"+ bloodGroup);



                                UserDatabase.updateChildren(userInformation).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Data set successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                        Loading.dismiss();

                                    }
                                });
                                if(ImageUri!=null){
                                    final StorageReference filepath= FirebaseStorage.getInstance().getReference()
                                            .child("profile image").child(currentUserId);
                                    Bitmap bitmap=null;

                                    try{
                                        bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),ImageUri);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data= byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask=filepath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null){
                                                Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String ImageUrl=uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepicture",ImageUrl);

                                                        UserDatabase.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {

                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(RegisterActivity.this,"Image Url added to database successfully",Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(RegisterActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }

                                        }
                                    });


                                }
                                Intent intent=new Intent(RegisterActivity.this,NavigationDrawerActivity.class);
                                startActivity(intent);
                                finish();

                                Loading.dismiss();
                            }
                        }
                    });

                }





            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!= null){
            ImageUri=data.getData();
            profileImage.setImageURI(ImageUri);
        }
    }
}

