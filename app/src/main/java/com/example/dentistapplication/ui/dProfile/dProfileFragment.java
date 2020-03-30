package com.example.dentistapplication.ui.dProfile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.GetChars;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class dProfileFragment extends Fragment {

    //firebase inits
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //init views
    ImageView avatar;
    TextView nameSurname, email, descriptionContent, numberContent, addressContent;
    FloatingActionButton fab;

    //uri of picked image
    Uri image_uri;

    //progress dialog
    ProgressDialog pd;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];

    private dProfileViewModel dProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dProfileViewModel =
                ViewModelProviders.of(this).get(dProfileViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_profile, container, false);

        //init firebase
        firebaseAuth =  FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        //init views
        avatar = root.findViewById(R.id.Avatar);
        nameSurname = root.findViewById(R.id.NameSurname);
        email = root.findViewById(R.id.Email);
        fab = root.findViewById(R.id.fab);
        descriptionContent = root.findViewById(R.id.descriptionContent);
        numberContent = root.findViewById(R.id.numberContent);
        addressContent = root.findViewById(R.id.addressContent);

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        //init arrays of permissions
        cameraPermissions =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get Data
                    String nameSurname1 = "" + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                    String email1 = "" + ds.child("email").getValue();
                    String number1 = "" + ds.child("number").getValue();
                    String image1 = "" + ds.child("imageURL").getValue();
                    String description1 = "" + ds.child("description").getValue();
                    String address1 = "" + ds.child("address").getValue();

                    //set data
                    nameSurname.setText(nameSurname1);
                    email.setText(email1);
                    numberContent.setText(number1);
                    descriptionContent.setText(description1);
                    addressContent.setText(address1);

                    try {
                        Picasso.get().load(image1).into(avatar);
                    } catch (Exception e){
                        Picasso.get().load(R.drawable.ic_person).into(avatar);
                    }
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //fab button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        return root;
    }


    public void showImagePickDialog(){
        //options
        String[] options = {"Aparat","Galeria"};

        //dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz zdjęcie z");
        //set options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("tag", "ajjjjjjjjjjjj");
                if(which==0){
                    //camera clicked
                    if(!checkCameraPermissions()){
                        requestCameraPermission();
                        Log.v("tag", "oooojjjjjjjjjjjj");
                    } else {
                        Log.v("tag", "ejjjjjjjjjjjj");
                        pickFromCamera();
                    }
                } if (which==1){
                    //gallery clicked
                    if(!checkStoragePermissions()){
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera(){

        Log.v("tag", "mmmmjjjjjjjjjjjj");
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);


    }

    private boolean checkStoragePermissions(){
        //check if storage permission is enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request permission to storage
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        Log.v("tag", "ppppjjjjjjjjjjjj");
        //check if camera permission is enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        Log.v("tag", "wwwwjjjjjjjjjjjj");
        //request permission to camera
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //handle permissions results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        Log.v("tag", "llllljjjjjjjjjjjj");
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Nie uzyskano zgody",Toast.LENGTH_LONG).show();
                    }
                }else {
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Nie uzyskano zgody",Toast.LENGTH_LONG).show();
                    }
                }else {
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is from gallery
                image_uri = data.getData();
                //set to image View
                try {
                    uploadProfilePhoto(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera
                try {
                    uploadProfilePhoto(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    public void showEditProfileDialog(){
        //view dialog with editing option
        //option to show in dialog
        String options[] = {"Edytuj zdjęcie profilowe", "Edytuj numer telefonu", "Edytuj opis", "Edytuj adres gabinetu"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Wybierz opcję");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items clicked
                if(which==0){
                    //Edit profile picture
                    pd.setMessage("Edycja zdjęcia");
                    showImagePickDialog();
                }
                else if(which==1){
                    //Edit phone number
                    pd.setMessage("Edycja numeru");
                    editPhoneNumber();
                }
                else if(which==2){
                    //Edit  description
                    pd.setMessage("Edycja opisu");
                    editDescription();
                }
                else if(which==3){
                    //Edit address
                    pd.setMessage("Edycja adresu");
                    editAddress();
                }
            }

        });
        //create and show dialog
        builder.create().show();
    }

    private void editPhoneNumber() {
        //sow dialog to edit phone number
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Zaktualizuj numer telefonu");
        //set layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Podaj numer");
        linearLayout.addView(editText);
        //set view
        builder.setView(linearLayout);

        //add buttons in dialog to save number
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //check if number is empty
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("number", value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Zaktualizowano..", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(), "Wprowadź numer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button to cancel
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void editDescription() {
        //sow dialog to edit phone number
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Zaktualizuj opis profilu");
        //set layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Podaj opis");
        linearLayout.addView(editText);
        //set view
        builder.setView(linearLayout);

        //add buttons in dialog to save number
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //check if number is empty
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("description", value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Zaktualizowano..", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(), "Wprowadź opis", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button to cancel
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void editAddress() {
        //sow dialog to edit phone number
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Zaktualizuj adres gabinetu");
        //set layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Podaj adres");
        linearLayout.addView(editText);
        //set view
        builder.setView(linearLayout);

        //add buttons in dialog to save number
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //check if number is empty
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("address", value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Zaktualizowano..", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(), "Wprowadź adres", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button to cancel
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();

    }


    private void uploadProfilePhoto(Uri image_uri) throws IOException {

        pd.show();
        //Path to place which will contain all send images
        String fileNameAndPath = "ProfilePictures/"+user.getUid();

        //get bitmap from uri
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        reference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image upload
                        //get url of image
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //add image uri and info to database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("imageURL", downloadUri);
                            databaseReference.child("Users/"+user.getUid()).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Zdjęcie zostało zaktualizowane", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Nie udało się zaktualizować zdjęcia", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Operacja nie powiodła się", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed
                        pd.dismiss();
                        Toast.makeText(getActivity(),"Nastąpił błąd", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}