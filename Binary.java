import java.util.*;

/**
 * Created by GreggJakubiak on 29.03.2017.
 */
public class Binary {
    private int[][] board;
    private int[] domain = {1, 0};
    private HashMap<int[],List<String>> variableDomains;

    public Binary(int n){
        board = new int[n][n];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = -1;
            }
        }

        variableDomains = prepareDomainsForVariables();
    }

    public Binary(int n, int m){
        board = new int[n][n];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = -1;
            }
        }
        fillRandomFields(m);
        variableDomains = prepareDomainsForVariables();
    }

    private void fillRandomFields(int m){
        Random random = new Random();
        for(int i = 0; i < m; i++){
            int row = (int) (random.nextDouble() * board.length);
            int col = (int) (random.nextDouble() * board.length);

            while(board[row][col] != -1){
                row = (int) (random.nextDouble() * board.length);
                col = (int) (random.nextDouble() * board.length);
            }

            int symbol = (int) (random.nextDouble() * 2);

            while(board[row][col] == -1){
                if(areConstraintsSatisfied(row,col,symbol)){
                    board[row][col] = symbol;
                }
                symbol = (int) (random.nextDouble() * 2);
            }
        }
    }

    public boolean solveForwardChecking(){
        int[] where = findFirstEmpty();
        int row = where[0];
        int col = where[1];

        if(row == -1)
            return true;

        List<String> variableDomain = getDomain(where);

        for(String d : variableDomain){
            int symbol = Integer.parseInt(d);
            if(areConstraintsSatisfied(row,col,symbol)){
                board[row][col] = symbol;
                HashMap<int[],List<String>> copy = copyVariableDomains(variableDomains);
                changeVariableDomains(where,symbol);

                if(solveForwardChecking()){
                    return true;
                }else{
                    board[row][col] = -1;
                    variableDomains = copy;
                }
            }

        }

        return false;
    }

    private void changeVariableDomains(int[] var, int symbol){
        int[] domainToRemove = null;
        int[] back = {var[0], var[1] - 1};
        int[] up = {var[0] - 1, var[1]};

        boolean backOK = back[1] != -1;
        boolean upOK = up[0] != -1;

        int[] first = {var[0], var[1] + 2};
        int[] second = {var[0] + 2, var[1]};

        for(int[] key : variableDomains.keySet()){
            if(Arrays.equals(key,var)){
                domainToRemove = key;
            }else{
                if(Arrays.equals(key,first)){
                    if(backOK && board[back[0]][back[1]] == symbol){
                        List<String> domain = variableDomains.get(key);
                        domain.remove("" + symbol + "");
                        variableDomains.put(key,domain);
                    }
                }

                if(Arrays.equals(key,second)){
                    if(upOK && board[up[0]][up[1]] == symbol){
                        List<String> domain = variableDomains.get(key);
                        domain.remove("" + symbol + "");
                        variableDomains.put(key,domain);
                    }
                }
            }
        }

        variableDomains.remove(domainToRemove);
    }

    private List<String> getDomain(int[] var){
        for(int[] key : variableDomains.keySet()){
            if(Arrays.equals(key,var)){
                return variableDomains.get(key);
            }
        }

        return null;
    }

    private HashMap<int[],List<String>> copyVariableDomains(HashMap<int[],List<String>> variableDomains){
        HashMap<int[],List<String>> copy = new HashMap<>();
        for(int[] key : variableDomains.keySet()){
            int[] k = new int[2];
            k[0] = key[0];
            k[1] = key[1];
            copy.put(k,new ArrayList<String>(variableDomains.get(key)));
        }

        return copy;
    }

    private HashMap<int[], List<String>> prepareDomainsForVariables(){
        HashMap<int[],List<String>> variableDomains = new HashMap<>();
        List<String> variableDom = new ArrayList<String>();
        for(int d : domain){
            variableDom.add("" + d + "");
        }

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j] == -1){
                    int[] var = new int[2];
                    var[0] = i;
                    var[1] = j;
                    variableDomains.put(var, new ArrayList<String>(variableDom));
                }
            }
        }

        return variableDomains;
    }

    public boolean solveBacktrack(){
        int[] where = findFirstEmpty();
        int row = where[0];
        int col = where[1];

        if(row == -1)
            return true;

        for(int d : domain){
            if(areConstraintsSatisfied(row,col,d)){
                board[row][col] = d;

                if(solveBacktrack()){
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

    private int[] findFirstEmptyInRow(int row){
        int[] first = {row,-1};
        for(int col = 0; col < board.length; col++){
            if(board[row][col] == -1){
                first[1] = col;
                return first;
            }
        }
        return first;
    }

    private int getFilledRowIndex(boolean most){
        int[] row = getRow(0);
        int emptyFields = countSymbolOccurence(row, -1);
        int rowIndex = 0;

        for(int i = 1; i < board.length; i++){
            int emptyFieldsAmount = countSymbolOccurence(getRow(i),-1);
            if(most){
                if(emptyFields < emptyFieldsAmount){
                    emptyFields = emptyFieldsAmount;
                    rowIndex = i;
                }
            }else{
                if(emptyFields > emptyFieldsAmount){
                    emptyFields = emptyFieldsAmount;
                    rowIndex = i;
                }
            }
        }

        if(emptyFields == 0)
            return -1;

        return rowIndex;
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
        Binary b = new Binary(8,6);
        b.print();
        System.out.println();
        b.solveForwardChecking();
        b.print();
    }


}
