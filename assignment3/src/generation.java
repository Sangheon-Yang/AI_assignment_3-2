import java.util.*;

public class generation {
    private int nOfQueens; //퀸들의 갯수 N을 저장하는 변수.
    private int nOfparent; //한 세대의 POPULATION; 한세대에서 parent 로 select 되는 state의 수.
    private int nthGeneration; // 몇번째 세대인지 나타내는 변수. 결과출력시 사용.
    private state[] parent; //현재 generation에서 population개의 parent들의 state를 나타냄.
    private state[][] child; //현재 generation 에서 reproduct 된 populatoin*N개의 child들의 state를 나타냄.

    //Generation 생성.
    //generation을 생성할 때 Queen의 갯수 NQ와 한세대의 population 숫자 NP를 인자로 받는다.
    public generation(int NQ, int NP){
        this.nOfQueens = NQ;
        this.nOfparent = NP;
        this.parent = new state[this.nOfparent];
        this.child = new state[this.nOfparent][this.nOfQueens];
    }

    //현재 Generation이 몇번째 generation인지 반환
    public int get_nthGeneration(){
        return this.nthGeneration;
    }

    //0번째 Generation을 초기 설정해주는 메소드.
    //이 메소드는 Main 함수 내에서 한번만 실행한다.
    //hill-climbing 때와 비슷하게 각각의 Parent들의 state(퀸의 배치상태)들을 랜덤하게 생성.
    public void initialize_ZERO_generation(){
        for(int i = 0; i < this.nOfparent; i++){
            this.parent[i] = new state(this.nOfQueens);
            this.parent[i].set_initial();//parent의 state를 랜덤하게 배치해준다.
        }
        this.nthGeneration = 0;
    }

    //현재 generation에서 population개의 parent로 select 된 state들 중 solution state가 존재하는지 확인하고
    //만약 solution state가 존재한다면 그 state의 위치를 parent[]배열의 인덱스로 반환한다.
    //solution state가 존재하지 않을 경우 -1을 반환한다.
    public int if_Solution_exist(){
        for(int i = 0; i < this.nOfparent; i++){
            if(this.parent[i].get_score() == 0) {//fitness score가 0일때만 solution state이다.
                return i;
            }
        }
        return -1;
    }

    //children을 생성하는 method.
    //population개의 Parent state를 가지고 population*N 개의 Child state를 생성한다.
    //각각의 Parent는 현재 자신의 퀸 배치 상태에서 각 column별로 퀸을 한칸씩만 내린 배치를 가지는 N개의 child를 생성한다.
    //위의 child 생성방식 (Mutation) 은 hill climbing 을 구현했을때 사용했던
    // change_position_down() 메소드를 사용하여 구현하였다.
    public void reproduct_children(){
        for(int i = 0; i < this.nOfparent; i++){
            for(int j = 0; j < this.nOfQueens; j++){
                //가장먼저 child state를 생성해준다.
                this.child[i][j] = new state(this.nOfQueens);
                //그후 parent의 state를 child에게 그대로 복사해준다.
                this.child[i][j].copy_state(this.parent[i]);
                //이부분이 Mutation이 일어나는 부분이다.
                //각각의 child state들은 parent의 state에서
                //각각의 column별로 퀸을 한칸씩 내려 배치한 상태를 갖는다.
                this.child[i][j].change_position_down(j);
            }
        }
    }

