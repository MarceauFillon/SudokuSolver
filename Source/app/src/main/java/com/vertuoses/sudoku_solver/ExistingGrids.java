package com.vertuoses.sudoku_solver;

import android.app.Activity;
import android.os.Bundle;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.content.*;
import solver.Grid;


public class ExistingGrids extends AppCompatActivity {

    ListView gridList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_grids);

        gridList = findViewById(R.id.gridList);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/SudokuGrids";

        File directory = new File(path);
        directory.setReadable(true);
        String[] files = directory.list();

        List<HashMap<String, String>> gridsList = new ArrayList<>();

        HashMap<String, String> gridHashMap;

        for (int i = 0; i < files.length; i++)
        {
            gridHashMap = new HashMap<>();
            gridHashMap.put("gridNumber", "Grid  " + i);
            gridHashMap.put("name", files[i]);
            gridHashMap.put("path",path+"/"+files[i]);
            gridsList.add(gridHashMap);
        }

        ListAdapter adapter = new SimpleAdapter(this, gridsList, android.R.layout.simple_list_item_2, new String[]{"gridNumber","name"}, new int[]{android.R.id.text1, android.R.id.text2});

        gridList.setAdapter(adapter);

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> gridItem = (HashMap<String, String>) gridList.getAdapter().getItem(position);

                Intent intent = new Intent(ExistingGrids.this, GridDisplay.class);
                intent.putExtra("pathToGrid",gridItem.get("path"));

                startActivity(intent);
            }
        });
    }


}
