package com.frequentlymisseddeadlines.benchmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.frequentlymisseddeadlines.benchmarks.bitboard.BitboardChessboard;
import com.frequentlymisseddeadlines.benchmarks.classic.ClassicChessboard;
import com.frequentlymisseddeadlines.benchmarks.classic.Color;
import com.frequentlymisseddeadlines.benchmarks.classic.Piece;
import com.frequentlymisseddeadlines.benchmarks.classic.PieceType;

@State(value = Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class BenchmarkPawnAttackingSquares {

    private ClassicChessboard classic;
    private BitboardChessboard bitboard;

    private int[] whitePawnSquares = new int[] {8, 9, 18, 27, 29, 36, 38, 39};
    private int[] blackPawnSquares = new int[] {16, 25, 34, 43, 45, 46, 47, 52};

    @Setup
    public void setup() {
        List<Piece> pieces = new ArrayList<>();
        for (int p : blackPawnSquares) {
            pieces.add(new Piece((byte) (p / 8), (byte) (p % 8), PieceType.PAWN, Color.BLACK));
        }
        for (int p : whitePawnSquares) {
            pieces.add(new Piece((byte) (p / 8), (byte) (p % 8), PieceType.PAWN, Color.WHITE));
        }
        this.classic = new ClassicChessboard(pieces);

        long blackPawns = 0L;
        for (int p : blackPawnSquares) {
            blackPawns |= (1L << p);
        }
        long whitePawns = 0L;
        for (int p : whitePawnSquares) {
            whitePawns |= (1L << p);
        }

        this.bitboard = new BitboardChessboard(
            0L, 
            0L,
            whitePawns,
            blackPawns,
            whitePawns,
            blackPawns
        );
    }

    @Benchmark
    public void testClassicPawnAttackingSquares() {
        classic.getPawnAttackingPiecesList(Color.WHITE);
    }

    @Benchmark
    public void testBitboardPawnAttackingSquares() {
        bitboard.getPawnAttackingSquares(Color.WHITE);
    }

    public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
            .include(BenchmarkPawnAttackingSquares.class.getSimpleName())
            .build();

    new Runner(opt).run();
}
}
