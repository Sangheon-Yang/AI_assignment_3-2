import java.util.*;

public class state {
    private int[] position_x; //position_x[x좌표] = y좌표; //퀸들의 배치상태를 나타냄
    private int point; //배치상태에 따른 점수(hill-climbing 함수값(h(state)))를 나타냄
    private int numOfQns; //퀸의 갯수를 나타냄

    //state 셍성
    public state(int num){
        this.position_x = new int[num];
        this.numOfQns = num;
    }

    //N개의 Queen들을 랜덤하게 배치후 배치위치에 따른 point(h(state)값) 부여.
    public void set_initial(){
        for(int i = 0; i<this.numOfQns ; i++){
            this.position_x[i] = (int)(Math.random()*this.numOfQns);
        }
        //배치위치에 따른 point(h(state)값) 부여
        this.calculate_point();
    }

    //현재 state에서 "index"번째의 column의 퀸을 아래로 한칸 옮기고
    //옮긴 배치상태의 h(state) point를 계산하여 부여.
    public void change_position_down(int index){
        this.position_x[index]= (this.position_x[index]+1) % this.numOfQns;
        this.calculate_point();
    }

    //현재 퀸들의 배치상태에 따른 Point(h(state)값)를 계산하여 "point"변수에 저장
    public void calculate_point(){
        int tmp_point = 0;//0점부터 시작후, 감점되는 형태; 모든 solution state는 point가 0 이다.
        for(int i=0; i < this.numOfQns; i++) {
            for(int j=0; j < this.numOfQns; j++){
                if(i!=j && this.position_x[i]==this.position_x[j]){
                    //만약 서로 다른 두개의 퀸이 같은 가로선상에 놓여있는 경우
                    //10000점을 감점한다. 이 감점 점수는 퀸하나를 기준으로 N번 실행되기에 계속 누적된다.
                    tmp_point -=10000;
                }
                if (i!=j && (((i-this.position_x[i])==(j-this.position_x[j]))||((i+this.position_x[i])==(j+this.position_x[j])))){
                    //만약 서로 다른 두개의 퀸이 같은 대각선상에 놓여있는 경우
                    //10점을 감점한다. 이 감점 점수는 퀴하나를 기준으로 N번 실행되기에 계속 누적된다.
                    tmp_point -= 10;
                }
            }
        }
        //계산된 점수는 현재 state의 point(h(state)함수값)가 된다.
        this.point = tmp_point;
    }

    //현재 State의 Point를 반환.
    public int get_point(){
        return this.point;
    }

    //실제 hill-climbing이 일어나는 Objective function
    //현재의 배치상태의 Point와 현재상태에서 각 columm에서 퀸들을 한칸씩 밑으로 이동시킨 배치상태의 Point들을 비교하여
    //더 높은 Point를 가지는 배치상태로 변경하는 Method
    public boolean changed_to_higher_point(){
        int current_point = this.get_point(); // 현재 배치상태의 point
        int tmp_max_point = current_point; // 앞으로 N가지의 배치상태를 탐색하면서 가장 높은 point를 저장
        int tmp_index = -1; // N개의 배치상태를 탐색하면서 가장 높은 Point를 가지는 배치상태일때의 퀸이 이동하는 column의 index 저장
        boolean changed = false; // 배치상태가 변경이 되는지 여부를 체크하는 변수.
        //배치상태의 변경이 일어나면 true가 되고, 변경이 일어나지 않으면 false가 되는데,
        //changed == false인 경우, 이는 local maximum에 도달했단 뜻이다.

        state tmp_state = new state(this.numOfQns); // 변경된 배치상태를 나타내는 state

        //N가지의 state를 탐색하면서 가장 높은 Point를 가지는 state를 탐색하고, 그 state로 현재 state를 변경.
        for(int i = 0; i<this.numOfQns;i++){
            //현재 배치상태를 복사하여 tmp_state에 넣어주고, 각각의 column별로 퀸을 한칸씩 밑으로 내린 배치상태의 point를 계산하여 부여
            System.arraycopy(this.position_x,0, tmp_state.position_x,0, this.numOfQns);
            //i번째 column의 퀸을 한칸 내리고 그 배치상태의 Point를 계산.
            tmp_state.change_position_down(i);

            //만약 위에서 변경된 배치상태의 point가 나머지 배치상태보다 높은 경우
            //columm의 index와 Point를 따로 저장해주고,
            //changed변수를 true로하여 배치상태의 변경이 일어났음을 표시.
            if(tmp_max_point < tmp_state.get_point()) {
                tmp_max_point = tmp_state.get_point();
                tmp_index = i;
                changed = true;
            }
        }

        //실제 배치상태의 변화가 일어난 경우, 아까 저장해뒀던 Max Point 값을 갖는 배치상태로 현재 state를 변경.
        if(changed) {
            this.change_position_down(tmp_index);
        }

        return changed;
    }

