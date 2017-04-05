import java.util.*;

/**
 * Created by GreggJakubiak on 29.03.2017.
 */
public class Binary {
    private int[][] board;
    private List<String> domain;

    public Binary(int n){
        board = new int[n][n];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = -1;
            }
        }
        domain = new ArrayList<>();
        domain.add("1");
        domain.add("0");
    }

    public Binary(int n, int m){
        board = new int[n][n];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = -1;
            }
        }
        domain = new ArrayList<>();
        domain.add("1");
        domain.add("0");
        fillRandomFields(m);
    }

    private void fillRandomFields(int m){
        Random random = new Random();
        for(int i = 0; i < m; i++){
            int row = (int) (random.nextDouble() * board.length);
            int col = (int) (random.nextDouble() * board.length);

            for(String d : domain){
                if(areConstraintsSatisfied(row,col,Integer.parseInt(d))){
                    board[row][col] = Integer.parseInt(d);
                }
            }
        }
    }

    public boolean solveForwardChecking(){
        return solveForwardChecking(board);
    }

    private boolean solveForwardChecking(int[][] puzzle){
        int[] where = findFirstEmpty();
        int row = where[0];
        int col = where[1];

        if(row == -1)
            return true;
        Iterator<String> domainIterator = domain.iterator();
        while(domainIterator.hasNext()){
            String d = domainIterator.next();
            if(areConstraintsSatisfied(row,col,Integer.parseInt(d))){
                board[row][col] = Integer.parseInt(d);

                if(solveBacktrack(puzzle)){
                    return true;
                }else{
                    board[row][col] = -1;
                }
            }

            domainIterator.remove();
        }

        return false;
    }

    public boolean solveBacktrack(){
        return solveBacktrack(board);
    }

    private boolean solveBacktrack(int[][] puzzle){
        int[] where = findFirstEmpty();
        int row = where[0];
        int col = where[1];

        if(row == -1)
            return true;

        for(String d : domain){
            if(areConstraintsSatisfied(row,col,Integer.parseInt(d))){
                board[row][col] = Integer.parseInt(d);

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

    private boolean areRowAndColUnique(int row, int col, int symbol){
        board[row][col] = symbol;
        int[] rowOfInsertion = getRow(row);
        int[] colOfInsertion = getCol(col);

        boolean rowHasEmptyField = hasEmptyField(rowOfInsertion);
        boolean colHasEmptyField = hasEmptyField(colOfInsertion);

        for(int i = 0; i < board.length; i++){
            if(!rowHasEmptyField){
                if(i != row){
                    int[] rowToCompare = getRow(i);
                    if(Arrays.equals(rowOfInsertion,rowToCompare)){
                        board[row][col] = -1;
                        return false;
                    }
                }
            }

            if(!colHasEmptyField){
                if(i != col){
                    int[] colToCompare = getCol(i);
                    if(Arrays.equals(colOfInsertion,colToCompare)){
                        board[row][col] = -1;
                        return false;
                    }
                }
            }
        }

        board[row][col] = -1;
        return true;
    }

    private boolean hasEmptyField(int[] array){
        for(int i = 0; i < array.length; i++){
            if(array[i] == -1)
                return true;
        }

        return false;
    }

    private int[] getCol(int col){
        int[] column = new int[board.length];
        for(int i = 0; i < board.length; i++){
            column[i] = board[i][col];
        }

        return column;
    }

    private int[] getRow(int rowNum){
        return board[rowNum];
    }

    private int countSymbolOccurence(int[] array, int symbol){
        int occurence = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i] == symbol)
                occurence++;
        }
        return occurence;
    }

    private boolean isNumberOfSymbolsInRowAndColConsistent(int row, int col,int symbol){
        board[row][col] = symbol;

        int[] rowOfInsertion = getRow(row);
        int[] colOfInsertion = getCol(col);

        boolean rowHasEmptyField = hasEmptyField(rowOfInsertion);
        boolean colHasEmptyField = hasEmptyField(colOfInsertion);

        if(rowHasEmptyField || colHasEmptyField){
            board[row][col] = -1;
            return true;
        }

        int numOfZerosRow = countSymbolOccurence(rowOfInsertion,0);
        int numOfOnesRow = countSymbolOccurence(rowOfInsertion,1);
        int numOfZerosCol = countSymbolOccurence(colOfInsertion,0);
        int numOfOnesCol = countSymbolOccurence(colOfInsertion,1);

        for(int i = 0; i < row; i++){
            if(!rowHasEmptyField){
                if(i != row){
                    int[] rowToCompare = getRow(i);
                    int numOfZeros = countSymbolOccurence(rowToCompare,0);
                    int numOfOnes = countSymbolOccurence(rowToCompare,1);

                    if(numOfOnes != numOfOnesRow || numOfZeros != numOfZerosRow){
                        board[row][col] = -1;
                        return false;
                    }
                }
            }
        }

        for(int i = 0; i < col; i++){
            if(!colHasEmptyField){
                if(i != col){
                    int[] colToCompare = getCol(i);
                    int numOfZeros = countSymbolOccurence(colToCompare,0);
                    int numOfOnes = countSymbolOccurence(colToCompare,1);

                    if(numOfOnes != numOfOnesCol || numOfZeros != numOfZerosCol){
                        board[row][col] = -1;
                        return false;
                    }
                }
            }
        }
        board[row][col] = -1;
        return true;

    }


    private boolean isNumberOfSymbolsNextToInRowAndColConsistent(int row, int col, int symbol){
        int symRowOccurence = 0;
        int symColOccurence = 0;

        board[row][col] = symbol;
        for(int i = 0; i < board.length; i++){
            if(board[row][i] == symbol){
                symRowOccurence++;
            }else{
                symRowOccurence = 0;
            }

            if(symRowOccurence >= 3){
                board[row][col] = -1;
                return false;
            }

            if(board[i][col] == symbol){
                symColOccurence++;
            }else{
                symColOccurence = 0;
            }

            if(symColOccurence >= 3){
                board[row][col] = -1;
                return false;
            }
        }
        board[row][col] = -1;
        return true;
    }

    private boolean nHasOnesAndZeros(int row, int col, int symbol){
        board[row][col] = symbol;

        int[] rowOfInsertion = getRow(row);
        int[] colOfInsertion = getCol(col);

        int zerosInRow = countSymbolOccurence(rowOfInsertion,0);
        int onesInRow = countSymbolOccurence(rowOfInsertion,1);
        int zerosInCol = countSymbolOccurence(colOfInsertion,0);
        int onesInCol = countSymbolOccurence(colOfInsertion,1);

        board[row][col] = -1;

        int n = board.length % 2 == 0 ? board.length / 2 : board.length;

        if(zerosInRow > n || zerosInCol > n|| onesInRow > n || onesInCol > n){
            return false;
        }

        return true;

    }


    private boolean areConstraintsSatisfied(int row, int col, int symbol){
        return isNumberOfSymbolsNextToInRowAndColConsistent(row,col,symbol)
                && isNumberOfSymbolsInRowAndColConsistent(row,col,symbol)
                && areRowAndColUnique(row,col,symbol)
                && nHasOnesAndZeros(row,col,symbol);
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
        Binary b = new Binary(8,8);
        b.print();
        b.solveBacktrack();
        b.print();
    }


}
