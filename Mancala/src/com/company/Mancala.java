package com.company;

import java.util.Scanner;

public class Mancala {
    int size=6,stonesPerBucket=4 ,w1=5,w2=1,w3=2;
    Scanner scanner = new Scanner(System.in);

    int toggleP(int p){
        if (p==1)p=  0;
        else if(p==0)p=  1;
        return p;
    }
    int togglePlayer(int p){
        if (p==1)return  2;
        else if(p==2)return  1;
        return 0;
    }
    void getInitialState(State state){
        state.player_1Side=state.boardSize*stonesPerBucket;
        state.player_2Side=state.boardSize*stonesPerBucket;
        state.player_1Storage=0;
        state.player_2Storage=0;
        for(int i=0;i<state.board.length;i++)
        {
            for(int j=0;j<state.board[i].length;j++){
                if(j !=state.boardSize){
                    state.board[i][j]=stonesPerBucket;
                }
            }

        }
    }
    State getResultForAction(State state, int action, int player){
        int index= action, p =player-1 ;
        boolean extraTurn = false , stoneCapture=false;
        int stones= state.board[p][action-1];
        state.board[p][index-1]=0;
        while (stones>0){
            state.board[p][index]++;
            //state.printBoard();
            if ((stones==1 && p==(player-1)) && state.board[p][index]==1 && index!=state.boardSize){
                stoneCapture=true;
                break;
            }
            else stoneCapture=false;
            if ((stones==1 && p==player-1) && index==state.boardSize)extraTurn=true;
            else extraTurn=false;
            index++;
            stones--;
            if(p != player-1 && index==state.boardSize){
                p= toggleP(p);
                index=0;
            }
            if(index>=state.boardSize+1){
                p= toggleP(p);
                index=0;
            }
        }
        if (stoneCapture==true){
            int collect= state.board[p][index];
            int p_prime=toggleP(p);
            if(state.board[p_prime][index]!=0){
                collect=collect+state.board[p_prime][index];
                state.addStoneCaptured(player,collect);
                state.board[p][index]=0;
                state.board[p_prime][index]=0;
                state.board[p][state.boardSize]+=collect;
            }

        }
        if (extraTurn==true && state.isTerminalState()==false){
            state.addTotalExtraMove(player,1);
            state.extraMove=1;
        }
        else state.extraMove=0;
        return state;
    }
    int MiniMaxWithHeu(State state,int depth, int alpha, int beta,boolean max,int player, int heuristic,int forPlayer){
        if(depth==0 ||state.isTerminalState()==true ){
            int h1=state.getPlayerStorage(player)-state.getPlayerStorage(togglePlayer(player));
            int h2=state.getPlayerSide(player)-state.getPlayerSide(togglePlayer(player));
            int h3=state.getTotalExtraMove(forPlayer);
            int h4=state.getStoneCaptured(forPlayer);
            if(heuristic==1){
                return h1;
            }
            else if(heuristic==2){
                return w1*h1+w2*h2;
            }
            else if(heuristic==3){
                //System.out.println("extra move = "+state.getTotalExtraMove(forPlayer));
                return w1*h1+w2*h2+w3*h3;
            }
            else if(heuristic==4){
                //System.out.println("stone captured = "+state.getStoneCaptured(forPlayer));
                return w1*h1+w2*h2+w3*h3+h4;
            }
        }

        if(max==true){
            int v=-9999;
            for(int i=0;i<state.boardSize;i++){
                if(state.board[player-1][i]!=0){
                    State state_copy=new State();
                    state_copy.CopyState(state);
                    getResultForAction(state_copy,(i+1),player);
                    while (state_copy.extraMove>0 && state_copy.isTerminalState()==false){
                        state_copy.extraMove=0;
                        //MiniMaxWithHeu(state_copy,2,-9999,9999,true,player);getResultForAction(state_copy,state_copy.move,player);
                        getResultForAction(state_copy,getMove2(state_copy,player),player);

                    }
                    int m=MiniMaxWithHeu(state_copy,depth-1,alpha,beta,false,player,heuristic,forPlayer);
                    if(m>v){
                        v=m;
                        state.move=i+1;

                    }
                    if(v>alpha)alpha=m;
                    if(beta<=alpha)break;

                }
            }
            return v;
        }
        else {
            int v=9999;
            for(int i=0;i<state.boardSize;i++){
                if(state.board[player-1][i]!=0){
                    State state_copy=new State();
                    state_copy.CopyState(state);
                    getResultForAction(state_copy,(i+1),togglePlayer(player));
                    while (state_copy.extraMove>0 && state_copy.isTerminalState()==false){
                        state_copy.extraMove=0;
                        //MiniMaxWithHeu(state_copy,2,-9999,9999,true,togglePlayer(player));getResultForAction(state_copy,state_copy.move,togglePlayer(player));

                        getResultForAction(state_copy,getMove2(state_copy,togglePlayer(player)),togglePlayer(player));
                    }
                    int m=MiniMaxWithHeu(state_copy,depth-1,alpha,beta,true,player,heuristic,forPlayer);
                    if(m<v){
                        v=m;
                        state.move=i+1;
                    }
                    if(v<beta)beta=m;
                    if(beta<=alpha)break;

                }
            }
            return v;
        }
    }

