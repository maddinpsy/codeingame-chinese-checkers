import sys
import math
import random
import time

# Auto-generated code below aims at helping you parse
# the standard input according to the problem statement.

Move = tuple[tuple[int,int,int],tuple[int,int,int]]
Board = dict[tuple[int,int],int]

def is_on_map(q:int,r:int)->bool:
    s=-q-r
    if abs(q)>6 or abs(r)>6 or abs(s)>6:
        return False
    if int(abs(q)>3) + int(abs(r)>3) + int(abs(s)>3) > 1:
        return False
    return True

def get_all_moves(pieces:Board)->list[Move]:
    allMoves:list[Move] = []
    for p in pieces:
        if pieces[p] == 0:
            allMoves+=get_jump_moves(pieces,p[0],p[1])
            allMoves+=get_push_moves(pieces,p[0],p[1])
    return allMoves

def get_push_moves(pieces:Board, q:int, r:int)->list[Move]:
    moves:list[Move] = []
    for dq in range(-1,2):
        for dr in range(-1,2):
            if dr != dq:
                if (q+dq,r+dr) not in pieces:
                    if is_on_map(q+dq,r+dr):
                        moves+=[((q,r,-q-r),(q+dq,r+dr,-q-dq-r-dr))]
    return moves

def get_jump_moves(pieces:Board, q:int, r:int)->list[Move]:
    moves:list[Move] = []
    s=-q-r
    openList:list[tuple[int,int]]=[(q,r)]
    closedList:set[tuple[int,int]]=set()

    while len(openList):
        cur = openList.pop()
        closedList.add(cur)
        for dq in range(-1,2):
            for dr in range(-1,2):
                if dr != dq:
                    nq = cur[0]+dq*2
                    nr = cur[1]+dr*2
                    ns = -nq-nr
                    if (nq,nr) in closedList:
                        continue
                    if is_on_map(nq,nr):
                        if (cur[0]+dq,cur[1]+dr) in pieces and (nq,nr) not in pieces:
                            openList+=[(nq,nr)]
                            moves+=[((q,r,s),(nq,nr,ns))]
    return moves

def get_score(q:int,r:int)->int:
    return (abs(q+6)+abs(r-3)+abs(-r-q-3))//2

def get_best_random(pieces:Board)->Move:
    bestMoves:list[Move] = []
    rating:int=100000
    allMoves = get_all_moves(pieces)
    for move in allMoves:
        score = get_score(move[1][0],move[1][1]) - get_score(move[0][0],move[0][1])
        if score < rating:
            rating = score
            bestMoves = [move]
        elif score==rating:
            bestMoves += [move]
    
    return random.choice(bestMoves)


# game loop
while True:
    n = int(input())
    pieces={}
    for i in range(n):
        _id, q, r, s = [int(j) for j in input().split()]
        pieces[(q,r)]=_id
    
    start,end = get_best_random(pieces)
    # Write an answer using print
    # To debug: print("Debug messages...", file=sys.stderr)

    print(*start,*end)