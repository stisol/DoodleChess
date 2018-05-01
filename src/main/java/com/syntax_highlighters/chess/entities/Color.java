package com.syntax_highlighters.chess.entities;

/**
 * Represents whether a piece is black or white.
 */
public class Color {
    private final boolean color;

    private static boolean WHITE_B = true;
    private static boolean BLACK_B = false;

    /**
     * The instance of color that is white.
     */
    public static Color WHITE = new Color(WHITE_B);

    /**
     * The instance of color that is black.
     */
    public static Color BLACK = new Color(BLACK_B);

    private Color(boolean color) {
        this.color = color;
    }

    /**
     * Whether or not the color is white.
     * @return Whether or not the color is white.
     */
    public Boolean isWhite() {
        return color == WHITE_B;
    }

    /**
     * Whether or not the color is black.
     * @return Whether or not the color is white.
     */
    public Boolean isBlack() {
        return color == BLACK_B;
    }

    /**
     * The opposite color of this.
     * @return The opposite color of this.
     */
    public Color opponentColor() {
        if (this == WHITE) return BLACK;
        else return WHITE;
    }
}
