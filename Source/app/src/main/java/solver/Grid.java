package solver;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by Marceau on 02/02/2018.
 */

public class Grid {
    private int numberOfColumns;
    private int numberOfRows;
    private int numberOfEmptyCases;
    private Square[][] squaresGrid;

    public Grid()
    {
        this.numberOfColumns=9;
        this.numberOfRows=9;
        this.numberOfEmptyCases=this.numberOfColumns * this.numberOfRows;
        squaresGrid= new Square[this.numberOfRows][this.numberOfColumns];
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
}
