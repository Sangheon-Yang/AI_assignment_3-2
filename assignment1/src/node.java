import java.util.*;

public class node {
    private int depth; //node의 현재 depth (위치정보)

    // NxN 에서 퀸들의 위치 나타냄; goaltest를 빠르게 진행하기 위해 x좌표기준과 y좌표기준을 모두 저장;
    private int[] state_x_index; //node의 state; state_x_index[x 좌표] = y좌표;
    private int[] state_y_index; //node의 state; state_y_index[y 좌표] = x좌표;

    private int depth_limit; //Queen의 갯수 N을 의미함;
    //private node parent; //tree 구조에서 parent node를 의미함;

    public node(int limit){
        this.depth_limit = limit;//퀸의 갯수 설정
        this.state_x_index = new int[limit];//x좌표 기준 배열 생성
        this.state_y_index = new int[limit];//y좌표 기준 배열 생성
        //this.parent = null;
    }


    public void set_node(node parent, int placement, int limit){
        this.depth_limit = limit; //최대 depth. N-Queens 의 N
        //this.parent = parent;
        this.depth = parent.get_depth()+1;//부모node의 depth 보다 1증가

        //부모node가 가지고있는 state 정보를 copy하여 저장.
        System.arraycopy(parent.state_x_index,0, this.state_x_index,0, limit);
        System.arraycopy(parent.state_y_index,0, this.state_y_index,0, limit);

        //부모node 부터 카피한 state정보를 업데이트 해줌.(새로운 Queen의 placement를 시행한것.)
        this.state_x_index[this.depth] = placement;
        this.state_y_index[placement] = this.depth;
    }


    public void set_root(){
        this.depth = -1; // root의 depth는 -1
        //state_index[] 의 index를 depth와 연동시켜 효과적으로 접근하기 위해 root의 depth를 -1로 함.
        for(int i = 0; i < this.depth_limit; i++){
            //각 행, 렬에 퀸이 위치하지 않는 초기 값을 -1로 표시.
            //값이 -1이 아닐경우 각 (행,렬) 혹은 (렬,행) 에 queen이 놓여있음을 표시.
            //만약 같은 행 또는 열에 퀸이 놓일 경우 가장 마지막에 놓여진 열 또는 행의 값 저장.
            //leaf_node의 state_y_index[] 값들 중 하나라도 -1 이 있는경우 하나의 row(x축)에 적어도 두개 이상 퀸이 놓여있다는 뜻.
            this.state_x_index[i] = -1;
            this.state_y_index[i] = -1;
        }
    }


    public int get_depth(){
        return this.depth;
    } //node의 depth를 반환하는 method


    public boolean is_leaf(){
        //현재 node가 leafnode인지 검사. goaltest를 할때 조건으로 사용. 끝까지 탐색했는지 검사.
        return ((this.depth+1) == this.depth_limit);
    }


    public boolean goal_test(){
        //leafnode일때 올바른 Solution 인지를 검사.
        if(this.is_leaf()){

            //0. (세로 중복 검사)
            // tree 형태로 0번째 col 부터 (n-1)번째 col 까지 queen 을 배치하기 때문에
            // 같은 세로선 상에는 두개이상의 queen이 놓일수 없다. 따로 검사하지 않는다.

            //1. (가로 중복 검사)
            // 같은 가로선 상에 있는 경우. 하나의 row에 두개 이상의 queen 이 놓여있는지를 검사한다.
            // 이때 state_y_index[] 를 탐색했을때 값이 -1 인 index가 존재하는경우,
            // 그 index에 해당하는 row 안에는 queen이 놓여있지 않다는 의미이고,
            // 이는 queen이 다른 임의의 한개 이상의 row에 두번 배치 되어있다는 의미이므로 false를 반환한다.
            for(int i=0; i < this.depth_limit; i++) {
                if (this.state_y_index[i] == -1) {
                    return false;// (같은 가로 선상에 있는 경우)
                }
            }

            //2. (대각선 중복 검사)
            // 대각선의 경우 동북쪽으로 뻗는 대각선 과 동남쪽으로 뻗는대각선 두경우를 생각해주어야한다.
            // 대각선 검사는 위에 1번.가로중복검사를 통과했다는 가정하에 진행한다.
            // 각각의 queen들의 위치를 (x좌표,y좌표) 로 표현할때,
            // (1)동북쪽 대각선들의 좌표들은 모두 (x좌표+y좌표) 값이 같고,
            // (2)동남쪽 대각선들의 좌표들은 모두 (x좌표-y좌표) 값이 같기 때문에
            // 이를 이용하여 이중 for문 내에서
            // (0). 좌표가 비교대상이랑 같지 않고(같은경우는 하나의 위치이므로)
            // (1).(x+y)가 비교대상과 같거나 (2).(x-y)가 같은 지점이 하나라도 발견될시
            // 곧바로 false를 반환한다.
            for(int i=0; i < this.depth_limit; i++){
                for (int j = 0; j < this.depth_limit; j++) {//대각선상에 있는 경우
                    if (i!=j && (((i-this.state_x_index[i])==(j-this.state_x_index[j]))||((i+this.state_x_index[i])==(j+this.state_x_index[j])))){
                        return false;//대각선 상에 있는경우
                    }
                }
            }

            // 위의 1.2번 검사를 모두 통과하면 알맞는 Solution 이므로 true를 반환한다.
            return true; //모든 조건을 만족하는 경우
        }

        //leafnode가 아닌경우 탐색을 끝까지 진행하기위해 무조건적으로 false를 반환한다.
        else {
            return false; // 아직 끝까지 탐색이 안된 경우
        }
    }