    // 다음 generation의 parent를 select해주는 메소드. 전체 코드에서 가장 중요한 부분이다.
    // reproduction으로 생성된 population*N개의 children state의 fitness 를 비교하여
    // population*N개중 다음 generation의 Parent가 될 population개의 state를 선택하는 메소드이다.
    // fitness 는 hill-climbing에서 사용했던 H(state)를 구하는 방식과 같은 방식으로 점수를 계산하여
    // fitness score를 각각의 child마다 부여하고, 이를 비교하여 가장 fitness score가 높은 population개의 child가 선택되어
    // 다음 Generation의 Parent가 되고, Generation이 1 증가한다.
    public void select_next_parents(){
        //이 배열들은 population*N개의 child 중에서 선택된 population개의 child[i][j]의 위치정보와 fitness score를 저장하는 변수이다.
        int[] index_i = new int[this.nOfparent];//선택된 child[][]의 row index 정보를 저장한다.
        int[] index_j = new int[this.nOfparent];//선택된 child[][]의 column index 정보를 저장한다.
        int[] tmp_score = new int[this.nOfparent];//선택된 child의 fitness score 정보를 저장한다.
        //이 변수들은 각각 child들의 fitness를 비교하는 과정에서 사용되는 변수이다.
        int nOfselectedStates = 0;//현재 선택된 child들의 갯수를 의미한다. child를 탐색할때 초기에 사용되는 변수이다.
        int tmp_min = 0;//이 변수는 현재 선택된 child들 중에서 가장 낮은 fitness를 저장하는 변수이다.
        int min_index = 0;//이 변수는 현재 선택된 child들 중에서 가장 낮은 fitness를 가지는
                            //child가 위에 선언된 3개의 배열에서 어느 index에 저장되었는지 나타내주는 변수이다.

        //총 population*N번 for문을 돌면서 각각의 child의 fitness score를 비교하여 Select된 child의 정보를 위의 변수에 저장
        for(int i = 0; i < this.nOfparent; i++){
            for(int j = 0; j < this.nOfQueens; j++){
                if(nOfselectedStates<this.nOfparent){//아직 Parent의 숫자 population보다 child들이 덜 선택된 경우
                    //일단 위의 변수에 현재 child의 정보를 저장한다.
                    index_i[nOfselectedStates]=i;//child의 row index 저장.
                    index_j[nOfselectedStates]=j;//child의 column index 저장.
                    tmp_score[nOfselectedStates]=this.child[i][j].get_score();//child의 fitness score 저장.
                    //이후 현재 Select 된 child 중에서 가장 낮은 fitness score를 갖는 child 정보를 따로 저장한다.
                    if(tmp_score[nOfselectedStates] < tmp_min){//만약 현재 저장되는 child의 fitness가 가장 낮은경우
                        min_index = nOfselectedStates;//현재 child 정보가 저장된 위치의 index를 저장.
                        tmp_min = tmp_score[min_index];//현재 child의 fitness score를 저장.
                    }
                    nOfselectedStates++;//Select된 child의 갯수 1증가.
                }
                //선택된 child의 갯수가 Population의 숫자와 같을때 fitness score가 더 높은 child를 발견하여
                //원래 선택되어있던 child 중에서 가장 fitness score가 낮은 child 를 버리고 새로 발견한 child를 저장한다.
                else if(this.child[i][j].get_score() > tmp_min){//fitness score가 더 높은 child를 발견했을때
                    //가장 작은 fitness score 을 가진 child state를 제거하고 그 자리에 새로 발견한 child의 정보를 저장
                    index_i[min_index] = i;//row index 정보 저장
                    index_j[min_index] = j;//column index 정보 저장
                    tmp_score[min_index] = this.child[i][j].get_score();//fitness score저장
                    //선택된 child들이 변경되면 이전의 사용했던 tmp_min값과 min_index를 재설정한다.
                    tmp_min = 0;
                    for(int k=0;k<this.nOfparent;k++){
                        //tmp_score 배열을 탐색하여 가장 낮은 fitness score를 tmp_min에 저장하고,
                        //가장 낮은 fitness score를 가지는 child 가 저장된 위치의 인덱스를 새롭게 저장.
                        if(tmp_score[k] < tmp_min){
                            tmp_min = tmp_score[k];
                            min_index = k;
                        }
                    }
                }
            }
        }
        //population*N개의 child들을 모두 탐색하여 population개의 다음 generation의 Parent를 선택한후
        //현재 generation의 parent를 새로 선택(SELECTION)한 population개의 child로 바꾸어준다.
        for(int i=0;i<this.nOfparent;i++) {
            //Select 된 child들이 parent가 된다.
            this.parent[i] = this.child[index_i[i]][index_j[i]];
        }
        //generation의 숫자를 1 증가 한다.
        this.nthGeneration++;
    }

    //현재 generation에 solution이 있는 경우 solution 결과를 출력한다.
    public String printResult(){
        int index = this.if_Solution_exist();
        String result = ">Genetic Algorithm(Population : " + this.nOfparent + ")\n";
        if(index != -1) {
            result = result + this.parent[index].printState() + "\n";//solution state 출력.
        }
        return result;
    }
}
