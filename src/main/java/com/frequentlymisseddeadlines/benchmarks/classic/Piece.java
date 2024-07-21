package com.frequentlymisseddeadlines.benchmarks.classic;


public class Piece {
    public final byte row, file;
    public final PieceType type;
    public final Color color;

    public Piece(byte row, byte file, PieceType type, Color color) {
        this.row = row;
        this.file = file;
        this.type = type;
        this.color = color;
    }
}
