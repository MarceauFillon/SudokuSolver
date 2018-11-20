package solver;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import com.vertuoses.sudoku_solver.GridDisplay;
import com.vertuoses.sudoku_solver.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.ContentValues.TAG;

/**
 * Created by Marceau on 02/02/2018.
 */

public class Grid implements Serializable {
    private int numberOfColumns;
    private int numberOfRows;
    private int numberOfEmptyCases;
    private Activity gridActivity;
    private GridLayout gridToDisplay;
    private Square[][] squaresGrid;

    public Grid() {
        this.numberOfColumns = 9;
        this.numberOfRows = 9;
        this.numberOfEmptyCases = this.numberOfColumns * this.numberOfRows;
        squaresGrid = new Square[this.numberOfRows][this.numberOfColumns];
    }

    public Grid(Activity gridActivity) {
        this();
        this.gridActivity = gridActivity;
        this.gridToDisplay = gridActivity.findViewById(R.id.gridDisplay);
    }

    public Grid(int rows, int columns) {
        this.numberOfColumns = columns;
        this.numberOfRows = rows;
        squaresGrid = new Square[this.numberOfRows][this.numberOfColumns];
    }

    private boolean InitializeGrid() {
        int x;
        int y;
        try {
            for (x = 0; x < this.numberOfRows; x++) {
                for (y = 0; y < this.numberOfColumns; y++) {
                    this.squaresGrid[x][y] = new Square();
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private int CountEmptySquares() {
        int emptySquares = 0;
        for (int x = 0; x < this.numberOfRows; x++) {
            for (int y = 0; y < this.numberOfColumns; y++) {
                if (this.squaresGrid[x][y].GetValue() == 0) {
                    emptySquares++;
                }
            }
        }

        return emptySquares;
    }

    public boolean CheckValuesInLine(int line) {
        List<Integer> valuesInLine = new ArrayList<>();

        for (int y = 0; y < this.numberOfColumns; y++) {
            if (this.squaresGrid[line][y].GetValue() != 0) {
                valuesInLine.add(this.squaresGrid[line][y].GetValue());
            }
        }

        if (valuesInLine.size() == this.numberOfColumns) {
            return true;
        } else {
            for (int y = this.numberOfColumns - 1; y >= 0; y--) {
                for (int value : valuesInLine) {
                    this.squaresGrid[line][y].RemoveValueFromPossibilities(value);
                }
            }
        }

        return false;
    }

    public boolean CheckValuesInColumn(int column) {
        List<Integer> valuesInColumn = new ArrayList<>();

        for (int x = 0; x < this.numberOfRows; x++) {
            if (this.squaresGrid[x][column].GetValue() != 0) {
                valuesInColumn.add(this.squaresGrid[x][column].GetValue());
            }
        }

        if (valuesInColumn.size() == this.numberOfColumns) {
            return true;
        } else {
            for (int x = this.numberOfRows - 1; x >= 0; x--) {
                for (int value : valuesInColumn) {
                    this.squaresGrid[x][column].RemoveValueFromPossibilities(value);
                }
            }
        }

        return false;
    }

    public boolean CheckValuesInBigSquare(int xStart, int yStart) {
        List<Integer> valuesInBigSquare = new ArrayList<>();

        for (int x = xStart; x < xStart + 3; x++) {
            for (int y = yStart; y < yStart + 3; y++) {
                if (this.squaresGrid[x][y].GetValue() != 0) {
                    valuesInBigSquare.add(this.squaresGrid[x][y].GetValue());
                }
            }
        }

        for (int x = xStart; x < xStart + 3; x++) {
            for (int y = yStart; y < yStart + 3; y++) {
                for (int value : valuesInBigSquare) {
                    this.squaresGrid[x][y].RemoveValueFromPossibilities(value);
                }
            }
        }

        return true;
    }

    public int CheckPossibleValuesInLine(int x, int y) {
        HashSet<Integer> possibleValuesInLine = new HashSet<>();
        HashSet<Integer> possibleValuesInSquare = new HashSet<>(this.squaresGrid[x][y].GetPossibleValues());

        for (int j = 0; j < this.numberOfColumns; j++) {
            if (j != y && this.squaresGrid[x][j].GetPossibleValues() != null) {
                if(this.squaresGrid[x][j].GetValue()!=0)
                {
                    possibleValuesInLine.add(this.squaresGrid[x][j].GetValue());
                }
                else
                {
                    possibleValuesInLine.addAll(this.squaresGrid[x][j].GetPossibleValues());
                }
            }
        }

        possibleValuesInSquare.removeAll(possibleValuesInLine);
        if (!possibleValuesInSquare.isEmpty() && possibleValuesInSquare.size() == 1) {
            return Integer.parseInt(possibleValuesInSquare.toArray()[0].toString());
        }

        return 0;
    }

    public int CheckPossibleValuesInColumn(int x, int y) {
        HashSet<Integer> possibleValuesInColumn = new HashSet<>();
        HashSet<Integer> possibleValuesInSquare = new HashSet<>(this.squaresGrid[x][y].GetPossibleValues());

        for (int i = 0; i < this.numberOfRows; i++) {
            if (i != x && this.squaresGrid[i][y].GetPossibleValues() != null) {
                if(this.squaresGrid[i][y].GetValue()!=0)
                {
                    possibleValuesInColumn.add(this.squaresGrid[i][y].GetValue());
                }
                else
                {
                    possibleValuesInColumn.addAll(this.squaresGrid[i][y].GetPossibleValues());
                }
            }
        }

        possibleValuesInSquare.removeAll(possibleValuesInColumn);
        if (!possibleValuesInSquare.isEmpty() && possibleValuesInSquare.size() == 1) {
            return Integer.parseInt(possibleValuesInSquare.toArray()[0].toString());
        }

        return 0;
    }

    public int CheckPossibleValuesInSquare(int x, int y) {
        HashSet<Integer> possibleValuesInBigSquare = new HashSet<>();
        HashSet<Integer> possibleValuesInSquare = new HashSet<>(this.squaresGrid[x][y].GetPossibleValues());

        int xStart = x - x % 3;
        int yStart = y - y % 3;

        for (int i = xStart; i < xStart + 3; i++) {
            for (int j = yStart; j < yStart + 3; j++) {
                if ((i != x || j != y) && this.squaresGrid[i][j].GetPossibleValues() != null) {
                    if (this.squaresGrid[i][j].GetValue() != 0) {
                        possibleValuesInBigSquare.add(this.squaresGrid[i][j].GetValue());
                    } else {
                        possibleValuesInBigSquare.addAll(this.squaresGrid[i][j].GetPossibleValues());
                    }
                }
            }
        }

        possibleValuesInSquare.removeAll(possibleValuesInBigSquare);
        if (!possibleValuesInSquare.isEmpty() && possibleValuesInSquare.size() == 1) {
            return Integer.parseInt(possibleValuesInSquare.toArray()[0].toString());
        }

        return 0;
    }

    public boolean GetGridFromText(String filePath) {
        File gridFile = new File(filePath);
        Integer counter = 0;
        if (gridFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(gridFile));
                String line;
                int x = 0;

                while ((line = br.readLine()) != null) {
                    String[] numbersInLine = line.split("[?!|]");
                    if (numbersInLine.length == 9) {
                        for (int i = 0; i < numbersInLine.length; i++) {
                            this.squaresGrid[x][i] = new Square(Integer.parseInt(numbersInLine[i]), this.gridActivity);
                            if (this.squaresGrid[x][i].GetValue() != 0) {
                                counter++;
                            }
                        }
                        x++;
                    } else {
                        return false;
                    }
                }
                this.numberOfEmptyCases = this.numberOfEmptyCases - counter;
                return true;
            } catch (Exception e) {
                Log.e("Error", "Invalid grid");
            }
        }
        return false;
    }

    public boolean FillGridLayoutWithGrid() {
        this.gridToDisplay.removeAllViews();
        this.gridToDisplay.setRowCount((int) Math.pow(this.numberOfRows, 0.5));
        this.gridToDisplay.setColumnCount((int) Math.pow(this.numberOfColumns, 0.5));
        this.gridToDisplay.setBackground(this.gridActivity.getResources().getDrawable(R.drawable.border));

        SetGridPadding(5, this.gridToDisplay);

        for (int x = 0; x < this.gridToDisplay.getRowCount(); x++) {
            for (int y = 0; y < this.gridToDisplay.getColumnCount(); y++) {
                this.gridToDisplay.addView(CreateBigSquareLayout(x, y));
            }
        }

        return true;
    }

    public boolean RunThrewGrid() {
        for (int x = 0; x < this.numberOfRows; x++) {
            try {
                CheckValuesInLine(x);
            } catch (Exception e) {
                Log.e(TAG, "Failure while checking the rows");
            }
        }

        for (int y = 0; y < this.numberOfColumns; y++) {
            try {
                CheckValuesInColumn(y);
            } catch (Exception e) {
                Log.e(TAG, "Failure while checking the columns");
            }
        }

        for (int x = 0; x < this.numberOfRows; x = x + 3) {
            for (int y = 0; y < this.numberOfColumns; y = y + 3) {
                try {
                    CheckValuesInBigSquare(x, y);
                } catch (Exception e) {
                    Log.e(TAG, "Failure while checking the squares");
                }
            }
        }

        for (int x = 0; x < this.numberOfRows; x++) {
            for (int y = 0; y < this.numberOfColumns; y++) {
                try {
                    if(x==3 && y ==5)
                    {
                        x = 3;
                    }
                    int value = 0;
                    if (this.squaresGrid[x][y].GetValue() == 0) {
                        if (this.squaresGrid[x][y].HasOnlyOnePossibility()) {
                            this.squaresGrid[x][y].SetLastPossibleValue(this.gridActivity);
                            continue;
                        }
                        if ((value = CheckPossibleValuesInSquare(x, y)) != 0) {
                            this.squaresGrid[x][y].SetValue(value, this.gridActivity);
                            continue;
                        }
                        if ((value = CheckPossibleValuesInColumn(x, y)) != 0) {
                            this.squaresGrid[x][y].SetValue(value, this.gridActivity);
                            continue;
                        }
                        if ((value = CheckPossibleValuesInLine(x, y)) != 0) {
                            this.squaresGrid[x][y].SetValue(value, this.gridActivity);
                            continue;
                        }

                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failure while checking the values");
                }
            }
        }

        return true;
    }

    private GridLayout CreateBigSquareLayout(int positionX, int positionY) {
        int xInGrid = positionX * this.gridToDisplay.getRowCount();
        int yInGrid = positionY * this.gridToDisplay.getColumnCount();

        GridLayout gridLayout = new GridLayout(this.gridActivity);
        gridLayout.setBackground(this.gridActivity.getResources().getDrawable(R.drawable.borderbigsquare));
        gridLayout.setRowCount(3);
        gridLayout.setColumnCount(3);
        for (int x = xInGrid; x < xInGrid + 3; x++) {
            for (int y = yInGrid; y < yInGrid + 3; y++) {
                gridLayout.addView(this.squaresGrid[x][y].GetTextView(x - 3 * positionX, y - 3 * positionY));
            }
        }

        SetGridPadding(3, gridLayout);

        GridLayout.LayoutParams parameters = new GridLayout.LayoutParams();
        parameters.columnSpec = GridLayout.spec(positionY);
        parameters.rowSpec = GridLayout.spec(positionX);
        gridLayout.setLayoutParams(parameters);

        return gridLayout;
    }

    private void SetGridPadding(int dpPadding, GridLayout gridLayout) {
        int padding = Square.GetDpToPixels(dpPadding, this.gridActivity);
        gridLayout.setPadding(padding, padding, padding, padding);
    }

    public GridLayout GetGridToDisplay() {
        return this.gridToDisplay;
    }

    public void SetGridToDisplay(GridLayout gridLayout) {
        this.gridToDisplay = gridLayout;
    }

    public int GetNumberOfEmptySquares() {
        return this.numberOfEmptyCases;
    }
}
