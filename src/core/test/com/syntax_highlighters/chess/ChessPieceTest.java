package syntax_highlighters.chess;

import com.syntax_highlighters.chess.*;
import com.syntax_highlighters.chess.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessPieceTest {
    @Test
    void TestKingMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece king = new ChessPieceKing(new Position(4,4), false);
        pieces.add(king);
        Board board = new Board(pieces);

        List<Move> moves = king.allPossibleMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    void TestBishopMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceBishop(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(13, moves.size());
    }
    @Test
    void TestQueenMoves() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceQueen(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(27, moves.size());
    }
    @Test
    void TestRookMoves2() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceRook(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(14, moves.size());
    }

    @Test
    void TestPawn() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPiecePawn(new Position(7,7), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(2, moves.size());
    }

    @Test
    void TestKnight() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceKnight(new Position(4,4), false);
        pieces.add(piece);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    void TestCastling() {
        ArrayList<IChessPiece> pieces = new ArrayList<>();
        IChessPiece piece = new ChessPieceKing(new Position(5,1), true);
        IChessPiece piece2 = new ChessPieceRook(new Position(8,1), true);
        IChessPiece piece3 = new ChessPieceRook(new Position(1,1), true);
        pieces.add(piece);
        pieces.add(piece2);
        pieces.add(piece3);
        Board board = new Board(pieces);

        List<Move> moves = piece.allPossibleMoves(board);
        assertEquals(7, moves.size());
    }

    @Test
    public void checkMateKingTest(){
        Board b = new Board();
        b.setupNewGame();

        b.movePiece(b.getAtPosition(new Position(5,2)),new Position(5,4));
        b.movePiece(b.getAtPosition(new Position(4,1)),new Position(6,3));
        b.movePiece(b.getAtPosition(new Position(6,1)),new Position(3,4));
        b.movePiece(b.getAtPosition(new Position(6,3)),new Position(6,7));

        List<Move> moves = b.getAtPosition(new Position(5,8)).allPossibleMoves(b);
        assertEquals(0, moves.size());

    }
}
