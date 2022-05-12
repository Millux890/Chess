package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    protected final Board board; // declaring local board, king and local moves
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    public boolean isInCheckMate;

    Player(final Board board,
           final Collection<Move> playerLegals, // constructor to connect things
           final Collection<Move> opponentLegals){
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
        playerLegals.addAll(calculateKingCastles(playerLegals, opponentLegals)); // TODO HERE
        this.legalMoves = Collections.unmodifiableCollection(playerLegals);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    public boolean isInCheckmate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStealMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    protected abstract Collection <Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);



    private Piece playerKing() { //connection to king
        return this.playerKing;
    }


    public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves){ // find attack moves from moves
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
            return ImmutableList.copyOf(attackMoves);
        }
        return moves;
    }

    private King establishKing() { // established king
        for(final Piece piece: getActivePieces()){ //find him from my pieces
            if(piece.getPieceType().isKing()){
                return (King) piece; // and return
            }
        }
        throw new RuntimeException("Should not reach here! Not a valid Board");
    }

    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            } 
        }
        return false;
    }

    public MoveTransition makeMove(final Move move){

        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());


        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move , MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    private King getPlayerKing() {
        return this.playerKing;
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }


    public boolean isInCheckMate() {
        return false;
    }

    public boolean isKingSideCastleCapable() {
        return true;
    }

    public boolean isQueenSideCastleCapable() {
        return true;
    }



    public boolean isInStaleMate() {
        return false;
    }

}


