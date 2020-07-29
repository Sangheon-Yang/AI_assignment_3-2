import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class main{
    public static void main(String[] args) {
        // write your code here
        Integer inputN = Integer.parseInt(args[0]);
        int limit = inputN;

        if(limit < 1){//숫자 입력 오류시. 1보다 작은 숫자를 입렵받는 경우
            System.out.println("please rewrite appropriate Number of queens");
            return;
        }

        String path = args[1];

        Stack<node> dfsFringe = new Stack<>();
        Stack<node> dfidFringe = new Stack<>();
        Queue<node> bfsFringe = new LinkedList<>();


        File resultFile = new File(path + "/result"+inputN.toString()+".txt");
        try {
            FileWriter writing = new FileWriter(resultFile);

            // DFS 실행//////////////////////////////////////////////////////
            writing.write(">DFS\n");
            double sT = System.nanoTime();
            node dfsRoot = new node(inputN);
            dfsRoot.set_root();
            String result1 = dfsRoot.dfs_n_queens(dfsFringe,limit);
            double eT = System.nanoTime();
            if(result1 == "No solution\n"){
                writing.write(result1 + "Time : 0.0 \n\n");
            }
            else {
                writing.write(result1 + "Time : " + ((eT-sT)/1000000000) + "\n\n");
            }

            //BFS 실행////////////////////////////////////////////////////////
            writing.write(">BFS\n");
            double sT2 = System.nanoTime();
            node bfsRoot = new node(inputN);
            bfsRoot.set_root();
            String result2 = bfsRoot.bfs_n_queens(bfsFringe,limit);
            double eT2 = System.nanoTime();
            if(result2 == "No solution\n"){
                writing.write(result2 + "Time : 0.0 \n\n");
            }
            else {
                writing.write(result2 + "Time : " + ((eT2 - sT2)/1000000000) + "\n\n");
            }

            //DFID 실행/////////////////////////////////////////////////////
            writing.write(">DFID\n");
            double sT3 = System.nanoTime();
            node dfidRoot = new node(inputN);
            dfidRoot.set_root();
            String result3 = dfidRoot.dfid_n_queens(dfidFringe,limit);
            double eT3 = System.nanoTime();
            if(result3 == "No solution\n"){
                writing.write(result3 + "Time : 0.0\n\n");
            }
            else {
                writing.write(result3 + "Time : " + ((eT3 - sT3)/1000000000) + "\n\n");
            }
            //////////////////////////////////////////////////////////////

            writing.flush();
        }
        catch(Exception e){//path 입력 오류시
            System.out.println("Please rewrite appropriate pathname");
        }
/*
        double starttime = System.nanoTime();
        System.out.println(">DFS : N = " + limit);
        node example = new node(limit);
        example.set_root();
        example.dfs_n_queens(dfsFringe, limit);
        double endtime = System.nanoTime();
        System.out.println("time : " + (endtime-starttime)/1000000000 + " sec");

        double starttime2 = System.nanoTime();
        System.out.println(">BFS : N = " + limit);
        node example2 = new node(limit);
        example2.set_root();
        example2.bfs_n_queens(bfsFringe, limit);
        double endtime2 = System.nanoTime();
        System.out.println("Time : " + (endtime2-starttime2)/1000000000 + " sec");

        double starttime3 = System.nanoTime();
        System.out.println(">DFID : N = " + limit);
        node example3 = new node(limit);
        example3.set_root();
        example3.dfid_n_queens(dfidFringe, limit);
        double endtime3 = System.nanoTime();
        System.out.println("Time : " + (endtime3-starttime3)/1000000000 + " sec");
*/
    }
}