    public String print_result(){//함수 실행후 goaltest를 통과할 경우 결과를 담은 string을 반환한다.
        String result;
        result = "Location :";
        //System.out.print("Location : ");
        for (int i = 0; i < this.depth_limit; i++) {
            //System.out.print(this.state_x_index[i]);
            //System.out.print(" ");
            result = result + " " + this.state_x_index[i];
        }
        //System.out.print("\n");
        result = result + "\n";

        return result;
    }


    //StackOverFlow - recursion// //bad//
    // 첫 디자인은 fringe에서 pop할때마다 recursive 하게 같은 함수를 호출하는 방식으로 디자인함.
    // N^N 번 만큼 함수를 recursive 하게 호출함으로 N이 숫자 5를 넘어가면 StackOverFlow 발생.
    /*
    public void dfs_n_queens_recur_error(Stack<node> dfs_fringe, int limit){
        if(this.goal_test()){
            this.print_result();
        }
        else if (this.is_leaf() && !dfs_fringe.empty()){
            node poppedNode = dfs_fringe.pop();
            poppedNode.dfs_n_queens_recur_error(dfs_fringe, limit);
        }
        else if (this.is_leaf() && dfs_fringe.empty()){
            System.out.println("NO SOLUTION");
        }
        else if(!this.is_leaf()){
            for (int i = limit-1; i > -1; i--) {
                node newNode = new node(limit);
                newNode.set_node(this, i, limit);
                dfs_fringe.push(newNode);
            }
            node poppedNode = dfs_fringe.pop();
            poppedNode.dfs_n_queens_recur_error(dfs_fringe, limit);
        }
    }
    */


    // DFS version
    // 이 함수는 main 함수 안에서 한번만 실행한다.
    // 함수의 인자는 Stack<node> 형의 dfs_fringe 와 Queen 의 갯수를 의미하는 int 형의 limit 변수가 들어간다.
    // 함수의 반환 값은 실행의 결과를 나타내는 String ("No solution\n" 혹은 "Location : ~") 이다.
    public String dfs_n_queens(Stack<node> dfs_fringe, int limit){
        // 이 함수를 실행하는 객체는 root node이다. root자신을 fringe에 넣고, expanding을 하며 leaf까지 탐색한다.
        dfs_fringe.push(this);

        //fringe가 비어있지 않으면 계속해서 반복문을 실행한다.
        while(!dfs_fringe.empty()){
            //node를 fringe에서 꺼내 expand 해준다.
            node poppedNode = dfs_fringe.pop();

            //만약 꺼낸 노드가 goaltest를 통과하면 solution을 출력하고 종료한다.
            if(poppedNode.goal_test()){
                return poppedNode.print_result();
            }

            //만약 꺼낸 노드가 leafnode가 아닌경우 이 노드의 childnode들을 생성하여 fringe에 넣어준다.
            else if(!poppedNode.is_leaf()){
                for (int i = 0; i < limit; i++) {
                    node newNode = new node(limit);
                    newNode.set_node(poppedNode, i, limit);
                    dfs_fringe.push(newNode);
                }
            }
        }

        //fringe가 비어있는 경우는 모든 가능한 node를 다 탐색하고 Solution이 없을 경우이다.
        //이 경우 No solution을 출력한다.
        //System.out.println("NO SOLUTION");
        return "No solution\n";
    }

