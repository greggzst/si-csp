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

    public Binary(int n){
        board = new int[n][n];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = -1;
            }
        }
    }

    private int[] findFirstEmpty(){
        int[] where = {-1, -1};
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board.length; col++){
                if(board[row][col] == -1){
                    where[0] = row;
                    where[1] = col;
                    return where;
                }
            }
        }

        return where;
    }

}
