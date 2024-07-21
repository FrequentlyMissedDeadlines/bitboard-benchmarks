package com.frequentlymisseddeadlines.benchmarks;

import java.util.Arrays;
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
public class BenchmarkRooks {

    private ClassicChessboard classic;
    private BitboardChessboard bitboard;

    private int[] rookSquares = new int[] {27, 46};

    @Setup
    public void setup() {
        this.classic = new ClassicChessboard(Arrays.asList(
            new Piece((byte) (rookSquares[0] / 8), (byte) (rookSquares[0] % 8), PieceType.ROOK, Color.WHITE),
            new Piece((byte) (rookSquares[1] / 8), (byte) (rookSquares[1] % 8), PieceType.ROOK, Color.WHITE)
        ));

        this.bitboard = new BitboardChessboard(
            (1L << 27) | (1L << 46), 
            0L,
            0L,
            0L,
            (1L << 27) | (1L << 46),
            0L
        );
    }

    @Benchmark
    public void testClassicRookMoves() {
        classic.getRooksLegalMoves(Color.WHITE);
    }

    @Benchmark
    public void testBitboardRookMoves() {
        bitboard.getRooksLegalMoves(Color.WHITE);
    }

    public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
            .include(BenchmarkRooks.class.getSimpleName())
            .build();

    new Runner(opt).run();
}
}
