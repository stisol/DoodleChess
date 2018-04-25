package com.syntax_highlighters.chess;

import java.util.Objects;

import com.syntax_highlighters.chess.entities.ChessPieceKing;
import com.syntax_highlighters.chess.entities.ChessPieceRook;

/**
 * Special move which places the king behind the rook.
 *
 * Only legal if neither the king nor the rook has moved, the path between them
 * is clear, the king is not in check, and the king doesn't pass over or end up
 * at a position threatened by an enemy piece.
 *
 * The king moves two steps towards the rook, and the rook is then placed on the
 * other side of the king.
 *
 * This class does not perform any checks concerning whether castling is legal
 * between the king and the rook.
 */
public class CastlingMove extends Move {
    private final Position rookOldPos;
    private final Position rookNewPos;

    /**
     * IMPORTANT: This must be changed on every release of the class
     * in order to prevent cross-version serialization.
     */
    private static final long serialVersionUID = 1;

    /**
     * Construct a castling move between the given king and rook.
     *
     * @param king The king to castle with.
     * @param rook The rook to castle with.
     */
    public CastlingMove(ChessPieceKing king, ChessPieceRook rook) {
        this.oldPos = king.getPosition();
        this.rookOldPos = rook.getPosition();

        if (rook.getPosition().getX() < king.getPosition().getX()) {
            this.newPos = king.getPosition().west(2);
            this.rookNewPos = newPos.east(1);
        }
        else {
            this.newPos = king.getPosition().east(2);
            this.rookNewPos = newPos.west(1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void DoMove(Board b) {
        if (hasDoneMove) throw new RuntimeException("Move has already been done.");
        b.putAtPosition(newPos, getPiece(b));
        b.putAtPosition(rookNewPos, b.getAtPosition(rookOldPos));
        hasDoneMove = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void UndoMove(Board b) {
        if (!hasDoneMove) throw new RuntimeException("Can not undo a move that has not been done");
        b.putAtPosition(oldPos, getPiece(b));
        b.putAtPosition(rookOldPos, b.getAtPosition(rookNewPos));
        hasDoneMove = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof CastlingMove)) return false;
        CastlingMove o = (CastlingMove) other;
        return super.equals(o)
            && Objects.equals(rookOldPos, this.rookOldPos)
            && Objects.equals(rookNewPos, this.rookNewPos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.rookOldPos, this.rookNewPos);
    }
}
