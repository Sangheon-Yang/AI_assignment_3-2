import java.util.*;
public class state {
    private int[] position_x; //position_x[x좌표] = y좌표; //퀸들의 배치상태를 나타냄
    private int score; //배치상태에 따른 score(fitness score)를 나타냄
    private int numOfQns; //퀸의 갯수를 나타냄

    //state 셍성
    public state(int num){
        this.position_x = new int[num];
        this.numOfQns = num;
    }

    //N개의 Queen들을 랜덤하게 배치후 배치위치에 따른 score(fitness score) 부여.
    //이 Method는 초기 0th Generation 에서 Population 크기 만큼의 parent state를 생성할때 사용된다.
    public void set_initial(){
        for(int i = 0; i<this.numOfQns ; i++){
            this.position_x[i] = (int)(Math.random()*this.numOfQns);
        }
        //배치위치에 따른 score(fitness score) 부여
        this.calculate_score();
    }

    //Parent state를 인자로 받아서 현재 state에 복사.
    //이 Method는 Reproduction 으로 Child를 생성할때 사용한다.
    public void copy_state(state parent){
        this.numOfQns = parent.numOfQns;
        this.score = parent.score;
        //퀸의 배치상태를 parent와 똑같이 복사.
        System.arraycopy(parent.position_x,0, this.position_x,0, this.numOfQns);
    }

    //현재 state에서 "index"번째의 column의 퀸을 아래로 한칸 옮기고
    //옮긴 배치상태의 fitness score를 계산하여 부여.
    //이 Method는 Reproduction 으로 Child를 생성할때 사용한다.
    public void change_position_down(int index){
        this.position_x[index]= (this.position_x[index]+1) % this.numOfQns;
        this.calculate_score();
    }

    //현재 퀸들의 배치상태에 따른 fitness score를 계산하여 "score"변수에 저장
    public void calculate_score(){
        int tmp_score = 0;//0점부터 시작후, 감점되는 형태; 모든 solution state는 fitness score가 0 이다.
        for(int i=0; i < this.numOfQns; i++) {
            for(int j=0; j < this.numOfQns; j++){
                if(i!=j && this.position_x[i]==this.position_x[j]){
                    //만약 서로 다른 두개의 퀸이 같은 가로선상에 놓여있는 경우
                    //10000점을 감점한다. 이 감점 점수는 퀸하나를 기준으로 N번 실행되기에 계속 누적된다.
                    tmp_score -=10000;
                }
                if (i!=j && (((i-this.position_x[i])==(j-this.position_x[j]))||((i+this.position_x[i])==(j+this.position_x[j])))){
                    //만약 서로 다른 두개의 퀸이 같은 대각선상에 놓여있는 경우
                    //10점을 감점한다. 이 감점 점수는 퀴하나를 기준으로 N번 실행되기에 계속 누적된다.
                    tmp_score -= 10;
                }
            }
        }
        //계산된 점수는 현재 state의 fitness score가 된다.
        this.score = tmp_score;
    }

    //현재 State의 fitness score를 반환.
    public int get_score(){
        return this.score;
    }

    //현재 state의 퀸들의 배치상태를 string으로 반환
    public String printState(){
        String result = "";
        for(int i = 0;i<this.numOfQns;i++){
            result = result + this.position_x[i] + " ";
        }
        return result;
    }
}
