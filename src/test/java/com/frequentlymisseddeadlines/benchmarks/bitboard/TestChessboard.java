package com.frequentlymisseddeadlines.benchmarks.bitboard;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.frequentlymisseddeadlines.benchmarks.Move;
import com.frequentlymisseddeadlines.benchmarks.classic.ClassicChessboard;
import com.frequentlymisseddeadlines.benchmarks.classic.Color;
import com.frequentlymisseddeadlines.benchmarks.classic.Piece;
import com.frequentlymisseddeadlines.benchmarks.classic.PieceType;

public class TestChessboard {

    @Test
    public void testBitboardRookMoves() {
        List<Move> moves = new BitboardChessboard(
            (1L << 27) | (1L << 46), 
            0L,
            0L,
            0L,
            (1L << 27) | (1L << 46),
            0L
        ).getRooksLegalMoves(Color.WHITE);
        
        assertEquals(28, moves.size());
    }

    @Test
    public void testClassicRookMoves() {
        List<Move> moves = new ClassicChessboard(Arrays.asList(new Piece((byte) 3, (byte) 3, PieceType.ROOK, Color.WHITE),
            new Piece((byte) 5, (byte) 6, PieceType.ROOK, Color.WHITE))).getRooksLegalMoves(Color.WHITE);

        assertEquals(28, moves.size());
    }

    private int[] whitePawnSquares = new int[] {8, 9, 18, 27, 29, 36, 38, 39};
    private int[] blackPawnSquares = new int[] {16, 25, 34, 43, 45, 46, 47, 52};

    @Test
    public void testBitboardPawnCaptures() {
        long blackPawns = 0L;
        for (int p : blackPawnSquares) {
            blackPawns |= (1L << p);
        }
        long whitePawns = 0L;
        for (int p : whitePawnSquares) {
            whitePawns |= (1L << p);
        }
        List<Move> moves = new BitboardChessboard(
            0L, 
            0L,
            whitePawns,
            blackPawns,
            whitePawns,
            blackPawns
        ).getPawnCaptures(Color.WHITE);
        
        assertEquals(8, moves.size());
    }

    @Test
    public void testClassicPawnCaptures() {
        List<Piece> pieces = new ArrayList<>();
        for (int p : blackPawnSquares) {
            pieces.add(new Piece((byte) (p / 8), (byte) (p % 8), PieceType.PAWN, Color.BLACK));
        }
        for (int p : whitePawnSquares) {
            pieces.add(new Piece((byte) (p / 8), (byte) (p % 8), PieceType.PAWN, Color.WHITE));
        } 
        List<Move> moves = new ClassicChessboard(pieces).getPawnCaptures(Color.WHITE);

        assertEquals(8, moves.size());
    }
}