    int getMove2(State state,int player){
        int move=0;
        int max=-9999,alpha=-9999,beta=9999;
        //for (int i=state.boardSize-1;i>=0;i--){
        for (int i=0;i<state.boardSize;i++){
            if(state.board[player-1][i]!=0){
                State state_copy=new State();
                state_copy.CopyState(state);
                //System.out.println("player="+player+" my action = "+(i+1));
                getResultForAction(state_copy,(i+1),player);
                int m= state_copy.getPlayerStorage(player);
                //System.out.println("for move = "+(i+1)+" value = "+m);
                if(m>max){
                    max=m;move=i+1;
                }
            }
        }
        return move;
    }
    int AlphaBeta(State state,int depth, int alpha, int beta,boolean max,int player){
        if(depth==0){
            //System.out.println("depth is "+depth+" and value is "+state.getPlayerStorage(player));
            return state.getPlayerStorage(player)+state.getPlayerSide(player);
        }
        if(state.isTerminalState()==true){
            //System.out.println("in terminal state and depth = "+depth);
            return state.getPlayerStorage(player)+state.getPlayerSide(player);
        }
        if(max==true){
            int v=-9999;
            for(int i=0;i<state.boardSize;i++){
                if(state.board[player-1][i]!=0){
                    State state_copy=new State();
                    state_copy.CopyState(state);
                    getResultForAction(state_copy,(i+1),player);
                    while (state_copy.extraMove>0 && state_copy.isTerminalState()==false){
                        state_copy.extraMove=0;
                        //AlphaBeta(state_copy,2,-9999,9999,true,player);getResultForAction(state_copy,state_copy.move,player);
                        getResultForAction(state_copy,getMove2(state_copy,player),player);
                    }
                    int m=AlphaBeta(state_copy,depth-1,alpha,beta,false,player);
                    if(m>v){
                        v=m;
                        state.move=i+1;
                    }
                    if(v>alpha)alpha=m;
                    if(beta<=alpha)break;

                }
            }
            return v;
        }
        else {
            int v=9999;
            for(int i=0;i<state.boardSize;i++){
                if(state.board[player-1][i]!=0){
                    State state_copy=new State();
                    state_copy.CopyState(state);
                    getResultForAction(state_copy,(i+1),togglePlayer(player));
                    while (state_copy.extraMove>0 && state_copy.isTerminalState()==false){
                        state_copy.extraMove=0;
                        //AlphaBeta(state_copy,2,-9999,9999,true,togglePlayer(player));getResultForAction(state_copy,state_copy.move,togglePlayer(player));

                        getResultForAction(state_copy,getMove2(state_copy,togglePlayer(player)),togglePlayer(player));
                    }
                    int m=AlphaBeta(state_copy,depth-1,alpha,beta,true,player);
                    if(m<v){
                        v=m;
                        state.move=i+1;
                    }
                    if(v<beta)beta=m;
                    if(beta<=alpha)break;

                }
            }
            return v;
        }
    }
    State player1(State state){
        System.out.println("player 1:");
        //int action = scanner.nextInt();
        State state_copy=new State();
        state_copy.CopyState(state);
        //int action= getMove(state_copy,1);
       // int action= getMove2(state_copy,1);
        //state_copy.printBoard();
        MiniMaxWithHeu(state_copy,5,-9999,9999,true,1,3,1); int action=state_copy.move;
        //AlphaBeta(state_copy,10,-9999,9999,true,1); int action=state_copy.move;
        System.out.println("action = "+action);
        state = getResultForAction(state,action,1);
        state.printBoard();
        if (state.extraMove>0 && state.isTerminalState()==false ){
            System.out.println("extra move player 1:");
            state=player1(state);
        }
        return state;
    }
    State player2(State state){
        System.out.println("player 2:");
        //int action = scanner.nextInt();
        State state_copy=new State();
        state_copy.CopyState(state);
        //int action= getMove2(state_copy,2);
        MiniMaxWithHeu(state_copy,2,-9999,9999,true,2,3,2); int action=state_copy.move;
        //AlphaBeta(state_copy,3,-9999,9999,true,2);int action=state_copy.move;
        System.out.println("action = "+action);
        state = getResultForAction(state,action,2);
        state.printBoard();
        if (state.extraMove>0 && state.isTerminalState()==false){
            System.out.println("extra move player 2");
            state=player2(state);
        }
        return state;
    }
    void startMancala(){
        State state1 = new State(size);
        getInitialState(state1);
        state1.printBoard();
        while (state1.isTerminalState() == false){
            state1=player1(state1);
            if(state1.isTerminalState() == true)break;
            state1=player2(state1);
        }
        state1.showResultState();

    }
}
