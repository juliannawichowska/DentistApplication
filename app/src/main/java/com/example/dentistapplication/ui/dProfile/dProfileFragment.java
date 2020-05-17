package com.example.dentistapplication.ui.dProfile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.dentistapplication.R;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/*
    Fragment ten odpowiada za wyświetlenie profilu lekarza, oraz umożliwia mu edycje danych
 */

public class dProfileFragment extends Fragment {
    ImageView avatar;
    TextView nameSurname, email, descriptionContent, numberContent, addressContent;
    FloatingActionButton fab;

    private dProfileViewModel dProfileViewModel;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    //dres url zdjęcia profilowego
    Uri image_uri;

    //progress dialog
    ProgressDialog pd;

    //zmienne dla pozwoleń
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //tablice pozwoleń
    String cameraPermissions[];
    String storagePermissions[];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dProfileViewModel =
                ViewModelProviders.of(this).get(dProfileViewModel.class);
        View root = inflater.inflate(R.layout.d_fragment_profile, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Doctors");

        avatar = root.findViewById(R.id.Avatar);
        nameSurname = root.findViewById(R.id.NameSurname);
        email = root.findViewById(R.id.Email);
        fab = root.findViewById(R.id.fab);
        descriptionContent = root.findViewById(R.id.descriptionContent);
        numberContent = root.findViewById(R.id.numberContent);
        addressContent = root.findViewById(R.id.addressContent);

        //inicjacja progress dialog
        pd = new ProgressDialog(getActivity());

        //inicjacja tabeli pozwoleń
        cameraPermissions =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //wyszukanie użytkownika po mailu
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //zapisanie do zmiennych wartości z bazy danych
                    String nameSurname1 = "" + ds.child("name").getValue() +" "+ ds.child("surname").getValue();
                    String email1 = "" + ds.child("email").getValue();
                    String number1 = "" + ds.child("number").getValue();
                    String image1 = "" + ds.child("imageURL").getValue();
                    String description1 = "" + ds.child("description").getValue();
                    String address1 = "" + ds.child("address").getValue();

                    //ustawienie w menu imienia i nazwiska, adresu e-mail, numeru telefonu, opisu lekarza i adresu gabinetu
                    nameSurname.setText(nameSurname1);
                    email.setText(email1);
                    numberContent.setText(number1);
                    descriptionContent.setText(description1);
                    addressContent.setText(address1);

                    //załadowanie zdjęcia profilowego z bazy danych
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

        //w momencie gdy ktoś kliknie floating button wyświetla się dialog edycji profilu
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
        return root;
    }

