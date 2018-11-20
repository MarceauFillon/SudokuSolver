package com.vertuoses.sudoku_solver;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

import solver.Grid;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GridTest {
    @Mock
    List<Integer>
    @Test
    public void checkValuesInLine_NoValuesInLine() throws Exception {
        Grid testGrid = new Grid();
        testGrid.GetGridFromText("C:\\Users\\Marceau\\Documents\\Sudokus\\Sudoku_Test.txt");


    }
}