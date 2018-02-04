package solver;

import java.util.List;

/**
 * Created by Marceau on 02/02/2018.
 */

public class Square {
    private List<Integer> possibleValues;
    private int value;

    public Square()
    {
        for(int i=0;i<10;i++)
        {
           this.possibleValues.add(i);
        }

        value = 0;
    }

    public Square(int value)
    {
        this.value=value;
    }

    public int GetValue()
    {
        return this.value;
    }

    public boolean SetValue(int value)
    {
        if(value>0 && value<10 && this.possibleValues.contains(value))
        {
            this.value=value;
            this.possibleValues.clear();
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<Integer> GetPossibleValues()
    {
        return this.possibleValues;
    }

    public boolean HasOnlyOnePossibility()
    {
        if(this.possibleValues.size()==1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean RemoveValueFromPossibilities(int valueToRemove)
    {
        try
        {
            if(this.possibleValues.contains(valueToRemove))
            {
                this.possibleValues.remove(valueToRemove);
                return true;
            }

        }
        catch (Exception ex)
        {

        }

        return false;

    }

    public boolean SetLastPossibleValue()
    {
        if(!this.HasOnlyOnePossibility() || this.possibleValues.isEmpty())
        {
            return false;
        }
        else
        {
            this.value=this.possibleValues.get(0);
            this.possibleValues.clear();
            return true;
        }
    }

}
