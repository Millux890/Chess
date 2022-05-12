package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {

    public WhitePlayer(Board board, //constructor of white piece
                       Collection<Move> whiteStandardLegalMoves,
                       Collection<Move> blackStandardLegalMoves) {

        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);

    }


    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentsLegals) { // calculate possibilities of doing castle

        final List<Move> kingCastles = new ArrayList<>(0); // list of moves
        //Castle right site

        if (this.playerKing.isFirstMove() && !this.isInCheck()) { // if its first move and not checked
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) { // if fields between king and rook are empty

                final Tile rookTile = this.board.getTile(63); // get rook coordinate

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) { // if rook is on place and hasn't moved yet
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && // checking if fields are under attack
                            (Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()) &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        kingCastles.add(new KingSideCastleMove(this.board,
                                this.playerKing,
                                62,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                61));

                    }
                }
            }
            // Castle left side
            if (!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {

                final Tile rookTile = this.board.getTile(63); // get rook coordinate

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) { // if rook is on place and hasn't moved yet
                    if (Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty() && // checking if fields are under attack
                            (Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()) &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                            kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                    this.playerKing,
                                    58,
                                    (Rook) rookTile.getPiece(),
                                    rookTile.getTileCoordinate(),
                                    59));
                        }
                    }
                }
            }
        }
        return List.copyOf(kingCastles);
    }


    @Override
    public Collection<Piece> getActivePieces() { // get pieces what didn't kill
        return this.board.getWhitePieces();
    }


    @Override
    public Alliance getAlliance() { // read your alliance
        return Alliance.WHITE;
    }


    @Override
    public Player getOpponent() { // get opponents
        return this.board.blackPlayer();
    }
}

