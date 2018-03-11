package solver;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.GridLayout;

import com.vertuoses.sudoku_solver.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Array;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by Marceau on 02/02/2018.
 */

public class Grid implements Serializable{
    private int numberOfColumns;
    private int numberOfRows;
    private int numberOfEmptyCases;
    private Activity gridActivity;
    private GridLayout gridToDisplay;
    private Square[][] squaresGrid;

    public Grid()
    {
        this.numberOfColumns=9;
        this.numberOfRows=9;
        this.numberOfEmptyCases=this.numberOfColumns * this.numberOfRows;
        squaresGrid= new Square[this.numberOfRows][this.numberOfColumns];
    }
    public Grid(Activity gridActivity)
    {
        this();
        this.gridActivity=gridActivity;
        this.gridToDisplay=gridActivity.findViewById(R.id.gridDisplay);
    }

    public Grid(int rows, int columns)
    {
        this.numberOfColumns=columns;
        this.numberOfRows=rows;
        squaresGrid= new Square[this.numberOfRows][this.numberOfColumns];
    }

    private boolean InitializeGrid()
    {
        int x;
        int y;
        try{
            for(x=0;x<this.numberOfRows;x++)
            {
                for(y=0;y<this.numberOfColumns;y++)
                {
                    this.squaresGrid[x][y] = new Square();
                }
            }
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public int GetNumberOfEmptySquares()
    {
        return this.numberOfEmptyCases;
    }

    private int CountEmptySquares()
    {
        int emptySquares = 0;
        for(int x=0;x<this.numberOfRows;x++)
        {
            for(int y=0;y<this.numberOfColumns;y++)
            {
                if(this.squaresGrid[x][y].GetValue()==0)
                {
                    emptySquares++;
                }
            }
        }

        return emptySquares;
    }

    public boolean CheckValuesInLine(int line, String kind)
    {
        List<Integer> valuesInLine = new LinkedList<>();

        for(int y=0;y<this.numberOfColumns; y++)
        {
            if(this.squaresGrid[line][y].GetValue()!=0)
            {
                valuesInLine.add(this.squaresGrid[line][y].GetValue());
            }
        }

        if(valuesInLine.size()==this.numberOfColumns)
        {
            return true;
        }
        else
        {
            for(int y = this.numberOfColumns; y>0;y--)
            {
                for(int value : valuesInLine)
                {
                    this.squaresGrid[line][y].RemoveValueFromPossibilities(value);
                    if(this.squaresGrid[line][y].HasOnlyOnePossibility())
                    {
                        this.squaresGrid[line][y].SetLastPossibleValue();
                    }
                }
            }
        }

        return false;
    }

    public boolean CheckValuesInColumn(int column)
    {
        List<Integer> valuesInColumn = new LinkedList<>();

        for(int x=0;x<this.numberOfRows; x++)
        {
            if(this.squaresGrid[x][column].GetValue()!=0)
            {
                valuesInColumn.add(this.squaresGrid[x][column].GetValue());
            }
        }

        if(valuesInColumn.size()==this.numberOfColumns)
        {
            return true;
        }
        else
        {
            for(int x = this.numberOfRows; x>0;x--)
            {
                for(int value : valuesInColumn)
                {
                    this.squaresGrid[x][column].RemoveValueFromPossibilities(value);
                    if(this.squaresGrid[x][column].HasOnlyOnePossibility())
                    {
                        this.squaresGrid[x][column].SetLastPossibleValue();
                    }
                }
            }
        }

        return false;
    }

    public boolean GetGridFromText(String filePath)
    {
        File gridFile = new File(filePath);
        if(gridFile.exists())
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(gridFile));
                String line;
                int x=0;

                while((line = br.readLine())!=null)
                {
                    String[] numbersInLine = line.split("[?!|]");
                    if(numbersInLine.length==9)
                    {
                        for(int i=0;i<numbersInLine.length;i++)
                        {
                            this.squaresGrid[x][i]=new Square(Integer.parseInt(numbersInLine[i]),this.gridActivity);
                        }
                        x++;
                    }
                    else
                    {
                        return false;
                    }
                }
                return true;
            }
            catch(Exception e)
            {
                Log.e("Error","Invalid grid");
            }
        }
        return false;
    }

    public boolean FillGridLayoutWithGrid()
    {
        this.gridToDisplay.setRowCount(this.numberOfRows);
        this.gridToDisplay.setColumnCount(this.numberOfColumns);
        this.gridToDisplay.setBackground(this.gridActivity.getResources().getDrawable(R.drawable.border));

        SetGridPadding(5,this.gridToDisplay);

        for(int x=0;x<this.numberOfRows;x=x+3)
        {
            for(int y=0;y<this.numberOfColumns;y=y+3)
            {
                this.gridToDisplay.addView(CreateBigSquareLayout(x,y));
            }
        }

        return true;
    }

    public GridLayout GetGridToDisplay()
    {
        return this.gridToDisplay;
    }

    public void SetGridToDisplay(GridLayout gridLayout)
    {
        this.gridToDisplay=gridLayout;
    }

    private GridLayout CreateBigSquareLayout(int positionX,int positionY)
    {
        GridLayout gridLayout = new GridLayout(this.gridActivity);
        gridLayout.setBackground(this.gridActivity.getResources().getDrawable(R.drawable.borderbigsquare));
        gridLayout.setRowCount(3);
        gridLayout.setColumnCount(3);
        for(int x=positionX;x<positionX+3;x++)
        {
            for(int y=positionY;y<positionY+3;y++)
            {
                gridLayout.addView(this.squaresGrid[x][y].GetTextView());
            }
        }

        SetGridPadding(3,gridLayout);

        GridLayout.LayoutParams parameters = new GridLayout.LayoutParams();
        parameters.columnSpec=GridLayout.spec(positionX/3);
        parameters.rowSpec=GridLayout.spec(positionY/3);
        gridLayout.setLayoutParams(parameters);

        return gridLayout;
    }

    private void SetGridPadding(int dpPadding, GridLayout gridLayout)
    {
        int padding = Square.GetDpToPixels(dpPadding,this.gridActivity);
        gridLayout.setPadding(padding,padding,padding,padding);
    }
}
