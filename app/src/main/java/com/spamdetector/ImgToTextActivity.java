package com.spamdetector;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.spamdetector.databinding.ActivityImgToTextBinding;

import java.io.IOException;

public class ImgToTextActivity extends AppCompatActivity {
    private Intent intentReceived;
    private MyReceiver myReceiver = new MyReceiver();
    private static final String TAG = "ImgToTextActivity";
    private ActivityImgToTextBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImgToTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        binding.storageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStorageImg();
            }
        });
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    openCamera();
                } else {
                    Log.d(TAG, "denied: ");

                }
            });

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            openCamera();

        } else if (shouldShowRequestPermissionRationale( android.Manifest.permission.CAMERA)) {
            Toast.makeText(this, "need permission", Toast.LENGTH_SHORT).show();

            requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.

            requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA);

        }
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentActivityResultLauncher.launch(intent);
    }


    private final ActivityResultLauncher<Intent> intentActivityResultLauncher =
    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
    result -> {
        intentReceived = result.getData();
        if (intentReceived != null) {
            Bundle data = intentReceived.getExtras();
            if (data != null) {
                if (intentReceived.getExtras().get("data") != null) {
                    binding.anim.setVisibility(View.GONE);
                    Bitmap bitmap = (Bitmap) intentReceived.getExtras().get("data");
                    binding.imageView.setImageBitmap(bitmap);
                    InputImage image = InputImage.fromBitmap(bitmap, 90);
                    recognizeText(image);
//                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                }
            }
        }
    });

    private void recognizeText(InputImage image) {

        // [START get_detector_default]
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        // [END get_detector_default]

        // [START run_detector]
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]
                                Log.d(TAG, "onSuccess: "+visionText.getText());
                                binding.textView.setText(visionText.getText());
                                binding.textView.setVisibility(View.VISIBLE);
                                binding.scanHeading.setVisibility(View.VISIBLE);
                                    Log.d(TAG, "onSuccess: spam"+myReceiver.isSpam(visionText.getText().toLowerCase(), ""));
                                if (myReceiver.isSpam(visionText.getText(), "")){
                                     binding.spamIndicatorScan.setVisibility(View.VISIBLE);
                                }
                                for (Text.TextBlock block : visionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();

                                    for (Text.Line line: block.getLines()) {
                                        // ...
                                        for (Text.Element element: line.getElements()) {
                                            // ...
                                            for (Text.Symbol symbol: element.getSymbols()) {
                                                // ...
                                            }
                                        }
                                    }
                                }
                                // [END get_text]
                                // [END_EXCLUDE]
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        Log.e(TAG, "onFailure: ",e );
                                        Toast.makeText(ImgToTextActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        binding.anim.setVisibility(View.VISIBLE);
                                    }
                                });
        // [END run_detector]
    }

    private void openStorageImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        dlIntent.launch(intent);
    }
    private ActivityResultLauncher<Intent> dlIntent =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Intent dlData = result.getData();
                        if (dlData != null) {
                            Uri dl = dlData.getData();
                            binding.imageView.setImageURI(dl);
                            binding.anim.setVisibility(View.GONE);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dl);
                                InputImage image = InputImage.fromBitmap(bitmap, 90);
                                recognizeText(image);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
}