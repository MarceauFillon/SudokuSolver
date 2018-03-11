package com.vertuoses.sudoku_solver;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;

public class MainActivity extends AppCompatActivity {
    static final int CAMERA_PIC_REQUEST = 1337;
    Button cameraButton = null;
    Button gridsButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraButton = findViewById(R.id.cameraButton);
        gridsButton = findViewById(R.id.gridsButton);

        cameraButton.setOnClickListener(cameraButtonListener);
        gridsButton.setOnClickListener(gridsButtonListener);
    }

    protected ViewStub.OnClickListener cameraButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
    };

    protected ViewStub.OnClickListener gridsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,ExistingGrids.class);
            startActivity(intent);

        }
    };


}
