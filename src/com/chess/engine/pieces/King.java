package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9}; // creating possibilities of movement

    public King(final Alliance pieceAlliance,final int piecePosition) { // constructor
        super(PieceType.KING,piecePosition, pieceAlliance,true);
    }
    public King(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) { // constructor
        super(PieceType.KING,piecePosition, pieceAlliance,isFirstMove);
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>(); // list of legal moves

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){ // from 0 to candidate moves
            final int candidateDestinationCandidate = this.piecePosition + currentCandidateOffset; // make candidate fields

            if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset) || //checking if there is no exclusion
                    isEightColumnExclusion(this.piecePosition,currentCandidateOffset)){
                continue;
            }

            if(BoardUtils.isValidTitleCoordinate(candidateDestinationCandidate)){ // checking if you will be on board
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCandidate);
                if(!candidateDestinationTile.isTileOccupied()){ // if isn't occupied add move
                    legalMoves.add(new Move.MajorMover(board,this,candidateDestinationCandidate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece(); // check what is on that tile
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); // download your alliance pieces
                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCandidate,pieceAtDestination)); // if there is no alliance, make it legal
                    }
                }

            }


        }

        return ImmutableList.copyOf(legalMoves); // return legal moves
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}
