package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile { // describing whole board

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES  = createAllPossibleEmptyTiles(); //making 64 fields (AD1) all empty tiles



    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() { //AD1
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();//adding map as a hashmap

        for(int i = 0; i < BoardUtils.NUM_TILES ; i++){
            emptyTileMap.put(i, new EmptyTile(i)); // putting new Tile on (i) place
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }



    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return (piece!=null ? new OccupiedTile(tileCoordinate,piece) : EMPTY_TILES.get(tileCoordinate));
    } // checking if is there a piece, if yes tile is occupied and get coordinates if no tile not occupied get coordinates


    private Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate; //returning tile coordinate to function when calling Tile
    }



    //Telling what to do if tale is occupied or isn't


    public abstract boolean isTileOccupied(); // is occupied or no
    public abstract Piece getPiece(); // function getPiece of type Piece

    public int getTileCoordinate(){
        return this.tileCoordinate;
    }


    public static final class EmptyTile extends Tile{ // creating class Empty Tile and describing what it contains
        EmptyTile(int coordinate) {
            super(coordinate);
        }
        @Override
        public boolean isTileOccupied() {
            return false;
        }
        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }


    public static final class OccupiedTile extends Tile{ // creating class Occupied Tile and describing what it contains

        Piece pieceOnTile;

        public OccupiedTile(int coordinate, Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() { // overriding methods that every tile has
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase():
                    getPiece().toString();
        }
    }
}
