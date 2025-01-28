package com.kelseyde.calvin.board;

import java.util.function.Consumer;

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

	public void print(long bits) {
		// TODO Auto-generated method stub
		// print(board, this::getPieceCell);
 	}

	@FunctionalInterface
	private interface CellBuilder<T> {
		String get(T board, int square);
	}

	private String getPieceCell(Board board, int square) {
		Piece piece = board.pieceAt(square);
		if (piece == null) {
			return " ";
		}
		boolean white = (board.getWhitePieces() & Bits.of(square)) != 0;
		return white ? piece.code().toUpperCase() : piece.code();
	}

	private <T>void print(T board, CellBuilder<T> cellBuilder) {
		for (int rank = 7; rank >= 0; --rank) {
			if (withBorders) {
				out.accept(BORDERS);
			}
			for (int file = 0; file < 8; ++file) {
				int sq = Square.of(rank, file);
				out.accept(" | " + cellBuilder.get(board, sq));
			}
			out.accept(withCoordinates ? " | " + (rank + 1) : "|");
		}
		if (withBorders) {
			out.accept(BORDERS);
		}
		if (withCoordinates) {
			out.accept("   a   b   c   d   e   f   g   h");
		}
	}
}
