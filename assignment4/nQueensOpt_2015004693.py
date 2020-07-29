from z3 import *
import time

# Number of Queens를 input으로 받음.
print("N: ")
N = int(input())

#start time 계산.
start = time.time()

# Variables
# 기존 nQueensNaive.py 에서 2차원 배열 형태로 선언되어 있는 것을
# 1차원 배열형태로 선언해준다.
# 퀸의위치(col,row)를 (i,X[i]) 형태로 나타낸다.
# 사실상 좌표계의 (x좌표,y좌표)라고 할 수 있다.
# X[i]=k 일때 i번째 column에서 퀸의 row 위치는 k이다.
X = [Int("x_%s" % (col)) for col in range(N)]

# Constraints
# X[i]의 값이 각각 0에서 N-1 사이의 정수를 갖는지 확인하는 constraint
# "0 <= X[i] < N" 을 표현하기 위해 And() 를 사용하였다.
domain = [And(X[col] >= 0, X[col] < N) for col in range(N)]

# 각 Column별로 하나의 퀸을 무조건 배치한다 가정햇을때
# 같은 Column에 두개 이상의 퀸이 절대로 놓일 수 없으므로 "colConst"는 따로 만들지 않음.

# 같은 row에 두개 이상의 퀸이 존재할수 없게 하기위해
# X[] 배열안의 값이 모두 다른 값을 갖는지 확인하기위한 constraint
# And(X[i]!=X[j],i!=j) 를 사용하지 않은 이유는
# And()연산을 하는 시간을 반으로 줄이고 for문의 탐색 크기를 N^2 에서 (N^2)/2으로 줄이기 위함.
rowConst = [X[i] != X[j] for i in range(N) for j in range(i+1,N) ]

# 대각선 상에 퀸이 존재할수 없게 하기위해
# |X[i]-i| != |X[j]-j| 를 만족하는지를 확인하기 위한 constraint
# nQueensNaive.py 처러 abs()를 사용하려 하였지만 error가 발생하여 두경우를 만들어줌.
# And(X[i]-i!=X[j]-j,i!=j) 를 사용하지 않은 이유는 위와 마찬가지로
# And()연산을 하는 시간을 반으로 줄이고 for문의 탐색 크기를 N^2에서 (N^2)/2으로 줄이기 위함.
digConstSub = [(X[i]-i) != (X[j]-j) for i in range(N) for j in range(i+1,N)]
digConstSum = [(X[i]+i) != (X[j]+j) for i in range(N) for j in range(i+1,N)]
 
# 모든 constraint를 합쳐준다.
# N_Queens_c 는 위의 constraints들의 교집합을 의미한다.
N_queens_c = domain + rowConst + digConstSub + digConstSum

s = Solver()
s.add(N_queens_c)

if s.check() == sat:
    m = s.model()
    # 출력할때 row의 index를 1부터 N으로 하기 위해 X[i]+1을 해준다.
    r = [m.evaluate((X[i]+1)) for i in range(N)]
    print(r)
        

print("elapsed time: ", time.time() - start, " sec")