    // BFS version
    // BFS와 DFS의 차이점은 fringe가 Queue냐 Stack이냐의 차이이다. 구조는 동일하다.
    // 이 함수는 main 함수 안에서 한번만 실행한다.
    // 함수의 인자는 Queue<node> 형의 bfs_fringe 와 Queen 의 갯수를 의미하는 int 형의 limit 변수가 들어간다.
    // 함수의 반환 값은 실행의 결과를 나타내는 String ("No solution\n" 혹은 "Location : ~") 이다.
    public String bfs_n_queens(Queue<node> bfs_fringe, int limit){
        // 이 함수를 실행하는 객체는 root node이다. root자신을 fringe에 넣고, expanding을 하며 leaf까지 탐색한다.
        bfs_fringe.offer(this);

        //fringe가 비어있지 않으면 계속해서 반복문을 실행한다.
        while(!bfs_fringe.isEmpty()){

            //node를 fringe에서 꺼내 expand 해준다.
            node polledNode = bfs_fringe.poll();

            //만약 꺼낸 노드가 goaltest를 통과하면 solution을 출력하고 종료한다.
            if(polledNode.goal_test()){
                return polledNode.print_result();
            }

            //만약 꺼낸 노드가 leafnode가 아닌경우 이 노드의 childnode들을 생성하여 fringe에 넣어준다.
            else if(!polledNode.is_leaf()){
                for (int i = 0; i < limit; i++) {
                    node newNode = new node(limit);
                    newNode.set_node(polledNode, i, limit);
                    bfs_fringe.offer(newNode);
                }
            }
        }

        //fringe가 비어있는 경우는 모든 가능한 node를 다 탐색하고 Solution이 없을 경우이다.
        //이 경우 No solution을 출력한다.
        //System.out.println("NO SOLUTION");
        return "No solution\n";
    }

    // DFID version
    // DFID와 DFS의 차이점은 node들을 expand 할때 cutline limit를 두고탐색하는 점이다. 전제적인 구조는 비슷하다.
    // 이 함수는 main 함수 안에서 한번만 실행한다.
    // 함수의 인자는 Queue<node> 형의 dfid_fringe 와 Queen 의 갯수를 의미하는 int 형의 limit 변수가 들어간다.
    // 함수의 반환 값은 실행의 결과를 나타내는 String ("No solution\n" 혹은 "Location : ~") 이다.
    public String dfid_n_queens(Stack<node> dfid_fringe, int limit){
        int cutline = 0; // 초기 cutline은 0으로 한다. root node로 시작할때 root의 depth는 -1이기 때문

        //cutline이 N값 (limit값) 보다 작을때만 반복문을 실행한다.
        while(cutline < limit) {
            //항상 Root부터 탐색을 시작한다.
            dfid_fringe.push(this);
            //fringe가 비어있지 않으면 계속해서 반복문을 실행한다.
            while (!dfid_fringe.empty()) {

                //node를 fringe에서 꺼내 expand 해준다.
                node poppedNode = dfid_fringe.pop();

                //DFS,BFS와는 다른 조건이 더 붙는데 꺼낸 노드의 depth cutline안에 드는지 확인한다.
                //만약 cutline을 만족하지 못할경우 안쪽 while문을 벋어난후 cutline을 증가시킨다.
                if(poppedNode.get_depth() <= cutline) {

                    ////만약 꺼낸 노드가 goaltest를 통과하면 solution을 출력하고 종료한다.
                    if (poppedNode.goal_test()) {
                        return poppedNode.print_result();
                    }
                    //만약 꺼낸 노드가 leafnode가 아닌경우 이 노드의 childnode들을 생성하여 fringe에 넣어준다.
                    else if (!poppedNode.is_leaf()) {
                        for (int i = 0; i < limit; i++) {
                            node newNode = new node(limit);
                            newNode.set_node(poppedNode, i, limit);
                            dfid_fringe.push(newNode);
                        }
                    }
                }
            }
            cutline ++;
        }

        //f모든 가능한 cutline 의 node를 다 탐색하고 Solution이 없을 경우이다.
        //이 경우 No solution을 출력한다.
        return "No solution\n";
    }
}
