package com.vertuoses.sudoku_solver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import solver.Grid;

public class GridDisplay extends AppCompatActivity {
    GridLayout gridLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_display);

        Intent intent = getIntent();
        Grid grid =  new Grid(this);

        grid.GetGridFromText(intent.getSerializableExtra("pathToGrid").toString());

        gridLayout = findViewById(R.id.gridDisplay);
        grid.SetGridToDisplay(gridLayout);
        grid.FillGridLayoutWithGrid();
    }
}
