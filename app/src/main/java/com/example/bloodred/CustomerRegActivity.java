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
import android.util.Log;
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

public class CustomerRegActivity extends AppCompatActivity {

    private TextView AlreadyHaveAnAccount;


    private CircleImageView profileImage;



    private TextInputEditText registercusName,registercusPhone,registercusEmail,registercusPassword;

    private Spinner registerBloodGroup;

    private Button registerButton;

    private Uri ImageUri;

    private ProgressDialog Loading;

    private FirebaseAuth mAuth;
    private DatabaseReference UserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reg);







        AlreadyHaveAnAccount=findViewById(R.id.AlreadyHaveAnAccount);
        AlreadyHaveAnAccount.setOnClickListener(view -> {
            Intent intent=new Intent(CustomerRegActivity.this,LoginActivity.class);
            startActivity(intent);
        });


        profileImage=findViewById(R.id.profileImage);
        registercusName=findViewById(R.id.registercusName);
        registerBloodGroup=findViewById(R.id.registerBloodGroup);
        registercusEmail=findViewById(R.id.registercusEmail);
        registercusPassword=findViewById(R.id.registercusPassword);
        registercusPhone=findViewById(R.id.registercusPhone);
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
                final String email=registercusEmail.getText().toString().trim();
                final String name=registercusName.getText().toString().trim();
                final String phoneNo=registercusPhone.getText().toString().trim();
                final String password=registercusPassword.getText().toString().trim();
                final String bloodGroup=registerBloodGroup.getSelectedItem().toString().trim();

                if(TextUtils.isEmpty(email)){
                    registercusEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    registercusName.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    registercusPassword.setError("Password is Required");
                    return;
                }
                if(bloodGroup.equals("Select Your BloodGroup")){
                    Toast.makeText(CustomerRegActivity.this, "Select Your Blood Group", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CustomerRegActivity.this,"Error" + error,Toast.LENGTH_SHORT).show();
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
                                userInformation.put("type","customer");
                                userInformation.put("search","customer"+ bloodGroup);



                                UserDatabase.updateChildren(userInformation).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(CustomerRegActivity.this, "Data set successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(CustomerRegActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CustomerRegActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();

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
                                                                    Toast.makeText(CustomerRegActivity.this,"Image Url added to database successfully",Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    Toast.makeText(CustomerRegActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                                result.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Toast.makeText(CustomerRegActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                                                        Log.e("Creg", "onFailure: ",e );

                                                    }
                                                });
                                            }

                                        }
                                    });


                                }
                                Intent intent=new Intent(CustomerRegActivity.this,NavigationDrawerActivity.class);
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
