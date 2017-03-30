import java.util.ArrayList;
import java.util.List;

/**
 * Created by GreggJakubiak on 30.03.2017.
 */
public class Graph {
    private int colours;
    private int[][] graph;
    private List<Tuple> colourPairs;

    public Graph(int n){
        graph = new int[n][n];
        colours = n % 2 == 0 ? 2 * n : 2 * n + 1;
        colourPairs = new ArrayList<>();
    }

    public void colourGraph(){
        List<Integer> coloursList = new ArrayList<>();
        colourGraph(0,0,coloursList);
    }

    private boolean colourGraph(int row, int col, List<Integer> colourList){
        if(isColoured())
            return true;

        for(int c = 1; c <= colours; c++){
            if(canBeColoured(row,col,c)){
                graph[row][col] = c;
                colourList.add(c);

                if(colourList.size() == 2){
                    colourPairs.add(new Tuple(colourList.get(0),colourList.get(1)));
                    colourList.clear();
                }

                if(col + 1 < graph.length){
                    if(colourGraph(row,col+1,colourList))
                        return true;
                }else if(row + 1 < graph.length){
                    if(colourGraph(row+1,0,colourList))
                        return true;
                }

                graph[row][col] = 0;
                if(!colourPairs.isEmpty())
                    colourPairs.remove(colourPairs.size()-1);
            }
        }

        return false;
    }

    private boolean isColoured(){
        for(int i = 0; i < graph.length; i++){
            for(int j = 0; j < graph.length; j++){
                if(graph[i][j] == 0)
                    return false;
            }
        }

        return true;
    }

    private boolean canBeColoured(int row, int col, int colour){
        if(row == 0){
            if(col == 0){
                return true;
            }else{
                int c = graph[row][col-1];
                return c != colour && !colourPairs.contains(new Tuple(colour,c));
            }
        }else{
            if(col == 0){
                int c = graph[row - 1][col];
                return c != colour && !colourPairs.contains(new Tuple(colour,c));
            }else{
                int c1 = graph[row][col - 1];
                int c2 = graph[row - 1][col];

                return (c1 != colour && c2 != colour) && (!colourPairs.contains(new Tuple(colour,c1)) && !colourPairs.contains(new Tuple(colour,c2)));
            }
        }
    }

    public void print(){
        for(int[] g : graph){
            for(int c : g){
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        Graph g = new Graph(3);
        g.print();
        g.colourGraph();
        System.out.println();
        g.print();
    }
}
