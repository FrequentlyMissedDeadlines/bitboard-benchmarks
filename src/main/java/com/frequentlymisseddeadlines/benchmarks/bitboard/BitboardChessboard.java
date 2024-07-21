package com.frequentlymisseddeadlines.benchmarks.bitboard;

import java.util.ArrayList;
import java.util.List;

import com.frequentlymisseddeadlines.benchmarks.Move;
import com.frequentlymisseddeadlines.benchmarks.classic.Color;

public class BitboardChessboard {
    private long whiteRooks;
    private long blackRooks;
    private long whitePawns;
    private long blackPawns;
    private long whitePieces;
    private long blackPieces;

    public BitboardChessboard(long whiteRooks, long blackRooks, long whitePawns, long blackPawns, long whitePieces, long blackPieces) {
        this.whiteRooks = whiteRooks;
        this.blackRooks = blackRooks;
        this.whitePawns = whitePawns;
        this.blackPawns = blackPawns;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
    }

    //TODO this is not taking other blocking pieces in consideration
    public List<Move> getRooksLegalMoves(Color player) {
        List<Move> moves = new ArrayList<>(28);
        long rooksIter = player == Color.WHITE ? whiteRooks : blackRooks;
        int square = 0;
        long squareBitboard = 1L;
        while (rooksIter != 0) {
            if ((rooksIter & 1L) != 0L) {
                byte fromRow = (byte) (square / 8);
                byte fromFile = (byte) (square % 8);
                long possibleSquares = ((0xffL << (fromRow * 8)) & ~squareBitboard) | ((0x0101010101010101L << (fromFile)) & ~squareBitboard);
                int targetSquare = 0;
                while (possibleSquares != 0) {
                    if ((possibleSquares & 1L) != 0L) {
                        moves.add(new Move(square, targetSquare));
                        possibleSquares >>>= 1;
                        targetSquare++;
                    } else {
                        int trailingZeros = Long.numberOfTrailingZeros(possibleSquares);
                        possibleSquares >>>= trailingZeros;
                        targetSquare += trailingZeros;
                    }
                }
                rooksIter >>>= 1;
                square++;
                squareBitboard <<= 1;
            } else {
                int trailingZeros = Long.numberOfTrailingZeros(rooksIter);
                rooksIter >>>= trailingZeros;
                square += trailingZeros;
                squareBitboard <<= trailingZeros;
            }
        }
        return moves;
    }

    public List<Move> getPawnCaptures(Color player) {
        List<Move> moves = new ArrayList<>();
        long pawnsIter = player == Color.WHITE ? whitePawns : blackPawns;
        long otherPieces = player == Color.WHITE ? blackPieces : whitePieces;
        int square = 0;
        long squareBitboard = 1L;
        while (pawnsIter != 0) {
            if ((pawnsIter & 1L) == 0) {
                int trailingZeros = Long.numberOfTrailingZeros(pawnsIter);
                pawnsIter >>>= trailingZeros;
                square += trailingZeros;
                squareBitboard <<= trailingZeros;
            } else {
                long possibleSquares = ((squareBitboard << (player == Color.WHITE ? 7 : -7)) | (squareBitboard << (player == Color.WHITE ? 9 : -9))) & otherPieces;
                int targetSquare = 0;
                while (possibleSquares != 0) {
                    if ((possibleSquares & 1L) != 0L) {
                        moves.add(new Move(square, targetSquare));
                        possibleSquares >>>= 1;
                        targetSquare++;
                    } else {
                        int trailingZeros = Long.numberOfTrailingZeros(possibleSquares);
                        possibleSquares >>>= trailingZeros;
                        targetSquare += trailingZeros;
                    }
                }
                pawnsIter >>>= 1;
                square++;
                squareBitboard <<= 1;
            }
        }
        return moves;
    }

    private static final long A_FILE = 0x8080808080808080L;
    private static final long H_FILE = 0x0101010101010101L;

    public long getPawnAttackingSquares(Color player) {
        if (player == Color.WHITE) {
            return (((whitePawns & ~A_FILE) << 7) | ((whitePawns & ~H_FILE) << 9)) & blackPieces;
        } else {
            return (((blackPawns & ~H_FILE) >> 7) | ((blackPawns & ~A_FILE) >> 9)) & whitePieces;
        }
    }
}
