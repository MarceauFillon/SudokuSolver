package com.vertuoses.sudoku_solver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import solver.Grid;

public class GridDisplay extends AppCompatActivity {
    GridLayout gridLayout = null;
    Grid grid = null;
    Button resolveButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_display);

        resolveButton = findViewById(R.id.resolveButton);
        resolveButton.setOnClickListener(resolveButtonListener);
        Intent intent = getIntent();

        Grid temporaryGrid = (Grid) intent.getSerializableExtra("grid");

        if (temporaryGrid.GetNumberOfEmptySquares() == 81) {
            grid = new Grid(this);

            grid.GetGridFromText(intent.getSerializableExtra("pathToGrid").toString());
        } else {
            grid = temporaryGrid;
        }

        gridLayout = findViewById(R.id.gridDisplay);
        grid.SetGridToDisplay(gridLayout);
        grid.FillGridLayoutWithGrid();
    }

    private View.OnClickListener resolveButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            grid.RunThrewGrid();
        }
    };
}
