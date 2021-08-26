package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("MANCALA");
        Scanner input = new Scanner(System.in);
        //String string = input.next();
        //System.out.println(string);
        Mancala mancala=new Mancala();
        mancala.startMancala();
    }
}
