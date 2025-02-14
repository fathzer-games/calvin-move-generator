package com.kelseyde.calvin.test;

import java.util.List;
import java.util.Optional;

import com.fathzer.chess.utils.model.IBoard;
import com.fathzer.chess.utils.model.TestAdapter;
import com.fathzer.chess.utils.model.Variant;
import com.fathzer.chess.utils.test.PGNTest;
import com.fathzer.chess.utils.test.SANTest;
import com.fathzer.chess.utils.test.helper.Supports;
import com.kelseyde.calvin.board.Bits;
import com.kelseyde.calvin.board.Board;
import com.kelseyde.calvin.board.ChessVariant;
import com.kelseyde.calvin.board.Move;
import com.kelseyde.calvin.board.Piece;
import com.kelseyde.calvin.board.Square;
import com.kelseyde.calvin.movegen.MoveGenerator;
import com.kelseyde.calvin.utils.notation.FEN;
import com.kelseyde.calvin.utils.notation.PGN;
import com.kelseyde.calvin.utils.notation.SAN;

public class ChessTestUtils {
	
	public static class CalvinBoard implements IBoard<Move> {
		private Board board;
		private MoveGenerator mg = new MoveGenerator();
		
		public CalvinBoard(Board board) {
			this.board = board;
			this.mg = new MoveGenerator();
		}
		
		@Override
		public List<Move> getMoves() {
			return mg.generateMoves(board);
		}

		@Override
		public boolean makeMove(Move mv) {
			return board.makeMove(mv);
		}

		@Override
		public void unmakeMove() {
			board.unmakeMove();
		}

		public Board getBoard() {
			return board;
		}

		@Override
		public int getPiece(String algebraicNotation) {
			final int square = Square.fromNotation(algebraicNotation);
			final Piece piece = board.pieceAt(square);
			if (piece==null) {
				return 0;
			} else {
				final int pieceKind = piece.ordinal()+1;
				final boolean white = (Bits.of(square) & board.getWhitePieces())!=0;
				return white ? pieceKind : -pieceKind;
			}
		}
	}
	
	@Supports(Variant.CHESS960)
	public static class CalvinUtilities implements TestAdapter<CalvinBoard, Move>,
			SANTest.SANConverter<CalvinBoard, Move>, PGNTest.PGNConverter<CalvinBoard> {
		public CalvinBoard fenToBoard(String fen, Variant variant) {
			final Board board = FEN.toBoard(fen, variant==Variant.CHESS960 ? ChessVariant.CHESS960 : ChessVariant.STANDARD);
			return new CalvinBoard(board);
		}

		@Override
		public Move move(CalvinBoard board, String uciMove) {
			final Move move = Move.fromUCI(uciMove);
	        final List<Move> legalMoves = board.getMoves();
	        final Optional<Move> legalMove = legalMoves.stream()
	                .filter(m -> m.matches(move))
	                .findAny();
	        return legalMove.orElse(move);
		}

		@Override
		public String getSAN(Move move, CalvinBoard board) {
			return SAN.fromMove(move, board.getBoard());
		}

		@Override
		public String toPGN(CalvinBoard board) {
			return PGN.toPGN(board.getBoard());
		}
	}
}
