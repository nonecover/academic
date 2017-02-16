package com.jakomulski.datacollector;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.jakomulski.datacollector.models.Photo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.jakomulski.datacollector.UsersActivity.DB_DATA_SOURCE;

public class PhotoActivity extends AppCompatActivity {
    private long userId;

    GridView imagesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_photo_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener((e)->{onBackPressed();});

        imagesView = (GridView) findViewById(R.id.imagesView);
        imagesView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            openRemoveItemDialog(((ImageAdapter) adapterView.getAdapter()).getItem(i));
            return true;
        });
        findViewById(R.id.makePhotoButton).setOnClickListener((e)->dispatchTakePictureIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = getIntent().getExtras().getLong(AddUserActivity.USER_ID_EXTRA);
        loadPhotos();
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.jakomulski.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Photo photo = DB_DATA_SOURCE.createPhoto(userId, mCurrentPhotoPath);
            loadPhotos();
        }
    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void loadPhotos() {
        List<Photo> photos = DB_DATA_SOURCE.getAllUserPhotos(userId);
        imagesView.setAdapter(new ImageAdapter(this, photos));
    }

    private void openRemoveItemDialog(Photo photo) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("delete record");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (d, i)->deletePhoto(photo));
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (d, i)->{});
        alertDialog.show();
    }

    private void deletePhoto(Photo photo){
        DB_DATA_SOURCE.deletePhoto(photo);
        loadPhotos();
    }
}
