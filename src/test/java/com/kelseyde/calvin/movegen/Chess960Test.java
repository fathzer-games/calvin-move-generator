package com.kelseyde.calvin.movegen;

import com.kelseyde.calvin.board.*;
import com.kelseyde.calvin.utils.notation.FEN;

import org.junit.jupiter.api.*;

import java.util.List;

public class Chess960Test {

    private static final MoveGenerator MOVEGEN = new MoveGenerator();

    @Test
    public void testRookGoesToKingSquare() {

        Board board = FEN.toBoard("qnnbrk1r/ppppp1pp/5p2/3b4/3B4/5P2/PPPPP1PP/QNNBRK1R w KQkq - 2 3", ChessVariant.CHESS960);

        Move target = Move.fromUCI("f1h1", Move.CASTLE_FLAG);
        assertMove(board, target, true);
        board.makeMove(target);
        assertKingAndRook(board, "f1", "g1", "h1", "f1");

        board.unmakeMove();
        assertKingAndRook(board, "g1", "f1", "f1", "h1");

    }

    @Test
    public void testRookGoesToKingSquareBlocked() {

        Board board = FEN.toBoard("qnnbrkbr/pppppppp/8/8/8/8/PPPPPPPP/QNNBRKBR w KQkq - 0 1", ChessVariant.CHESS960);
        Move target = Move.fromUCI("f1h1", Move.CASTLE_FLAG);
        assertMove(board, target, false);

    }

    @Test
    public void testQueensideRookOnKingside() {

        Board board = FEN.toBoard("qn2rkbr/ppbppppp/1np5/8/8/1NP5/PPBPPPPP/QN2RKBR w KQkq - 2 4", ChessVariant.CHESS960);

        Move target = Move.fromUCI("f1e1", Move.CASTLE_FLAG);
        assertMove(board, target, true);
        board.makeMove(target);
        assertKingAndRook(board, "f1", "c1", "e1", "d1");
        board.unmakeMove();
        assertKingAndRook(board, "c1", "f1", "d1", "e1");

    }

    @Test
    public void testQueensideRookOnKingsideTwo() {

        Board board = FEN.toBoard("bbqnr1nQ/1ppppp1p/8/p7/5k2/PP2N3/2PPPP1P/1B2RKNR w KQ - 2 8", ChessVariant.CHESS960);

        Move target = Move.fromUCI("f1e1", Move.CASTLE_FLAG);
        assertMove(board, target, true);
        board.makeMove(target);
        assertKingAndRook(board, "f1", "c1", "e1", "d1");
        board.unmakeMove();
        assertKingAndRook(board, "c1", "f1", "d1", "e1");

    }

    @Test
    public void testQueensideRookOnKingsideCastleKingside() {
        Board board = FEN.toBoard("bnqbrk1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/BNQBRK1R w KQkq - 2 2", ChessVariant.CHESS960);

        Move target = Move.fromUCI("f1h1", Move.CASTLE_FLAG);
        assertMove(board, target, true);
        board.makeMove(target);
        assertKingAndRook(board, "f1", "g1", "h1", "f1");
        board.unmakeMove();
        assertKingAndRook(board, "g1", "f1", "f1", "h1");

    }

    @Test
    public void testQueensideRookOnKingsideBlocked() {

        Board board = FEN.toBoard("nbb1rkrn/pp1ppppp/1qp5/8/8/1QP5/PP1PPPPP/NBB1RKRN w KQkq - 2 3", ChessVariant.CHESS960);

        Move target = Move.fromUCI("f1e1", Move.CASTLE_FLAG);
        assertMove(board, target, false);

    }

    @Test
    public void testKingsideRookOnQueenside() {

        Board board = FEN.toBoard("nbbqr2n/ppp2kr1/3ppppp/8/8/3PPPN1/PPPBBQPP/NRKR4 w KQ - 2 8", ChessVariant.CHESS960);

        Move target = Move.fromUCI("c1d1", Move.CASTLE_FLAG);
        assertMove(board, target, true);
        board.makeMove(target);
        assertKingAndRook(board, "c1", "g1", "d1", "f1");
        board.unmakeMove();
        assertKingAndRook(board, "g1", "c1", "f1", "d1");
    }

    @Test
    public void testKingsideRookOnQueensideBlocked() {

        Board board = FEN.toBoard("nbbqr1rn/ppp2k2/3ppppp/8/8/3PPPN1/PPPBB1PP/NRKR2Q1 w KQ - 0 7", ChessVariant.CHESS960);

        Move target = Move.fromUCI("c1d1", Move.CASTLE_FLAG);
        assertMove(board, target, false);
    }

    @Test
    public void testDontGetConfusedBetweenKingsideQueenside() {

        Board board = FEN.toBoard("bqnbrk1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/BQNBRK1R w KQkq - 2 2", ChessVariant.CHESS960);
        Move kingside = Move.fromUCI("f1h1", Move.CASTLE_FLAG);
        Move queenside = Move.fromUCI("f1e1", Move.CASTLE_FLAG);
        assertMove(board, kingside, true);
        assertMove(board, queenside, false);

    }

	@Test
	void testKingDoesntMoveCastling() {
		// Rook is attacked, but the castling is legal
		final Board board = FEN.toBoard("nrk2rnb/pp1ppppp/6b1/q1p5/3P2Q1/1N3N2/1P2PPPP/1RK1BR1B w KQkq - 2 10", ChessVariant.CHESS960);
		final Move move = Move.fromUCI("c1b1", Move.CASTLE_FLAG);
		assertMove(board, move, true);
	}

	@Test
	void testPinnedRookCastling() {
		// Test castling where king seems safe ... but is not because it does not move and the rook does not defend it anymore
		final Board board = FEN.toBoard("nrk1brnb/pp1ppppp/8/2p5/3P4/1N1Q1N2/1PP1PPPP/qRK1BR1B w KQkq - 2 10", ChessVariant.CHESS960);
		final Move move = Move.fromUCI("c1b1", Move.CASTLE_FLAG);
		assertMove(board, move, false);
	}

    private void assertMove(Board board, Move move, boolean exists) {
        List<Move> moves = MOVEGEN.generateMoves(board);
        Assertions.assertEquals(exists, moves.stream().anyMatch(m -> m.equals(move)));
        Assertions.assertEquals(exists, MOVEGEN.isLegal(board, move));
    }

    private void assertKingAndRook(Board board, String kingFrom, String kingTo, String rookFrom, String rookTo) {

        int kingFromSq = Square.fromNotation(kingFrom);
        int kingToSq = Square.fromNotation(kingTo);
        int rookFromSq = Square.fromNotation(rookFrom);
        int rookToSq = Square.fromNotation(rookTo);

        if (kingFromSq != rookToSq) {
            Assertions.assertNull(board.pieceAt(kingFromSq));
        }
        Assertions.assertEquals(Piece.KING, board.pieceAt(kingToSq));

        if (rookFromSq != kingToSq) {
            Assertions.assertNull(board.pieceAt(rookFromSq));
        }
        Assertions.assertEquals(Piece.ROOK, board.pieceAt(rookToSq));

        Assertions.assertFalse(Bits.contains(board.getKing(true), kingFromSq));
        Assertions.assertFalse(Bits.contains(board.getRooks(true), rookFromSq));
        Assertions.assertTrue(Bits.contains(board.getKing(true), kingToSq));
        Assertions.assertTrue(Bits.contains(board.getRooks(true), rookToSq));
    }
}
