import java.io.File;
import java.io.FileWriter;
import java.util.*;
public class main{
    public static void main(String[] args) {
        //퀸의 갯수 N 입력처리;
        Integer inputN = Integer.parseInt(args[0]);
        int NofQns = inputN;
        //int NofQns = 20;

        //퀸의 갯수 예외처리;
        if(NofQns < 1){//숫자 입력 오류시. 1보다 작은 숫자를 입렵받는 경우
            System.out.println("please rewrite appropriate Number of queens");
            return;
        }

        //solution을 찾았는지 확인해주는 변수, true일때 solution을 찾은것이다.
        boolean got_solution = false;

        String result;//출력할 결과 값을 저장할 string 변수

        int Population = 2*NofQns;//한 generation의 population크기를 정해준다.

        //generation 생성
        generation G = new generation(NofQns,Population);

        double Ts, Te;//Elapsed time 계산용 변수
        Ts = System.nanoTime();//시작시간 저장.
        G.initialize_ZERO_generation();//0-generation으로 초기화(랜덤배치).

        //실제 GENETIC ALGORITHM이 실행되는 부분
        //Solution을 찾거나, 혹은 N^N 번째 generation에 도달했을 경우 반복문을 벗어난다.
        while(G.get_nthGeneration() < Math.pow(NofQns,NofQns)) {
            if(G.if_Solution_exist() != -1) {//항상 새로운 Parent가 정해지면 Solution 검사를 실시한다.
                got_solution = true;
                break;
            }
            else {//parent 중 solution이 없는 경우
                G.reproduct_children();//Child들을 생성한다.
                G.select_next_parents();//child들의 fitness score를 비교하여 다음 세대의 parent를 선택한다.
            }
        }
        Te = System.nanoTime();//끝나는 시간 저장.
        if(got_solution){//solution으르 찾은 경우
            result = G.printResult();//solution을 출력한다.
        }
        else{//solution을 찾지 못한경우
            result = ">Genetic Algorithm (Population : " + Population + ")\nNo Solution until (N^N)th Generation\n";
        }
        //시간 과 nth generation 출력
        result += "Total Elapsed Time : "+(Te-Ts)/1000000000+"\n"+"Until Nth Generation : "+G.get_nthGeneration();

        //System.out.println(result);


        //resultN.txt 파일의 경로 처리 및 생성
        String path = args[1];
        File resultFile = new File(path + "/result"+inputN.toString()+".txt");
        try {
            FileWriter writing = new FileWriter(resultFile);
            // Genetic Algorithm 결과 출력//////////////////////////////////
            writing.write(result);
            //////////////////////////////////////////////////////////////
            writing.flush();
        }
        catch(Exception e){//path 입력 오류시
            System.out.println("Please rewrite appropriate pathname");
        }
    }
}