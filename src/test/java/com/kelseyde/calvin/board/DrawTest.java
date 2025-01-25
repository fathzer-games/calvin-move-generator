package com.kelseyde.calvin.board;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.kelseyde.calvin.movegen.MoveGenerator;
import com.kelseyde.calvin.utils.TestUtils;
import com.kelseyde.calvin.utils.notation.FEN;

class DrawTest {
	private MoveGenerator mg = new MoveGenerator();

	@Test
	void testRepetitions() {
		// No draw at all
        String fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String[] moves = "d2d4 d7d5 g1f3 g8f6 g2g3 c7c6 f1g2 h7h6 e1g1 g7g6".split(" ");
        Board board = build(fen, moves);
        assertFalse(Draw.isDoubleRepetition(board));
        assertFalse(Draw.isThreefoldRepetition(board));
        assertFalse(Draw.isInsufficientMaterial(board));
        assertFalse(Draw.isStalemate(board, mg));
        assertFalse(Draw.isEffectiveDraw(board));
        assertFalse(Draw.isDraw(board, mg));
        
        // Two repetitions
        fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        moves = "b1c3 b8c6 c3b1 c6b8".split(" ");
        board = build(fen, moves);
        assertTrue(Draw.isDoubleRepetition(board));
        assertFalse(Draw.isThreefoldRepetition(board));
        assertFalse(Draw.isInsufficientMaterial(board));
        assertFalse(Draw.isStalemate(board, mg));
        assertTrue(Draw.isEffectiveDraw(board));
        assertFalse(Draw.isDraw(board, mg));
        
        // Three repetitions
        fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        moves = "b1c3 b8c6 g1f3 g8f6 f3g1 f6g8 c3b1 c6b8 b1c3 b8c6 c3b1 c6b8".split(" ");
        board = build(fen, moves);
        assertTrue(Draw.isDoubleRepetition(board));
        assertTrue(Draw.isThreefoldRepetition(board));
        assertFalse(Draw.isInsufficientMaterial(board));
        assertFalse(Draw.isStalemate(board, mg));
        assertTrue(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));
	}
	
    @Test
    void testStaleMate() {
        // staleMate
        Board board = FEN.toBoard("7k/3Q4/8/8/2B5/8/8/4K3 b - - 0 1");
        assertFalse(Draw.isDoubleRepetition(board));
        assertFalse(Draw.isThreefoldRepetition(board));
        assertFalse(Draw.isInsufficientMaterial(board));
        assertTrue(Draw.isStalemate(board, mg));
        assertFalse(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));
        
        // Black seem stale mated ... but white to move.
        board = FEN.toBoard("7k/3Q4/8/8/2B5/8/8/4K3 w - - 0 1");
        assertFalse(Draw.isDoubleRepetition(board));
        assertFalse(Draw.isThreefoldRepetition(board));
        assertFalse(Draw.isInsufficientMaterial(board));
        assertFalse(Draw.isStalemate(board, mg));
        assertFalse(Draw.isEffectiveDraw(board));
        assertFalse(Draw.isDraw(board, mg));

        // Mate.
        board = FEN.toBoard("6Qk/8/8/8/2B5/8/8/4K3 b - - 0 1");
        assertFalse(Draw.isDoubleRepetition(board));
        assertFalse(Draw.isThreefoldRepetition(board));
        assertFalse(Draw.isInsufficientMaterial(board));
        assertFalse(Draw.isStalemate(board, mg));
        assertFalse(Draw.isEffectiveDraw(board));
        assertFalse(Draw.isDraw(board, mg));
    }
    
    @Test
    void testInsufficientMaterial() {
        // King vs white bishop
        Board board = FEN.toBoard("7k/8/8/8/2B5/8/8/4K3 b - - 0 1");
        assertTrue(Draw.isInsufficientMaterial(board));
        assertTrue(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));
 
        // King vs black knight
        board = FEN.toBoard("8/7k/8/8/8/6n1/8/3K4 b - - 0 1");
        assertTrue(Draw.isInsufficientMaterial(board));
        assertTrue(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));

        // Nothing but kings
        board = FEN.toBoard("8/7k/8/8/8/8/8/3K4 b - - 0 1");
        assertTrue(Draw.isInsufficientMaterial(board));
        assertTrue(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));
        
        // Bishop vs bishop of the same color -> draw
        board = FEN.toBoard("8/3K4/k7/8/3b4/8/3B4/8 b - - 0 1");
        assertTrue(Draw.isInsufficientMaterial(board));
        assertTrue(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));
        
        // Bishop vs bishop of different color -> not draw
