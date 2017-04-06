import java.util.*;

/**
 * Created by GreggJakubiak on 30.03.2017.
 */
public class Graph {
    private List<String> coloursList;
    private int[][] graph;
    private List<Tuple> colourPairs;

    public Graph(int n){
        graph = new int[n][n];
        colourPairs = new ArrayList<>();

        int colours = n % 2 == 0 ? 2 * n : 2 * n + 1;

        coloursList = new ArrayList<>();
        for(int c = 1; c <= colours; c++){
            coloursList.add("" + c + "");
        }



    }


    private boolean colourGraphForwardChecking(){
        int[] first = findFirstUnColoured();
        int row = first[0];
        int col = first[1];

        if(row == -1)
            return true;

        Iterator<String> colourIterator = coloursList.iterator();
        while(colourIterator.hasNext()){
            String c = colourIterator.next();
            if(canBeColoured(row,col,Integer.parseInt(c))){
                graph[row][col] = Integer.parseInt(c);
                if(colourGraphForwardChecking()){
                    return true;
                }
                else{
                    graph[row][col] = 0;
                    removePairs(row,col);
                }

            }
            colourIterator.remove();
        }

        return false;

    }

    public boolean colourGraphBacktrackVariableSelect(){
        List<Triple> variables = variablesSortedByNeighbours();
        return colourGraphBacktrackVariableSelect(variables);
    }

    private boolean colourGraphBacktrackVariableSelect(List<Triple> variables){

        if(variables.size() == 0)
            return true;

        int row = variables.get(0).x;
        int col = variables.get(0).y;

        for(String c : coloursList){
            if(canBeColoured(row,col,Integer.parseInt(c))){
                graph[row][col] = Integer.parseInt(c);
                Triple t = variables.remove(0);
                if(colourGraphBacktrackVariableSelect(variables)){
                    return true;
                }else{
                    graph[row][col] = 0;
                    removePairs(row,col);
                    variables.add(0,t);
                }
            }
        }

        return false;
    }

    public boolean colourGraphBacktrack(){
        int[] first = findFirstUnColoured();
        int row = first[0];
        int col = first[1];

        if(row == -1)
            return true;

        for(String c : coloursList){
            if(canBeColoured(row,col,Integer.parseInt(c))){
                graph[row][col] = Integer.parseInt(c);
                if(colourGraphBacktrack()){
                    return true;
                }
                else{
                    removePairs(row,col);
                    graph[row][col] = 0;
                }

            }
        }

        return false;
    }

