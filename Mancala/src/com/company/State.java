package com.company;

public class State {
    public int player_1Side;
    public int player_2Side;
    public int player_1Storage;
    public int player_2Storage;
    public int boardSize;
    public int[][] board ;
    public int extraMove =0;
    public int totalExtraMovePlayer1=0;
    public int totalExtraMovePlayer2=0;
    public int value;
    public int stoneCapturedPlayer1=0;
    public int stoneCapturedPlayer2=0;
    public int move;

    State(){}

    State(int size){
        this.boardSize = size;
        this.board =new int [2][this.boardSize+1];
    }

    void CopyState(State state){
        this.boardSize=state.boardSize;
        this.board=new int [2][this.boardSize+1];
        this.totalExtraMovePlayer1=state.totalExtraMovePlayer1;
        this.totalExtraMovePlayer2=state.totalExtraMovePlayer2;
        for(int i=0;i<2;i++){
            for(int j=0;j<(boardSize+1);j++){
                this.board[i][j]=state.board[i][j];
            }
        }
        this.stoneCapturedPlayer2=state.stoneCapturedPlayer2;
        this.stoneCapturedPlayer1=state.stoneCapturedPlayer1;
    }

    boolean isTerminalState(){
        if(this.getPlayer_1Side() ==0 || this.getPlayer_2Side() ==0)
        {
            countResultState();
            return true;
        }
        else return false;
    }

    int getPlayer_1Side(){
        int count=0;
        for(int i=0;i<this.boardSize;i++){
            count=count+this.board[0][i];
        }
        return count;
    }
    int getPlayer_2Side(){
        int count=0;
        for(int i=0;i<this.boardSize;i++){
            count=count+this.board[1][i];
        }
        return count;
    }
    void printBoard(){
        System.out.println("        ---------------------------------------------------------");
        for(int i=0;i<2;i++){
            System.out.print("player : "+(i+1)+" :");
            for(int j=0;j<boardSize+1;j++){
                if(j==boardSize) System.out.print("storage:");
                System.out.print("  "+board[i][j]+"     ");
            }
            System.out.println();
        }
        System.out.println("        ---------------------------------------------------------");
    }
    int getPlayer_1Storage(){
        return  board[0][boardSize];
    }
    int getPlayer_2Storage(){
        return board[1][boardSize];
    }
    int getPlayerStorage(int player){
        if (player==1)return getPlayer_1Storage();
        else return getPlayer_2Storage();
    }
    int getPlayerSide(int player){
        if (player==1)return getPlayer_1Side();
        else return getPlayer_2Side();
    }
    void countResultState(){
        int count;
        count=getPlayer_1Side();
        if(count!=0){
            board[0][boardSize]=getPlayer_1Storage()+count;
            for (int i=0;i<boardSize;i++)board[0][i]=0;
        }
        count=getPlayer_2Side();
        if(count!=0){
            board[1][boardSize]=getPlayer_2Storage()+count;
            for (int i=0;i<boardSize;i++)board[1][i]=0;
        }
    }
    void showResultState(){
        countResultState();
        printBoard();
        if(getPlayer_1Storage()>getPlayer_2Storage()) System.out.println("player 1 wins");
        else System.out.println("player 2 wins");
    }
    void addStoneCaptured(int player,int stones){
        if(player==1)stoneCapturedPlayer1+=stones;
        else if(player==2)stoneCapturedPlayer2+=stones;
    }
    int getStoneCaptured(int player){
        if(player==1)return stoneCapturedPlayer1;
        else return stoneCapturedPlayer2;
    }
    void addTotalExtraMove(int player,int move){
        if(player==1)totalExtraMovePlayer1+=move;
        else if(player==2)totalExtraMovePlayer2+=move;
    }
    int getTotalExtraMove(int player){
        if(player==1)return totalExtraMovePlayer1;
        else return totalExtraMovePlayer2;
    }
}
