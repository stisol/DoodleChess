package com.syntax_highlighters.chess.entities;

import com.syntax_highlighters.chess.Board;
import com.syntax_highlighters.chess.Move;
import com.syntax_highlighters.chess.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessPieceQueen extends AbstractChessPiece {
    public ChessPieceQueen(Position pos, boolean isWhite) {
        super(pos, isWhite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int[] getPositionScoreTable() {
        return new int[]{
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10,   0,   0,  0,  0,   0,   0, -10,
            -10,   0,   5,  5,  5,   5,   0, -10,
             -5,   0,   5,  5,  5,   5,   0,  -5,
              0,   0,   5,  5,  5,   5,   0,  -5,
            -10,   5,   5,  5,  5,   5,   0, -10,
            -10,   0,   5,  0,  0,   0,   0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
        };
    }

    /**
     * Return all possible moves the queen can make.
     *
     * The queen can move any number of steps in any direction, but only as far
     * as the path is not blocked by another piece. If the path is blocked by an
     * enemy piece, the queen may capture that piece and go no further.
     *
     * @param board The current state of the board
     *
     * @return A List of all the possible moves the piece can make
     */
    @Override
    public List<Move> allPossibleMoves(Board board) {
        return allPossibleMovesUnfiltered(board).stream()
                .filter(m -> !board.movePutsKingInCheck(board, m, isWhite))
                .collect(Collectors.toList());
    }

    @Override
    public List<Move> allPossibleMovesUnfiltered(Board board) {
        List<Move> ne = movesInDirection(1, 1, board);
        List<Move> nw = movesInDirection(-1, 1, board);
        List<Move> se = movesInDirection(-1, -1, board);
        List<Move> sw = movesInDirection(1, -1, board);
        List<Move> n = movesInDirection(0, 1, board);
        List<Move> e = movesInDirection(1, 0, board);
        List<Move> s = movesInDirection(0, -1, board);
        List<Move> w = movesInDirection(-1, 0, board);
        return Stream.of(ne, nw, se, sw, n, e, s, w)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChessPiece copy() {
        return new ChessPieceQueen(this.getPosition(), this.isWhite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPieceScore() {
        return 900;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAssetName() {
        return isWhite() ? "queen_white.png" : "queen_black.png";
    }
}
