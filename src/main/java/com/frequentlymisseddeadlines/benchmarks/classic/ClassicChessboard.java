package com.frequentlymisseddeadlines.benchmarks.classic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.frequentlymisseddeadlines.benchmarks.Move;

public class ClassicChessboard {
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final List<Piece> whiteRooks;
    private final List<Piece> blackRooks;
    private final List<Piece> whitePawns;
    private final List<Piece> blackPawns;
    private final Piece[][] pieces;

    public ClassicChessboard(List<Piece> pieces) {
        this.pieces = new Piece[8][8];
        for (Piece p: pieces) {
            this.pieces[p.file][p.row] = p;
        }
        this.whitePieces = pieces.stream().filter(p -> p.color == Color.WHITE).collect(Collectors.toList());
        this.blackPieces = pieces.stream().filter(p -> p.color == Color.BLACK).collect(Collectors.toList());
        this.whiteRooks = whitePieces.stream().filter(p -> p.type == PieceType.ROOK).collect(Collectors.toList());
        this.blackRooks = blackPieces.stream().filter(p -> p.type == PieceType.ROOK).collect(Collectors.toList());
        this.whitePawns = whitePieces.stream().filter(p -> p.type == PieceType.PAWN).collect(Collectors.toList());
        this.blackPawns = blackPieces.stream().filter(p -> p.type == PieceType.PAWN).collect(Collectors.toList());
    }

    private static final byte[] rookDirectionsRow = new byte[] {-1, 1, 0, 0};
    private static final byte[] rookDirectionsFile = new byte[] {0, 0, -1, 1};

    //TODO this is not taking other blocking pieces in consideration
    public List<Move> getRooksLegalMoves(Color player) {
        List<Piece> rooks = player == Color.WHITE ? whiteRooks : blackRooks;
        List<Move> moves = new ArrayList<>(14 * rooks.size()); //let's help the JVM as we know there will be maximum 14 moves per Rook
        for (Piece rook : rooks) {
            for (int direction = 0 ; direction < 4 ; direction++) {
                for (byte distance = 1 ; distance <= 7 ; distance++) {
                    byte newRow = (byte) (rook.row + distance * rookDirectionsRow[direction]);
                    byte newFile = (byte) (rook.file + distance * rookDirectionsFile[direction]);
                    
                    if (newRow >= 0 && newRow < 8 && newFile >= 0 && newFile < 8) {
                        moves.add(new Move(rook.row * 8 + rook.file, newRow * 8 + newFile));
                    } else {
                        break;
                    }
                }
            }
        }
        return moves;
    }

    public List<Move> getPawnCaptures(Color player) {
        List<Piece> pawns = player == Color.WHITE ? whitePawns : blackPawns;
        List<Piece> otherPieces = player == Color.WHITE ? blackPieces : whitePieces;
        int advanceOffset = player == Color.WHITE ? 1 : -1;
        List<Move> moves = new ArrayList<>();
        for (Piece pawn : pawns) {
            for (int dx : new int[] {-1, 1}) {
                if (otherPieces.stream().anyMatch(p -> p.file == pawn.file + dx && p.row == pawn.row + advanceOffset)) {
                    moves.add(new Move(pawn.row * 8 + pawn.file, (pawn.row + advanceOffset) * 8 + pawn.file + dx));
                }
            }
        }
        return moves;
    }

    public List<Integer> getPawnAttackingPiecesList(Color playerToMove) {
        List<Piece> pawns = playerToMove == Color.WHITE ? whitePawns : blackPawns;
        int advanceOffset = playerToMove == Color.WHITE ? 1 : -1;
        List<Integer> attacking = new ArrayList<>();
        for (Piece pawn : pawns) {
            for (int fileOffset : new int[] {-1, 1}) {
                int targetRow = pawn.row + advanceOffset;
                int targetFile = pawn.file + fileOffset;
                if (targetFile < 0 || targetFile > 7) {
                    continue;
                }
                Piece targetPiece = this.pieces[targetFile][targetRow];
                if (targetPiece != null && targetPiece.color != playerToMove) {
                    attacking.add(targetRow * 8 + targetFile);
                }
            }
        }
        return attacking;
    }

    public boolean[][] getPawnAttackingPiecesArray(Color playerToMove) {
        List<Piece> pawns = playerToMove == Color.WHITE ? whitePawns : blackPawns;
        int advanceOffset = playerToMove == Color.WHITE ? 1 : -1;
        boolean[][] attacking = new boolean[8][8];
        for (Piece pawn : pawns) {
            for (int fileOffset : new int[] {-1, 1}) {
                int targetRow = pawn.row + advanceOffset;
                int targetFile = pawn.file + fileOffset;
                if (targetFile < 0 || targetFile > 7) {
                    continue;
                }
                Piece targetPiece = this.pieces[targetFile][targetRow];
                if (targetPiece != null && targetPiece.color != playerToMove) {
                    attacking[targetFile][targetRow] = true;
                }
            }
        }
        return attacking;
    }
}
