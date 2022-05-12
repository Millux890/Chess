package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {
    private final List<Tile> gameBoard; // implementing board
    private final Collection<Piece> whitePieces; // implementing pieces
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;


    public Board(Builder builder) { // constructor
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            final String tileText =this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+ 1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private static String prettyPrint(final Tile tile){ // print board in ASCI text
        return tile.toString();
    }


    public Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){

         List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece : pieces){

            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    public Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
         List<Piece> activePieces = new ArrayList<>(); // making array for active pieces

        for (final Tile tile : gameBoard) { //from 0 to number of active tiles
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece(); // place particular piece here
                if (piece.getPieceAlliance() == alliance) { // if this piece is alliance piece add it
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces); // return active pieces
    }

    private static List<Tile> createGameBoard(final Builder builder) { // creating gameboard
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES]; // creating array of tiles

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i)); // counting to 64 and making board
        }
        return ImmutableList.copyOf(tiles); // return created board
    }

    public static Board createStandardBoard() {
             final Builder builder = new Builder();
            // Black Layout
            builder.setPiece(new Rook(Alliance.BLACK, 0));
            builder.setPiece(new Knight(Alliance.BLACK, 1));
            builder.setPiece(new Bishop(Alliance.BLACK, 2));
            builder.setPiece(new Queen(Alliance.BLACK, 3));
            builder.setPiece(new King(Alliance.BLACK, 4));
            builder.setPiece(new Bishop(Alliance.BLACK, 5));
            builder.setPiece(new Knight(Alliance.BLACK, 6));
            builder.setPiece(new Rook(Alliance.BLACK, 7));
            builder.setPiece(new Pawn(Alliance.BLACK, 8));
            builder.setPiece(new Pawn(Alliance.BLACK, 9));
            builder.setPiece(new Pawn(Alliance.BLACK, 10));
            builder.setPiece(new Pawn(Alliance.BLACK, 11));
            builder.setPiece(new Pawn(Alliance.BLACK, 12));
            builder.setPiece(new Pawn(Alliance.BLACK, 13));
            builder.setPiece(new Pawn(Alliance.BLACK, 14));
            builder.setPiece(new Pawn(Alliance.BLACK, 15));
            // White Layout
            builder.setPiece(new Pawn(Alliance.WHITE, 48));
            builder.setPiece(new Pawn(Alliance.WHITE, 49));
            builder.setPiece(new Pawn(Alliance.WHITE, 50));
            builder.setPiece(new Pawn(Alliance.WHITE, 51));
            builder.setPiece(new Pawn(Alliance.WHITE, 52));
            builder.setPiece(new Pawn(Alliance.WHITE, 53));
            builder.setPiece(new Pawn(Alliance.WHITE, 54));
            builder.setPiece(new Pawn(Alliance.WHITE, 55));
            builder.setPiece(new Rook(Alliance.WHITE, 56));
            builder.setPiece(new Knight(Alliance.WHITE, 57));
            builder.setPiece(new Bishop(Alliance.WHITE, 58));
            builder.setPiece(new Queen(Alliance.WHITE, 59));
            builder.setPiece(new King(Alliance.WHITE, 60));
            builder.setPiece(new Bishop(Alliance.WHITE, 61));
            builder.setPiece(new Knight(Alliance.WHITE, 62));
            builder.setPiece(new Rook(Alliance.WHITE, 63));
            //white to move
            builder.setMoveMaker(Alliance.WHITE);
            //build the board
            return builder.build(); //place pieces on board and build it

        }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves());
    }

    public Iterable<Piece> getAllPieces() {
        return blackPieces;
    }

    public long getPiece(int i) {
        return i;
    }


    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        private Pawn enPassantPawn; // CAREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Board build() {
            return new Board(this);
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}

