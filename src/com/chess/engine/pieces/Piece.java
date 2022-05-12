package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    public Piece(final PieceType pieceType, int piecePosition, Alliance pieceAlliance,final boolean isFirstMove) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.pieceType = pieceType;
        this.cachedHashCode = computeHashCode();
    }
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    public int getPiecePosition(){
        return this.piecePosition;
    }
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) obj;
        return piecePosition == otherPiece.getPiecePosition() &&
                pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() &&
                isFirstMove == otherPiece.isFirstMove();
    }


    public int computeHashCode() { // give adres in memory as integer
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }
    public abstract Piece movePiece(Move move);

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType{

        PAWN("P",100){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N",300){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B",300)
                {
                    @Override
                    public boolean isKing() {
                        return false;
                    }
                    @Override
                    public boolean isRook() {
                        return false;
                    }
                },
        ROOK("R",500)
                {
                    @Override
                    public boolean isKing() {
                        return false;
                    }
                    @Override
                    public boolean isRook() {
                        return true;
                    }
                },
        QUEEN("Q",900)
                {
                    @Override
                    public boolean isKing() {
                        return false;
                    }
                    @Override
                    public boolean isRook() {
                        return false;
                    }
                },
        KING("K",10000){
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName,final int pieceValue){

            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();

        public int getPieceValue(){
            return this.pieceValue;
        }
    }


}
