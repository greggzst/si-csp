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
