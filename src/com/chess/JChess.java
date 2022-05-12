package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class JChess {

    public static void main(String[] args){

        Board board = Board.createStandardBoard(); //creating board

        System.out.println(board); // printing board as a strings

        Table table = new Table(); // creating JFrame table

    }
}
