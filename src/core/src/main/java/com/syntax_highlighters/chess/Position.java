package com.syntax_highlighters.chess;
import java.util.Objects; // Objects.hash

/**
 * Position on a grid (x, y).
 *
 * This class should be immutable. The Position is one-based.
 */
public class Position {

    private int x, y;
    
    /**
     * Constructor.
     *
     * @param x The X coordinate on the board
     * @param y The Y coordinate on the board
     */
    public Position(int x, int y) {
        this.x = x; this.y = y;
    }


    /**
     * Get the X coordinate of this Position.
     *
     * @return The X coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the Y coordinate of this Position.
     *
     * @return The Y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Return the position in chess notation.
     *
     * @return A string on the form [a-h][1-8]
     */
    @Override
    public String toString() {
        // novalidity
        char file = (char) ('a' + (this.x-1));
        char rank = (char) ('0' + this.y);
        return "" + file + rank;
    }

    /**
     * Return the position mirrored vertically onto the other side of the board.
     *
     * @param boardHeight The number of rows on the board
     * @return The mirrored position
     */
    public Position mirrorVertical(int boardHeight) {
        // novalidity
        return new Position(this.getX(), boardHeight - this.getY());
    }

    /**
     * Return the position mirrored horizontally onto the other side of the board.
     *
     * @param boardWidth The number of columns on the board
     * @return The mirrored position
     */
    public Position mirrorHorizontal(int boardWidth) {
        // novalidity
        return new Position(boardWidth - this.getX(), this.getY());
    }

    /**
     * Parse a board position written in chess notation, and return a Position.
     *
     * @param pos The position in chess notation, of the form [A-Ha-h][1-8]
     * @return The corresponding logical position
     */
    public static Position fromChessNotation(String pos) {
        // precondition: pos is of the form [A-Ha-h][1-8]
        String lowerPos = pos.toLowerCase(); // ignore case
        assert lowerPos.length() == 2;
        
        char file = lowerPos.charAt(0);
        char rank = lowerPos.charAt(1);
        assert "abcdefgh".contains(""+file);
        assert "12345678".contains(""+rank);
        
        int xPos = (int)(file - 'a' + 1);
        int yPos = (int)(rank - '0');
        return new Position(xPos, yPos);
    }

    /**
     * Custom equality method.
     *
     * Two positions are equal if their x and y coordinates are equal.
     *
     * @param other The object to test for equality
     * @return true if the object is equal to this Position, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Position)) return false;
        Position o = (Position) other;
        return o.x == this.x && o.y == this.y;
    }

    /**
     * Custom hashCode method.
     *
     * Use Objects.hash for simplicity. Considers the x and y coordinate.
     *
     * @return The hash of the Position
     */
    @Override
    public int hashCode() {
        return Objects.hash(new Integer(this.x), new Integer(this.y));
    }
}