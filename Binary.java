import java.util.Random;

/**
 * Created by GreggJakubiak on 29.03.2017.
 */
public class Binary {
    private int[][] board;
    private int[] domain = {0, 1};

    public Binary(int[][] puzzle){
        board = puzzle;
        isColNumberOnesEqual();
        isRowNumberOnesEqual();
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

   private int numOfZerosInCol(int col){
        return board.length - numOfOnesInCol(col);
   }

   private int numOfZerosInRow(int row){
       return board.length - numOfOnesInRow(row);
   }

    private int numOfOnesInCol(int col){
        int numOfOnes = 0;
        for(int i = 0; i < board.length; i++){
            if(board[i][col] == 1)
                numOfOnes++;
        }

        return numOfOnes;
    }

    private int numOfOnesInRow(int row){
        int numOfOnes = 0;
        for(int i = 0; i < board.length; i++){
            if(board[row][i] == 1)
                numOfOnes++;
        }

        return numOfOnes;
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
