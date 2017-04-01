import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by GreggJakubiak on 30.03.2017.
 */
public class Graph {
    private int colours;
    private int[][] graph;
    private List<Tuple> colourPairs;
    private HashMap<int[],List<String>> variableValuesList;

    public Graph(int n){
        graph = new int[n][n];
        colours = n % 2 == 0 ? 2 * n : 2 * n + 1;
        colourPairs = new ArrayList<>();
        variableValuesList = new HashMap<>();

        List<String> coloursList = new ArrayList<>();
        for(int c = 0; c <= colours; c++){
            coloursList.add("" + c + "");
        }

        for(int i = 0; i < graph.length; i++){
            for(int j = 0; j < graph.length; j++){
                int[] elem = new int[2];
                elem[0] = i;
                elem[1] = j;

                variableValuesList.put(elem,coloursList);
            }
        }


    }

    public boolean colourGraphForwardChecking(){
        int[] first = findFirstUnColoured();
        int row = first[0];
        int col = first[1];

        if(row == -1)
            return true;

        List<String> colours = getColourList(first);
        for(String c : colours){
            if(canBeColoured(row,col,Integer.parseInt(c))){
                graph[row][col] = Integer.parseInt(c);
                HashMap<int[],List<String>> variableValuesCopy = new HashMap<>(variableValuesList);
                changeColourPossibilities(first,c);

                if(colourGraphForwardChecking()){
                    return true;
                }else{
                    graph[row][col] = 0;
                    removePairs(row,col);
                    variableValuesList = variableValuesCopy;
                }
            }
        }
        return false;
    }

    private List<String> getColourList(int[] key){
        for(int[] k : variableValuesList.keySet()){
            if(Arrays.equals(k,key)){
                return variableValuesList.get(k);
            }
        }

        return null;
    }

    private void changeColourPossibilities(int[] key, String colour){
        for(int[] k : variableValuesList.keySet()){
            if(Arrays.equals(k,key)){
                List<String> selectedColour = new ArrayList<>();
                selectedColour.add(colour);
                variableValuesList.put(key,selectedColour);
            }else{
                List<String> colourList = variableValuesList.get(k);
                colourList.remove(colour);
                variableValuesList.put(k,colourList);
            }
        }
    }

    public boolean colourGraphBacktrack(){
        int[] first = findFirstUnColoured();
        int row = first[0];
        int col = first[1];

        if(row == -1)
            return true;

        for(int c = 1; c <= colours; c++){
            if(canBeColoured(row,col,c)){
                graph[row][col] = c;
                if(colourGraphBacktrack()){
                    return true;
                }
                else{
                    graph[row][col] = 0;
                    removePairs(row,col);
                }

            }
        }

        return false;
    }

    private void removePairs(int row, int col){
        if(row == 0 && col !=0 || row != 0 && col == 0){
            colourPairs.remove(colourPairs.size() - 1);
        }else if(row != 0 && col != 0){
            colourPairs.remove(colourPairs.size() - 1);
            colourPairs.remove(colourPairs.size() - 1);
        }
    }

    private int[] findFirstUnColoured(){
        int[] first = {-1, -1};

        for(int i = 0; i < graph.length; i++){
            for(int j = 0; j < graph.length; j++){
                if(graph[i][j] == 0){
                   first[0] = i;
                   first[1] = j;
                   return first;
                }

            }
        }

        return first;
    }

    private boolean canBeColoured(int row, int col, int colour){
        return areNeighbourColoursSame(row,col,colour) && isOnlyOneColourPairConnection(row,col,colour);
    }

    private boolean areNeighbourColoursSame(int row, int col, int colour){

        boolean condition = false;

        if(row == 0 && col ==0){
            condition = true;
        }else if(row == 0 && col != 0){
            condition = graph[row][col - 1] != colour;
        }else if(row != 0 && col == 0){
            condition = graph[row - 1][col] != colour;
        }else if(row != 0 && col != 0){
            condition = graph[row - 1][col] != colour && graph[row][col - 1] != colour;
        }

        return condition;
    }

    private boolean isOnlyOneColourPairConnection(int row, int col, int colour){

        Tuple t1 = null;
        Tuple t2 = null;


        if(row == 0 && col ==0){
            return true;
        }
        else if (row != 0 && col == 0){
            t1 = new Tuple(graph[row-1][col],colour);

        }else if (row == 0 && col != 0){
            t2 = new Tuple(graph[row][col - 1], colour);
        }
        else if (row != 0 && col != 0){
            t1 = new Tuple(graph[row - 1][col], colour);
            t2 = new Tuple(graph[row][col - 1], colour);

        }

        boolean hasFirstTuple = false;
        boolean hasSecondTuple = false;

        if(t1 != null) {
            hasFirstTuple = colourPairs.contains(t1);
        }

        if(t2 != null) {
            hasSecondTuple = colourPairs.contains(t2);
        }

        if(hasFirstTuple || hasSecondTuple){
            return false;
        }

        if(t1 != null && !hasFirstTuple)
            colourPairs.add(t1);

        if(t2 != null && !hasSecondTuple)
            colourPairs.add(t2);

        return true;

    }

    public void print(){
        for(int[] g : graph){
            for(int c : g){
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        Graph g = new Graph(4);
        g.print();
        g.colourGraphForwardChecking();
        System.out.println();
        g.print();
    }
}