    public void showImagePickDialog(){
        //funkcja ta odpowiada za wyświetlenie opcji wyboru skąd ma zostać pobrane zdjęcie profilowe

        //opcje do wyboru
        String[] options = {"Aparat","Galeria"};

        //budowanie dialogu
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz zdjęcie z");
        //ustawianie opcji
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //kliknięto opcje aparatu
                    //sprawdzanie czy zaakceptowano zgodę na dostęp do aparatu
                    if(!checkCameraPermissions()){
                        //przekierowanie do funkcji proszącej o zaakceptowanie zgody na dostęp do aparatu
                        requestCameraPermission();
                    } else {
                        //przekierowanie do funkcji wyboru zdjęcia z aparatu
                        pickFromCamera();
                    }
                } if (which==1){
                    //kliknięto opcję galerii
                    //sprawdzanie czy zaakceptowano zgodę na dostęp do galerii
                    if(!checkStoragePermissions()){
                        //przekierowanie do funkcji proszącej o zaakceptowanie zgody na dostęp do galerii
                        requestStoragePermission();
                    } else {
                        //przekierowanie do funkcji wyboru zdjęcia z galerii
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery(){
        //funkcja ta odpowiada za wybór zdjęcia z galerii
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        //funkcja ta odpowiada za wybór zdjęcia z aparatu
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissions(){
        //sprawdzanie czy zaakceptowana została zgoda na dostęp do galerii
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //funkcja ta odpowiada za wyświetlenie prośby o dostęp do galerii
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        //sprawdzanie czy zaakceptowana została zgoda na dostęp do aparatu
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //funkcja ta odpowiada za wyświetlenie prośby o dostęp do aparatu
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //funkcja która następuje po zaakceptowaniu zgód o dostęp

        //sprawdzanie która ze zgód została przed chwilą zaakceptowana
        switch(requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length>0) {
                    //zapisanie zgody
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Nie uzyskano zgody",Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0) {
                    //zapisanie zgody
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
        //ustawienie zdjęcia profilowego
        if(resultCode==RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //zdjęcie jest z galerii
                image_uri = data.getData();
                //ustawianie zdjęcia
                try {
                    uploadProfilePhoto(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //zdjęcie jest z aparatu
                //ustawianie zdjęcia
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
        //funkcja ta odpowiada za wyświetlenie dialogu z opcjami edycji profilu
        //opcje w dialogu
        String options[] = {"Edytuj zdjęcie profilowe", "Edytuj numer telefonu", "Edytuj opis", "Edytuj adres gabinetu"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ustawianie tytułu
        builder.setTitle("Wybierz opcję");
        //ustawianie opcji
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //obsługa kliknięcia danej opcji
                if(which==0){
                    //Edycja zdjęcia profilowego
                    pd.setMessage("Edycja zdjęcia");
                    showImagePickDialog();
                }
                else if(which==1){
                    //Edycja numeru telefonu
                    pd.setMessage("Edycja numeru");
                    editPhoneNumber();
                }
                else if(which==2){
                    //Edycja opisu lekarza
                    pd.setMessage("Edycja opisu");
                    editDescription();
                }
                else if(which==3){
                    //Edycja adresu gabinetu
                    pd.setMessage("Edycja adresu");
                    editAddress();
                }
            }

        });
        //utworzenie i wyświetlenie dialogu
        builder.create().show();
    }

    private void editPhoneNumber() {
        //funkcja ta odpowiada za wyświetlenie dialogu edycji numeru telefonu i zaktulizowanie go
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ustawianie tytułu
        builder.setTitle("Zaktualizuj numer telefonu");
        //ustawienie layoutu
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //dodanie pola tekstowego
        final EditText editText = new EditText(getActivity());
        //ustawienie podpowiedzi
        editText.setHint("Podaj numer");
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        //dodanie przyciksu zapisu numeru
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //zapisanie do zmiennej wprowadzonego numeru
                String value = editText.getText().toString().trim();
                //sprawdzanie czy numer jest pusty
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    //utworzenie HashMap z numerem telefonu
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("number", value);

                    //zaktualizowanie bazy danych o numer telefonu
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //zaktualizowano pomyślnie
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Zaktualizowano..", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //aktualizacja nie powiodła się
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
        //dodanie przycisku anulowania
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //utworzenie i wyświetlenie dialogu
        builder.create().show();
    }

    private void editDescription() {
        //funkcja ta odpowiada za wyświetlenie dialogu edycji opisu i zaktualizowanie go
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ustawianie tytułu
        builder.setTitle("Zaktualizuj opis profilu");
        //ustawienie layoutu
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //dodanie pola tekstowego
        final EditText editText = new EditText(getActivity());
        //ustawienie podpowiedzi
        editText.setHint("Podaj opis");
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        //dodanie przyciksu zapisu opisu
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //zapisanie do zmiennej wprowadzonego opisu
                String value = editText.getText().toString().trim();
                //sprawdzanie czy numer jest pusty
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    //utworzenie HashMap z opisem lekarza
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("description", value);

                    //zaktualizowanie bazy danych o opis
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //zaktualizowano pomyślnie
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Zaktualizowano..", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //aktualizacja nie powiodła się
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
        //dodanie przycisku anulowania
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //utworzenie i wyświetlenie dialogu
        builder.create().show();

    }

    private void editAddress() {
        //funkcja ta odpowiada za wyświetlenie dialogu edycji adresu i zaktualizowanie go
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ustawianie tytułu
        builder.setTitle("Zaktualizuj adres gabinetu");
        //ustawienie layoutu
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //dodanie pola tekstowego
        final EditText editText = new EditText(getActivity());
        editText.setHint("Podaj adres");
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        //dodanie przyciksu zapisu adresu
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //zapisanie do zmiennej wprowadzonego adresu
                String value = editText.getText().toString().trim();
                //sprawdzanie czy numer jest pusty
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    //utworzenie HashMap z adresem gabinetu
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("address", value);

                    //zaktualizowanie bazy danych o adres
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //zaktualizowano pomyślnie
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Zaktualizowano..", Toast.LENGTH_SHORT).show();;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //aktualizacja nie powiodła się
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
        //dodanie przycisku anulowania
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //utworzenie i wyświetlenie dialogu
        builder.create().show();
    }

    private void uploadProfilePhoto(Uri image_uri) throws IOException {
        //funkcja ta odpowiada za zaktualizowanie zdjęcia profilowego
        pd.show();
        //ścieżka do miejsca gdzie składowane zostaną zdjęcia
        String fileNameAndPath = "ProfilePictures/"+user.getUid();

        //pobranie bitmapy z uri
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();
        //zapis referencji do zdjęcia
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        reference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //pobranie adresu uri zdjęcia
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //utworzenie referencji do bazy
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            //utworzenie HashMap z adresem uri
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("imageURL", downloadUri);

                            //zaktualizowanie bazy danych o adres uri
                            databaseReference.child("Doctors/"+user.getUid()).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //zaktualizowano pomyślnie
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Zdjęcie zostało zaktualizowane", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //aktualizacja nie powiodła się
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Nie udało się zaktualizować zdjęcia", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //Akttualizacja nie powiodła się
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Operacja nie powiodła się", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Akttualizacja nie powiodła się
                        pd.dismiss();
                        Toast.makeText(getActivity(),"Nastąpił błąd", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}