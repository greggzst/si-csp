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
    private int[] domain = {0,1};

    public Binary(int n){
        board = new int[n][n];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = -1;
            }
        }
    }

    public boolean solve(){
        return solveBacktrack(board);
    }

    private boolean solveBacktrack(int[][] puzzle){
        int[] where = findFirstEmpty();
        int row = where[0];
        int col = where[1];

        if(row == -1)
            return true;

        for(Integer d : domain){
            board[row][col] = d;

            if(areConstraintsSatisfied()){
                if(solveBacktrack(puzzle)){
                    return true;
                }else{
                    board[row][col] = -1;
                }
            }
        }

        return false;
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






    private boolean areConstraintsSatisfied(){
        return true;
    }

    public void print(){
        for(int[] b : board){
            for(Integer i : b){
                if(i != -1)
                    System.out.print(i);
                else
                    System.out.print("-");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        int[][] puzzle = {
                {1,1,0,0},
                {1,0,1,0},
                {0,1,0,1},
                {0,0,1,1}
        };
        Binary b = new Binary(4);
        b.print();
        b.solve();
        b.print();
    }


}
