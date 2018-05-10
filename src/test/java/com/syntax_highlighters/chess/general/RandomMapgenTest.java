package com.syntax_highlighters.chess.general;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.entities.ChessPieceKing;

import org.junit.jupiter.api.Test;

class RandomMapgenTest{
    private Random rdm = new Random();

    @Test
    void SetupPracGamehasKingsTest(){
        Board board = new Board();
        board.setupPracticeGame(rdm.nextInt(20));

        long kings = board.getAllPieces().stream().filter(p -> p instanceof ChessPieceKing).count();
        assertEquals(2,kings);

    }
}
