package com.kelseyde.calvin.board;

import java.util.function.Consumer;

import com.kelseyde.calvin.utils.notation.FEN;

public class BoardPrinter {
	private static final String BORDERS = " +---+---+---+---+---+---+---+---+";

	private boolean withBorders = true;
	private boolean withCoordinates = true;
	private final Consumer<String> out;

	public BoardPrinter(Consumer<String> out) {
		this.out = out;
	}
	
	public BoardPrinter withBorders(boolean b) {
		this.withBorders = b;
		return this;
	}
	public BoardPrinter withCoordinates(boolean b) {
		this.withCoordinates = b;
		return this;
	}

	public void print(Board board) {
		print(board, this::getPieceCell);
 	}

	public void print(long bitBoard) {
		print(bitBoard, this::getBitCell);
 	}

	@FunctionalInterface
	private interface CellBuilder<T> {
		String get(T board, int square);
	}

	private String getPieceCell(Board board, int square) {
		final Piece piece = board.pieceAt(square);
		if (piece == null) {
			return " ";
		}
		final boolean white = (board.getWhitePieces() & Bits.of(square)) != 0;
		return white ? piece.code().toUpperCase() : piece.code();
	}
	
	private String getBitCell(Long bitBoard, int sq) {
        boolean piece = (bitBoard & (Bits.of(sq))) != 0;
        return piece ? "1" : " ";
    }

	private <T>void print(T board, CellBuilder<T> cellBuilder) {
		for (int rank = 7; rank >= 0; --rank) {
			if (withBorders) {
				out.accept(BORDERS);
			}
			final StringBuilder builder = new StringBuilder();
			for (int file = 0; file < 8; ++file) {
				builder.append(" | ");
				builder.append(cellBuilder.get(board, Square.of(rank, file)));
			}
			builder.append(" |");
			if (withCoordinates) {
				builder.append(' ');
				builder.append(rank + 1);
			}
			out.accept(builder.toString());
		}
		if (withBorders) {
			out.accept(BORDERS);
		}
		if (withCoordinates) {
			out.accept("   a   b   c   d   e   f   g   h");
		}
	}
	
	public static void main(String[] toto) {
		final BoardPrinter p = new BoardPrinter(System.out::println);
		p.print(Square.WHITE);
		Bits.print(Square.WHITE);
		
		final Board board = Board.from(FEN.STARTPOS);
		p.print(board);
		board.print();
	}
}