    //hill-climb를 실행해주는 매소드.
    public String hill_climb(){
        String Str; //출력된 결과를 저장하는 변수

        //objective function에서 local maximum에 도달했지만, solution이 아닐때 랜덤하게 퀸들을 재배치하는 횟수를 나타내는 변수
        int total_restart = 0;

        //배치상태의 변화가 일어나고 있다는 것을 나타내는 변수
        //changing이 false인 경우 local maximum에 도달하여 더이상 배치상태가 변하지 않고 있다는 뜻.
        boolean changing = true;

        //현재의 배치상태를 initial state로 만듬
        //initial state는 각 column별로 퀸이 랜덤하게 배치되어있는 상태.
        this.set_initial();

        //total elapsed time을 계산할때 사용하는 변수
        //Ts는 시작시간, Te는 끝난시간을 의미.
        double Ts, Te;

        Ts = System.nanoTime();//시작시간 계산.

        //hill-climbing 실행.
        //Solution이 아닌 local maximum에 도달하여 더이상 상태변화가 없을때,
        //Random Restart 횟수를 N^N 으로 제한을 둠.
        //각 column별로 퀸을 하나씩 배치했을때 N개의 퀸을 배치할수 있는 경우의 수가 N^N 이기에 이처럼 설정함.
        while(changing && total_restart < Math.pow(this.numOfQns,this.numOfQns)){

            //현재의 배치상태보다 더 높음 Point를 가지는 배치상태로 계속해서 배치상태를 변화시킴.
            changing = this.changed_to_higher_point();

            //solution이 아닌 local maximum에 도달하여 배치상태 변화가 없을때,
            //퀸들을 다시 랜덤하게 배치하여 다시 탐색.
            //이때 "total_restart"변수에 랜덤배치된 횟수를 저장.
            if(!changing && this.get_point()!=0){
                this.set_initial();
                changing = true;
                total_restart++ ;
            }

            //Solution을 찾은 경우는 changing이 false가 되어 while문을 벗어나게 됨.
        }

        Te = System.nanoTime();//종료시간 계산.

        //Solution을 찾은 경우
        if(this.get_point() == 0){
            Str = ">Hill Climbing\n";
            for(int i =0 ; i < this.numOfQns; i++){
                Str = Str + this.position_x[i]+ " ";//퀸들의 위치 출력
            }
            //elapsed time과 Random Restart 횟수를 출력
            Str = Str + "\nTotal Elapsed Time: " + (Te-Ts)/1000000000 + "\n" + "( Total Random Restart : " + total_restart + " )\n" ;
        }

        //주어진 random restart 횟수를 다 쓰고도, Solution를 못찾은 경우
        else{
            Str = ">Hill Climbing\nCould Not Find the Solution\nTotal Elapsed Time: " + (Te-Ts)/1000000000 + "\n" + "( Total Random Restart : " + total_restart + " )\n";
        }

        return Str; //출력된 결과를 반환.
    }
}
