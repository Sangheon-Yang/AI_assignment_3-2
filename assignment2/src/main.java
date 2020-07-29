import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class main{
    public static void main(String[] args) {
        // write your code here


        //퀸의 갯수 N 입력처리;
        Integer inputN = Integer.parseInt(args[0]);
        int NofQns = inputN;


        //int NofQns = 10;

        //퀸의 갯수 예외처리;
        if(NofQns < 1){//숫자 입력 오류시. 1보다 작은 숫자를 입렵받는 경우
            System.out.println("please rewrite appropriate Number of queens");
            return;
        }

        //hill-climb 실행후 resultString에 결과 저장;
        state S = new state(NofQns);
        String resultString = S.hill_climb();

        //resultString 출력
        //System.out.println(resultString);

        //resultN.txt 파일의 경로 처리 및 생성
        String path = args[1];
        File resultFile = new File(path + "/result"+inputN.toString()+".txt");
        try {
            FileWriter writing = new FileWriter(resultFile);
            // hill_climb 결과 출력//////////////////////////////////////////////////////
            writing.write(resultString);
            //////////////////////////////////////////////////////////////
            writing.flush();
        }
        catch(Exception e){//path 입력 오류시
            System.out.println("Please rewrite appropriate pathname");
        }

    }
}
