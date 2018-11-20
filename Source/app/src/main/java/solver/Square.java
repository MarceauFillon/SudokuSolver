package solver;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.vertuoses.sudoku_solver.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Marceau on 02/02/2018.
 */

public class Square {
    private List<Integer> possibleValues;
    private int value;
    private EditText textView;
    private int viewID;

    public Square()
    {
        FillPossiblesValues();

        value = 0;
    }

    public Square(int value, Activity activity)
    {
        int pixels = GetDpToPixels(36.5,activity);

        this.value=value;
        this.textView = new EditText(activity);
        this.textView.setWidth(pixels);
        this.textView.setHeight(pixels);
        this.textView.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(1);
        this.textView.setFilters(fArray);
        this.textView.setBackground(activity.getResources().getDrawable(R.drawable.bordersquare));
        this.textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        int padding = GetDpToPixels(0.5,activity);
        this.textView.setPadding(padding,padding,padding,padding);
        this.viewID = this.textView.generateViewId();
        this.textView.setId(this.viewID);

        if(value!=0)
        {
            this.textView.setText(String.valueOf(value));
            this.textView.setEnabled(false);
            this.textView.setTextColor(Color.parseColor("#990000"));
        }
        else
        {
            this.textView.setEnabled(false);
            FillPossiblesValues();
        }
    }

    public int GetValue()
    {
        return this.value;
    }

    public boolean SetValue(int value, Activity gridActivity)
    {
        boolean res = false;
        try{
            if(value>0 && value<10 && this.possibleValues.contains(value))
            {
                this.value=value;
                this.possibleValues.clear();

                TextView temporaryView = gridActivity.findViewById(this.GetViewID());
                temporaryView.setText(String.valueOf(this.value));
                temporaryView.setEnabled(false);
                temporaryView.setBackgroundColor(Color.RED);
                res = true;
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, "SetValue failed");
        }

        return res;
    }

    public List<Integer> GetPossibleValues()
    {
        return this.possibleValues;
    }

    public boolean HasOnlyOnePossibility()
    {
        if(this.possibleValues!=null && this.possibleValues.size()==1)
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
                this.possibleValues.remove(this.possibleValues.indexOf(valueToRemove));
                return true;
            }

        }
        catch (Exception ex)
        {

        }

        return false;

    }

    public boolean SetLastPossibleValue(Activity gridActivity)
    {
        if(!this.HasOnlyOnePossibility() || this.possibleValues.isEmpty())
        {
            return false;
        }
        else
        {
            this.SetValue(this.possibleValues.get(0),gridActivity);
            this.possibleValues.clear();
            return true;
        }
    }

    public TextView GetTextView(int positionX, int positionY)
    {
        GridLayout.LayoutParams parameters = new GridLayout.LayoutParams();
        parameters.columnSpec=GridLayout.spec(positionY);
        parameters.rowSpec=GridLayout.spec(positionX);
        this.textView.setLayoutParams(parameters);
        return this.textView;
    }

    public void SetValueInTextView(int value)
    {
        this.textView.setText(String.valueOf(value));
        this.textView.setEnabled(false);
    }

    public int GetViewID()
    {
        return viewID;
    }

    static public int GetDpToPixels(double sizeIndDp, Activity activity)
    {
        final float scale = activity.getResources().getDisplayMetrics().density;
        int pixels = (int) (sizeIndDp * scale + 0.5f);
        return pixels;
    }

    private void FillPossiblesValues()
    {
        this.possibleValues = new ArrayList<>();
        for(int i=1;i<10;i++)
        {
            this.possibleValues.add(i);
        }
    }


}
