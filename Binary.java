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

}
