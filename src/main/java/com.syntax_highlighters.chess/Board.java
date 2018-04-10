package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.entities.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * Holds the current state of the board.
 *
 * The board state is stored in a 8x8 grid containing the pieces (null values
 * signifying empty squares).
 *
 * The white player starts at rows 1 and 2
 * The black player starts at rows 7 and 8
 */
public class    Board {
    // constants, just in case
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    
    private Move lastMove;
    private IChessPiece whiteKing = null; // For caching purposes.
    private IChessPiece blackKing = null; // For caching purposes.

    // for performance
    private final int[][] positionsLookupTable = new int[BOARD_HEIGHT][BOARD_WIDTH];

    List<IChessPiece> pieces = new ArrayList<>();

    /**
     * Create an empty board.
     */
    public Board() {
        initBoard();
    }

    /**
     * Create a board from a list of pieces.
     */
    public Board(List<IChessPiece> pieces) {
        this.pieces.addAll(pieces);
        initBoard();
    }

    /**
     * Helper method: reinitialize all positions on the board.
     *
     * Modifies positionsLookupTable.
     */
    private void initBoard() {
        // expensive if we must do it a lot, but hopefully we won't have to
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                positionsLookupTable[i][j] = -1;
            }
        }
        // fill in all the positions of the pieces currently on the board
        for (int i = 0; i < this.pieces.size(); i++) {
            updatePositionIndex(this.pieces.get(i).getPosition(), i);
        }
    }

    /**
     * Helper method: update the lookup table's index based on the position.
     *
     * Modifies positionsLookupTable. No bounds checks.
     */
    private void updatePositionIndex(Position pos, int index) {
        positionsLookupTable[pos.getY()-1][pos.getX()-1] = index;
    }

    /**
     * Helper method: return the index of the piece with the given position in
     * the list of pieces, or -1 if the position is empty.
     *
     * No bounds checks.
     *
     * @return The index of the piece occupying the position in the pieces list,
     * if the position is occupied, or -1 otherwise
     */
    private int lookupPositionIndex(Position pos) {
        return positionsLookupTable[pos.getY()-1][pos.getX()-1];
    }

    /**
     * Sets up a new board with the white and black players in their starting
     * positions.
     */
    public void setupNewGame() {
        final String[] blackPieces = new String[]{
            "RA8", "NB8", "BC8", "QD8", "KE8", "BF8", "NG8", "RH8",
            "PA7", "PB7", "PC7", "PD7", "PE7", "PF7", "PG7", "PH7"
        };

        final String[] whitePieces = new String[]{
            "PA2", "PB2", "PC2", "PD2", "PE2", "PF2", "PG2", "PH2",
            "RA1", "NB1", "BC1", "QD1", "KE1", "BF1", "NG1", "RH1"
        };

        // reset board
        this.pieces = new ArrayList<>();

        // add all white pieces
        for (String p : whitePieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, Color.WHITE);
            putAtPosition(piece.getPosition(), piece);
        }

        // add all black pieces
        for (String p : blackPieces) {
            IChessPiece piece = AbstractChessPiece.fromChessNotation(p, Color.BLACK);
            putAtPosition(piece.getPosition(), piece);
        }
    }

    /**
     *
     * @param hcpW
     * @param hcpB
     * @param bonusW
     * @param bonusB
     */
    public void setupRandomGame(int hcpW, int hcpB,int bonusW, int bonusB) {
        if (bonusW > 16 && bonusB > 16) {
            throw new IllegalArgumentException("Invalid bonus value. Must be 0 < Bonus < 16. ");
        }

        int[] WhiteTiles = generateTileScores(hcpW, bonusW);
        int[] BlackTiles = generateTileScores(hcpB,bonusB);
        int[][] Bmap = generateTileMap(bonusB);
        int[][] Wmap = generateTileMap(bonusW);
        int N = 0;

        IChessPiece Wking = new ChessPieceKing(new Position(4,1), Color.WHITE);
        IChessPiece Bking = new ChessPieceKing(new Position(4,8), Color.BLACK);

        for (int x = 0; x < Bmap.length; x++) {
            for (int y = 0; y < Bmap[x].length; y++) {
                if( Bmap[x][y] == 1 && !(x==4 && y == 1)){
                    IChessPiece piece = AbstractChessPiece.GetPiecefromScore(x+1,7-y+1,BlackTiles[N], Color.BLACK);
                    putAtPosition(piece.getPosition(),piece);
                    N = N + 1;
                }
            }
        }
        N = 0;
        for (int x = 0; x < Wmap.length; x++) {
            for (int y = 0; y < Wmap[x].length; y++) {
                if( Wmap[x][y] == 1 && !(x==4 && y == 8)){
                    IChessPiece piece = AbstractChessPiece.GetPiecefromScore(x+1,y+1,WhiteTiles[N], Color.WHITE);
                    putAtPosition(piece.getPosition(),piece);
                    N = N + 1;
                }
            }
        }
        putAtPosition(Wking.getPosition(),Wking);
        putAtPosition(Bking.getPosition(),Bking);

    }

    /**
     * Generates a tilemap to be used when placing the pieces. The tiles are randomly placed.
     * @param bonus
     * @return
     */
    public int[][] generateTileMap(int bonus){
        int[][] map = new int[8][4];
        Random rand = new Random();

        map[4][1] = 1;

        while (bonus - 1 + 15 > 0){
            int x = rand.nextInt(8);
            int y = rand.nextInt(4);
            if (map[x][y] == 0){
                map[x][y] = 1;
                bonus = bonus - 1;
            }
        }
        return map;
    }

    /**
     * Generates a list of scores to be used in the random generator. The function
     * tries to exploit randomness as best it can.
     * TODO: Make sure it cannot end up in an infinite loop when hcp is high and bonus is low.
     *
     * @param hcp Handicap for each player
     * @param bonus Bonus pieces assigned to a player.
     * @return
     */
    public int[] generateTileScores(int hcp, int bonus){

        Random rdm = new Random();
        int[] N = new int[15 +  bonus];
        int[] values = new int[]{100,320,330,500,900};
        int score = hcp*70;

        while (score > 1){
            int chng = 0;
            int temp = 0;
            int x = rdm.nextInt(N.length );

            if (N[x] < 100 ){
                chng = values[rdm.nextInt(5)];
                N[x] = chng;
                score = score - chng;
            }if (N[x] < 320 && N[x] > 100){
                if (rdm.nextInt(2) == 2) { //Rolls a "dice" to reduce probability.
                    chng = values[rdm.nextInt(4) + 1]; //Decreasing bounds so that it does not "upgrade down".
                    temp = N[x];
                    N[x] = chng;
                    score = score -(chng-temp);
                }
            }else if (N[x] < 330 && N[x] > 320){
                if (rdm.nextInt(2) == 2);{
                    chng = values[rdm.nextInt(3) + 2];
                    temp = N[x];
                    N[x] = chng;
                    score = score -(chng-temp);
                }
            }else if (N[x] < 500){
                if (rdm.nextInt(2) == 2) { //This corresponds to a probability of around 12.5%
                    chng = values[rdm.nextInt(2) + 3];
                    N[x] = chng;
                    score = score -(chng-temp);
                }
            }
        }
        for (int i = 0; i < N.length; i++){
            if (N[i] == 0){N[i] = 100;}
        }
        return N;
    }


    /**
     * Put a piece at a position on the board.
     *
     * @param pos The position where the piece should be put
     * @param piece The piece to place
     */
    public void putAtPosition(Position pos, IChessPiece piece) {
        assert isOnBoard(pos);
        
        IChessPiece target = getAtPosition(pos);
        if (target != null) {
            removePiece(target);
        }

        Position oldPos = piece.getPosition();
        piece.setPosition(pos); // ensure position is correct for this piece
        updatePositionIndex(oldPos, -1);

        if (!this.pieces.contains(piece)) {
            this.pieces.add(piece);
            updatePositionIndex(pos, this.pieces.size()-1); // the index of the newly added piece
        }
        else {
            updatePositionIndex(pos, this.pieces.indexOf(piece));
        }
    }

    /**
     * Put a piece at a position after checking that it's empty.
     *
     * @param pos The position where the piece should be put
     * @param piece The piece to place
     *
     * @throws IllegalArgumentException if the position is already occupied
     */
    public void putAtEmptyPosition(Position pos, IChessPiece piece) {
        if (isOccupied(pos))
            throw new IllegalArgumentException("Position " + pos + " is already occupied");
        putAtPosition(pos, piece);
    }

    /**
     * Check if a given position is occupied.
     *
     * @param pos The position to check
     * @return true if the position is occupied, false otherwise
     */
    public boolean isOccupied(Position pos) {
        return getAtPosition(pos) != null;
    }

    /**
     * Check if a given position is Enemy.
     *
     * @param pos The position to check
     * @return true if the position is Enemy piece, false otherwise
     */
    public boolean isEnemy(IChessPiece piece, Position pos) {
        IChessPiece pieceAtPos = getAtPosition(pos);
        return pieceAtPos != null && pieceAtPos.getColor() != piece.getColor();
    }

    /**
     * Check if a given position is friendly.
     *
     * @param pos The position to check
     * @return true if the position is friendly piece, false otherwise
     */
    public boolean isFriendly(IChessPiece piece, Position pos) {
        IChessPiece pieceAtPos = getAtPosition(pos);
        return pieceAtPos != null && pieceAtPos.getColor() == piece.getColor();
    }

    /**
     * Get the piece at a given position.
     *
     * @param pos The position on the board
     * @return The piece at the position
     */

    public IChessPiece getAtPosition(Position pos) {
        //assert isOnBoard(pos);
        if (!isOnBoard(pos)) return null;
        int index = lookupPositionIndex(pos);
        if (index != -1) {
            return this.pieces.get(index);
        }
        return null;
    }

    /**
     * Return a list of all the pieces on the board.
     *
     * @return A list of all the pieces currently on the board.
     */
    public List<IChessPiece> getAllPieces() {
        // Ensure that manipulating the returned list cannot modify the internal
        // list of the board
        // This does not fully encapsulate the board, but it does hopefully help
        // against accidentally adding/removing pieces without intending to
        List<IChessPiece> copied = new ArrayList<>();
        copied.addAll(this.pieces);
        return copied;
    }

    /**
     * If the king is moving 2 steps in any direction it can only be castling.
     * If king is castling, the ability to caste has already been checked, so the rook can move to it's new position
     * before the king is moved, without taking an extra turn.
     */
    @Deprecated
    private void performCastling(IChessPiece piece, Position toPosition){
        Position rookpos, oldRookpos;
        if (toPosition.getX() < 5) {
            oldRookpos = piece.getPosition().west(4);
            rookpos = toPosition.east(1);
        }
        else {
            oldRookpos = piece.getPosition().east(3);
            rookpos = toPosition.west(1);
        }
        ChessPieceRook rook = (ChessPieceRook) getAtPosition(oldRookpos);
        putAtPosition(rookpos, rook);
    }

    /**
     * Move a piece to a position, if it can move there.
     *
     * @param piece The piece to move
     * @param toPosition The position to move to
     *
     * @return true if the piece moved, false otherwise
     */
    public boolean movePiece(IChessPiece piece, Position toPosition) {
        assert isOnBoard(toPosition);

        Move m = piece.getMoveTo(toPosition, this);
        if (m != null) {
            this.lastMove = m;
            m.DoMove(this);
            return true;
        }
        return false;
    }

    /**
     * Copy the board so that modifications can safely be made without changing
     * the original.
     *
     * @return A new board which does not contain references to any part of the
     * old one
     */
    public Board copy() {
        return new Board(copyPieces());
    }

    /**
     * Test if a Position is on this board.
     *
     * @return true if the Position is on the board, false otherwise
     */
    public boolean isOnBoard(Position pos) {
        return pos.getX() >= 1 && pos.getX() <= BOARD_WIDTH
            && pos.getY() >= 1 && pos.getY() <= BOARD_HEIGHT;
    }

    /**
     * Helper method: return a List of pieces with every piece copied.
     *
     * @return A copied list
     */
    private List<IChessPiece> copyPieces() {
        List<IChessPiece> ret = new ArrayList<>();
        for (IChessPiece p : pieces) {
            ret.add(p.copy());
        }
        return ret;
    }

    /**
     * Determine whether a given move puts a given king at risk.
     *
     * Used in order to determine illegal moves, seeing as moves which leave the
     * king threatened are not legal in chess.
     *
     * @param m The move to consider
     * @param kingColor The color of the king to check for
     *
     * @return true if the move is safe, false otherwise
     */
    public boolean moveDoesntPutKingInCheck(Move m, Color kingColor) {
        Optional<IChessPiece> a = getAllPieces().stream()
            .filter(p -> p.getColor() == kingColor && p instanceof ChessPieceKing)
            .findFirst();
        if (!a.isPresent()) return true; // no move can put king in check
        
        ChessPieceKing king = (ChessPieceKing)a.get();
        m.DoMove(this);
        boolean inCheck = king.isThreatened(this);
        m.UndoMove(this);
        return !inCheck;
    }
    
    /**
     * Get the last move performed in the game.
     *
     * @return The last Move that was performed
     */
    public Move getLastMove() {
        return this.lastMove;
    }

    /**
     * Determine whether the given player is in checkmate.
     *
     * @param playerColor The color of the player to check for
     *
     * @return true if the given player is in checkmate, false otherwise
     */
    public boolean checkMate(Color playerColor) {
        List<IChessPiece> allPieces = getAllPieces();
        if (allPieces.size() == 0) return false; // not possible
        ChessPieceKing king = allPieces.stream()
                .filter(p -> p instanceof ChessPieceKing && p.getColor() == playerColor)
                .map(p -> (ChessPieceKing)p).findFirst().orElse(null);
        return king == null || king.isThreatened(this) && allPieces.stream()
                .filter(p -> p.getColor() == playerColor)
                .mapToLong(p -> p.allPossibleMoves(this).size()).sum() == 0;
    }

    /**
     * Convenience method for displaying the board.
     * 
     * @return A String indicating the current positions of each of the pieces
     * on the board, arranged in a 2D grid
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int y = 8; y >= 1; y--) {
            for (int x = 1; x <= 8; x++) {
                IChessPiece p = this.getAtPosition(new Position(x, y));
                if (p != null) {
                    if (p instanceof ChessPieceKing) b.append("K");
                    if (p instanceof ChessPieceQueen) b.append("Q");
                    if (p instanceof ChessPieceRook) b.append("R");
                    if (p instanceof ChessPieceBishop) b.append("B");
                    if (p instanceof ChessPieceKnight) b.append("N");
                    if (p instanceof ChessPiecePawn) b.append("p");
                }
                else b.append('.');
            }
            b.append('\n');
        }
        return b.toString();
    }

    /**
     * Removes a piece from the board.
     *
     * In order to not invalidate all the positions in the lookup table, swap
     * the piece in the list with the last piece, before removing
     *
     * @param p The piece to remove from the board.
     */
    public void removePiece(IChessPiece p) {
        int index = lookupPositionIndex(p.getPosition());
        assert(index >= 0 && index < pieces.size());
        int lastPieceIndex = pieces.size()-1;
        if (index != lastPieceIndex) {
            IChessPiece lastPieceInList = pieces.get(lastPieceIndex);
            pieces.set(index, lastPieceInList);
            updatePositionIndex(lastPieceInList.getPosition(), index);
        }
        
        // p should now be at the end of the list
        pieces.remove(pieces.size()-1);
        updatePositionIndex(p.getPosition(), -1);
    }

    /**
     * Find the king of the specified color and caches the result.
     *
     * @param color The color of king to look for.
     * @return The king of the specified color.
     */
    public IChessPiece getKing(Color color) {
        if (color.isWhite()) {
            if (whiteKing == null) {
                whiteKing = getAllPieces().stream()
                        .filter(p -> p.getColor().isWhite() && p instanceof ChessPieceKing)
                        .findFirst().orElse(null);
            }
            return whiteKing;
        }
        else {
            if (blackKing == null) {
                blackKing = getAllPieces().stream()
                        .filter(p -> p.getColor().isBlack() && p instanceof ChessPieceKing)
                        .findFirst().orElse(null);
            }
            return blackKing;
        }
    }
}