    private void removePairs(int row, int col){
        if(row == 0 && col == 0){
            if(graph[row][col + 1] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(graph[row + 1][col] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }
        }
        else if (row != 0 && col == 0){

            if(row + 1 < graph.length && graph[row + 1][col] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(graph[row][col+1] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(graph[row-1][col] != 0)
                colourPairs.remove(colourPairs.size() - 1);

        }else if (row == 0 && col != 0){
            if(col + 1 < graph.length && graph[row][col+1] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(graph[row+1][col] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(graph[row][col-1] != 0)
                colourPairs.remove(colourPairs.size() - 1);

        }
        else if (row != 0 && col != 0){

            if(col + 1 < graph.length && graph[row][col+1] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(row + 1 < graph.length && graph[row+1][col] != 0){
                colourPairs.remove(colourPairs.size() - 1);
            }

            if(graph[row][col - 1] != 0)
                colourPairs.remove(colourPairs.size() - 1);

            if(graph[row - 1][col] != 0)
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

        if(row == 0 && col == 0){
            condition = graph[row][col + 1] != colour && graph[row + 1][col] != colour;
        }else if(row == 0 && col != 0){
            if(col + 1 < graph.length){
                condition = graph[row][col - 1] != colour && graph[row][col + 1] != colour && graph[row + 1][col] != colour;
            }else{
                condition = graph[row][col - 1] != colour && graph[row + 1][col] != colour;
            }

        }else if(row != 0 && col == 0){
            if(row + 1 < graph.length){
                condition = graph[row - 1][col] != colour && graph[row][col + 1] != colour && graph[row + 1][col] != colour;
            }else{
                condition = graph[row - 1][col] != colour && graph[row][col + 1] != colour;
            }
        }else if(row != 0 && col != 0){
            if(row + 1 < graph.length && col + 1 < graph.length){
                condition = graph[row - 1][col] != colour && graph[row][col - 1] != colour
                        && graph[row + 1][col] != colour && graph[row][col + 1] != colour;
            }else if(row + 1 < graph.length){
                condition = graph[row - 1][col] != colour && graph[row][col - 1] != colour
                        && graph[row + 1][col] != colour;
            }else if(col + 1 < graph.length){
                condition = graph[row - 1][col] != colour && graph[row][col - 1] != colour
                        && graph[row][col + 1] != colour;
            }else{
                condition = graph[row - 1][col] != colour && graph[row][col - 1] != colour;
            }
        }

        return condition;
    }


    private boolean isOnlyOneColourPairConnection(int row, int col, int colour){

        Tuple t1 = null;
        Tuple t2 = null;
        Tuple t3 = null;
        Tuple t4 = null;


        if(row == 0 && col ==0){
            //if next neighbours are empty return true
            if(graph[row + 1][col] != 0 && graph[row][col + 1] != 0){
                return true;
            }else{
                if(graph[row + 1][col] != 0){
                    t1 = new Tuple(graph[row+1][col],colour);
                }

                if(graph[row][col + 1] != 0){
                    t2 = new Tuple(graph[row][col+1],colour);
                }
            }
        }
        else if (row != 0 && col == 0){
            if(graph[row-1][col] != 0)
                t1 = new Tuple(graph[row-1][col],colour);

            if(graph[row][col+1] != 0){
                t2 = new Tuple(graph[row][col+1],colour);
            }

            if(row + 1 < graph.length && graph[row + 1][col] != 0){
                t3 = new Tuple(graph[row+1][col],colour);
            }

        }else if (row == 0 && col != 0){
            if(graph[row][col-1] != 0)
                t1 = new Tuple(graph[row][col - 1], colour);

            if(graph[row+1][col] != 0){
                t2 = new Tuple(graph[row+1][col], colour);
            }

            if(col + 1 < graph.length && graph[row][col+1] != 0){
                t3 = new Tuple(graph[row][col+1],colour);
            }
        }
        else if (row != 0 && col != 0){
            if(graph[row - 1][col] != 0)
                t1 = new Tuple(graph[row - 1][col], colour);

            if(graph[row][col - 1] != 0)
                t2 = new Tuple(graph[row][col - 1], colour);

            if(row + 1 < graph.length && graph[row+1][col] != 0){
                t3 = new Tuple(graph[row+1][col], colour);
            }

            if(col + 1 < graph.length && graph[row][col+1] != 0){
                t4 = new Tuple(graph[row][col+1],colour);
            }

        }

        boolean hasFirstTuple = false;
        boolean hasSecondTuple = false;
        boolean hasThirdTuple = false;
        boolean hasFourthTuple = false;

        if(t1 != null) {
            hasFirstTuple = colourPairs.contains(t1);
        }

        if(t2 != null) {
            hasSecondTuple = colourPairs.contains(t2);
        }

        if(t3 != null){
            hasThirdTuple = colourPairs.contains(t3);
        }

        if(t4 != null){
            hasFourthTuple = colourPairs.contains(t4);
        }

        if(hasFirstTuple || hasSecondTuple || hasThirdTuple || hasFourthTuple){
            return false;
        }

        if(t1 != null && !hasFirstTuple)
            colourPairs.add(t1);

        if(t2 != null && !hasSecondTuple)
            colourPairs.add(t2);

        if(t3 != null && !hasThirdTuple)
            colourPairs.add(t3);

        if(t4 != null && !hasFourthTuple)
            colourPairs.add(t4);

        return true;

    }

    private int countNeighbours(int row, int col){
        int neighbours = 0;
        int n = graph.length - 1;
        if((row == 0 && col == 0) || (row == n && col ==0) || (row == 0 && col == n) ||(row == n && col == n)){
            neighbours = 2;
        }else if((row == 0 && col != 0) || (row == n && col != 0) || (row != 0 && col == 0) || (row != 0 && col == n)){
            neighbours = 3;
        }else{
            neighbours = 4;
        }

        return neighbours;
    }

    private List<Triple> variablesSortedByNeighbours(){
        List<Triple> vars = new ArrayList<>();
        for(int row = 0; row < graph.length; row++){
            for(int col = 0; col < graph.length; col++){
                vars.add(new Triple(row,col,countNeighbours(row,col)));
            }
        }
        vars.sort(new TripleComparator());
        return vars;
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
        Graph g = new Graph(6);
        g.print();
        g.colourGraphBacktrackVariableSelect();
        System.out.println();
        g.print();
    }
}
