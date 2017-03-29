import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by GreggJakubiak on 29.03.2017.
 */
public class Binary {
    private int[][] board;
    private int[] domain = {0, 1};

    public Binary(int[][] puzzle){
        board = puzzle;
    }

    private boolean isSolved(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == -1)
                    return false;
            }
        }

        return true;
    }

    private boolean constrainsSatisfied(){
        return areRowsUnique() && areColsUnique() && areOnlyTwoSymbolsNextToInCol()
                && areOnlyTwoSymbolsNextToInRow() && isRowNumberOnesEqual() && isColNumberOnesEqual();
    }

    private boolean areOnlyTwoSymbolsNextToInRow(){
        for(int i = 0; i < board.length; i++){
            int numOfElems = 0;
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == 1)
                    numOfElems++;
                else
                    numOfElems--;
            }
            if(numOfElems == 3 || numOfElems == -3)
                return false;
        }
        return true;
    }

    private boolean areOnlyTwoSymbolsNextToInCol(){
        for(int i = 0; i < board.length; i++){
            int numOfElems = 0;
            for(int j = 0; j < board[0].length; j++){
                if(board[j][i] == 1)
                    numOfElems++;
                else
                    numOfElems--;
            }
            if(numOfElems == 3 || numOfElems == -3)
                return false;
        }
        return true;
    }

    private boolean areRowsUnique(){
        for(int i = 0; i < board.length - 1; i++){
            if(Arrays.equals(board[i],board[i+1]))
                return false;
        }

        return true;
    }

    private boolean areColsUnique(){
        List<int[]> columnList = new ArrayList<>();
        for(int i = 0; i < board.length; i++){
            int[] col = new int[board.length];
            for(int j = 0; j < board.length; j++){
                col[j] = board[j][i];
            }
            columnList.add(col);
        }

        for(int i = 0; i < columnList.size() - 1; i++){
            if(Arrays.equals(columnList.get(i), columnList.get(i + 1)))
                return false;
        }

        return true;
    }

    private boolean isRowNumberOnesEqual(){
        int[] rowOnesNumber = new int[board.length];
        for (int i = 0; i < board.length; i++){
            int numOfOnes = 0;
            for (int j = 0; j < board[0].length; j++){
                if(board[i][j] == 1)
                    numOfOnes++;
            }
            rowOnesNumber[i] = numOfOnes;
        }

        int num = rowOnesNumber[0];
        for (int i = 1; i < rowOnesNumber.length; i++){
            if(num != rowOnesNumber[i])
                return false;
        }

        return true;
    }

    private boolean isColNumberOnesEqual(){
        int[] colOnesNumber = new int[board.length];
        for (int i = 0; i < board.length; i++){
            int numOfOnes = 0;
            for (int j = 0; j < board[0].length; j++){
                if(board[j][i] == 1)
                    numOfOnes++;
            }
            colOnesNumber[i] = numOfOnes;
        }

        int num = colOnesNumber[0];
        for (int i = 1; i < colOnesNumber.length; i++){
            if(num != colOnesNumber[i])
                return false;
        }

        return true;
    }


    public static void main(String[] args){
        int[][] puzzle = {
                {1,1,0,0},
                {1,0,1,0},
                {0,1,0,1},
                {0,0,1,1}
        };
        Binary b = new Binary(puzzle);
    }

}