//        board = FEN.toBoard("8/3K4/k7/8/3b4/8/4B3/8 b - - 0 1");
//        assertFalse(Draw.isInsufficientMaterial(board));
//        assertFalse(Draw.isEffectiveDraw(board));
//        assertFalse(Draw.isDraw(board, mg));

        // King vs pawn -> not draw
        board = FEN.toBoard("7k/8/8/8/2P5/8/8/4K3 b - - 0 1");
        assertFalse(Draw.isInsufficientMaterial(board));
        assertFalse(Draw.isEffectiveDraw(board));
        assertFalse(Draw.isDraw(board, mg));

        
        // Bishop vs knight -> not draw
//        board = FEN.toBoard("7k/1n6/8/8/2B5/8/8/4K3 b - - 0 1");
//        assertFalse(Draw.isInsufficientMaterial(board));
//        assertFalse(Draw.isEffectiveDraw(board));
//        assertFalse(Draw.isDraw(board, mg));

        // knight vs knight -> not draw
//        board = FEN.toBoard("8/8/8/5k2/2n5/7N/2K5/8 b - - 0 1");
//        assertFalse(Draw.isInsufficientMaterial(board));
//        assertFalse(Draw.isEffectiveDraw(board));
//        assertFalse(Draw.isDraw(board, mg));
    }

    @Test
    void fiftyMoveRuleTest() {
        Board board = FEN.toBoard("8/8/4k3/4p3/4KP2/8/8/8 b - - 99 148");
        assertFalse(Draw.isFiftyMoveRule(board));
        assertFalse(Draw.isEffectiveDraw(board));
        assertFalse(Draw.isDraw(board, mg));
        board = FEN.toBoard("8/8/3k4/4p3/4KP2/8/8/8 w - - 100 149");
        assertTrue(Draw.isFiftyMoveRule(board));
        assertTrue(Draw.isEffectiveDraw(board));
        assertTrue(Draw.isDraw(board, mg));
    }

    @Test
    void perfTest() {
        String fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String[] moves = "d2d4 d7d5 g1f3 g8f6 g2g3 c7c6 f1g2 h7h6 e1g1 g7g6".split(" ");
        doTest(fen, moves, false, Draw::isDoubleRepetition);
        fen ="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        moves = "d2d4 g8f6 g1f3 g7g6 b1d2 d7d5 c2c4 c7c6 g2g3 f8g7 f1g2 e8g8 e1g1 c8f5 b2b3 b8d7 c1b2 d8b6 h2h3 h7h6 f3h4 f5e6 d1c2 a8d8 a1d1 f8e8 e2e3 a7a6 f1e1 a6a5 e3e4 d5e4 d2e4 f6e4 e1e4 g6g5 d4d5 d7c5 d5e6 g7b2 d1d8 e8d8 e6f7 g8f7 e4g4 c5d3 h4f5 e7e6 f5h6 f7g7 g2e4 g7h6 e4d3 b2d4 d3h7 h6g7 g4g5 g7f6 g5g6 f6e7 c2e2 d8d6 e2f3 b6b4 h7g8 b4e1 g1g2 e7e8 f3f7 e8d8 f7f4 d4e5 f4g5 d8c7 g6e6 d6e6 g8e6 e1e4 g2h2 c7d6 e6f5 e4e1 g5d8 d6c5 d8e7 c5d4 h2g2 d4c3 h3h4 c3b2 h4h5 b2a2 h5h6 e1e2 h6h7 e2b2 f5c8 b7b6 c4c5 e5d4 e7f7 b6c5 b3b4 c5c4 b4a5 b2c2 a5a6 c2e4 g2h2 e4e5 a6a7 d4a7 f7a7 a2b2 a7b6 b2a2 b6a7 a2b2 a7b6 b2a2".split(" ");
        doTest(fen, moves, true, Draw::isDoubleRepetition);
   }

    protected void doTest(String fen, String[] moves, boolean hasRepetition, Predicate<Board> tested) {
        final Board board = build(fen, moves);
        assertEquals(hasRepetition, tested.test(board));
        
        for (int i=0;i<100000000;i++) {
            tested.test(board);
        }
    }

    private Board build(String fen, String[] moves) {
        final Board board = Board.from(fen);
        Arrays.stream(moves).forEach(m -> play(board, m));
        return board;
    }

    private void play(Board board, String uci) {
        Move mv = TestUtils.getLegalMove(board, Move.fromUCI(uci));
        assertTrue(mg.isLegal(board, mv));
        board.makeMove(mv);
    }


}
