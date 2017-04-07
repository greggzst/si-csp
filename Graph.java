import java.util.*;

/**
 * Created by GreggJakubiak on 30.03.2017.
 */
public class Graph {
    private int[][] graph;
    private List<Tuple> colourPairs;
    private int colours;
    private HashMap<int[],List<String>> variableDomains;

    public Graph(int n){
        graph = new int[n][n];
        colourPairs = new ArrayList<>();
        colours = n % 2 == 0 ? 2 * n : 2 * n + 1;
        variableDomains = prepareDomainsForVariables();
    }

    public boolean colourGraphForwardChecking(){

        int[] first = findFirstUnColoured();
        int row = first[0];
        int col = first[1];

        if(row == -1)
            return true;

        List<String> colours = getDomain(first);
        for(String c : colours){
            int colour = Integer.parseInt(c);
            if(canBeColoured(row,col,colour)){
                graph[row][col] = colour;
                HashMap<int[],List<String>> copy = copyVariableDomains(variableDomains);
                changeVariableDomains(first, colour);

                if(colourGraphForwardChecking()){
                    return true;
                }
                else{
                    graph[row][col] = 0;
                    removePairs(row,col);
                    variableDomains = copy;
                }

            }
        }

        return false;

    }

    private void changeVariableDomains(int[] var, int colour){
        int[] firstNeighbour = {var[0], var[1] + 1};
        int[] secondNeighbour = {var[0] + 1, var[1]};
        int[] domainToDelete = null;

        for(int[] key : variableDomains.keySet()){
            if(Arrays.equals(key,var)){
                domainToDelete = key;
            }else{
                if(Arrays.equals(key,firstNeighbour)){
                    List<String> colours = variableDomains.get(key);
                    List<String> coloursInPair = getColoursInPair(colour);
                    colours.remove("" + colour + "");
                    colours.removeAll(coloursInPair);
                    variableDomains.put(key,colours);
                }

                if(Arrays.equals(key,secondNeighbour)){
                    List<String> colours = variableDomains.get(key);
                    List<String> coloursInPair = getColoursInPair(colour);
                    colours.remove("" + colour + "");
                    colours.removeAll(coloursInPair);
                    variableDomains.put(key,colours);
                }
            }
        }

        variableDomains.remove(domainToDelete);
    }

    private List<String> getColoursInPair(int colour){
        List<String> coloursInPair = new ArrayList<>();
        for(Tuple t : colourPairs){
            if(t.y == colour){
                coloursInPair.add("" + t.x + "");
            }
        }

        return coloursInPair;
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

    private List<String> getDomain(int[] var){
        for(int[] key : variableDomains.keySet()){
            if(Arrays.equals(key,var)){
                return variableDomains.get(key);
            }
        }

        return null;
    }

    private HashMap<int[], List<String>> prepareDomainsForVariables(){
        HashMap<int[],List<String>> variableDomains = new HashMap<>();
        List<String> domain = new ArrayList<String>();
        for(int c = 1; c <= colours; c++){
            domain.add("" + c + "");
        }

        for(int i = 0; i < graph.length; i++){
            for(int j = 0; j < graph.length; j++){
                int[] var = new int[2];
                var[0] = i;
                var[1] = j;
                variableDomains.put(var, new ArrayList<String>(domain));
            }
        }

        return variableDomains;
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

        for(int c = 1; c <= colours; c++){
            if(canBeColoured(row,col,c)){
                graph[row][col] = c;
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

    public boolean colourGraphValueSelect(boolean most){
        int[] first = findFirstUnColoured();
        int row = first[0];
        int col = first[1];

        if(row == -1)
            return true;

        if(colourPairs.size() > 2){
            int c = getColour(most);

            if(canBeColoured(row,col,c)){
                graph[row][col] = c;
                if(colourGraphValueSelect(most)){
                    return true;
                }
                else{
                    removePairs(row,col);
                    graph[row][col] = 0;
                }
            }
        }

        for(int c = 1; c <= colours; c++){
            if(canBeColoured(row,col,c)){
                graph[row][col] = c;
                if(colourGraphValueSelect(most)){
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

    private int getColour(boolean most){
        HashMap<Integer,Integer> colourAmountMap = new HashMap<>();
        for (Tuple t : colourPairs){

            if(!colourAmountMap.containsKey(t.x)){
                colourAmountMap.put(t.x,1);
            }else{
                int newVal = colourAmountMap.get(t.x) + 1;
                colourAmountMap.put(t.x,newVal);
            }

            if(!colourAmountMap.containsKey(t.y)){
                colourAmountMap.put(t.y,1);
            }else{
                int newVal = colourAmountMap.get(t.y) + 1;
                colourAmountMap.put(t.y,newVal);
            }
        }

        Map.Entry<Integer,Integer> lookingFor = null;

        for(Map.Entry<Integer,Integer> entry : colourAmountMap.entrySet()){
            if(most){
                if(lookingFor == null || entry.getValue() > lookingFor.getValue()){
                    lookingFor = entry;
                }
            }else{
                if(lookingFor == null || entry.getValue() < lookingFor.getValue()){
                    lookingFor = entry;
                }
            }
        }

        return lookingFor.getKey();
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
        Graph g = new Graph(8);
        g.print();
        g.colourGraphValueSelect(false);
        System.out.println();
        g.print();
    }
}
