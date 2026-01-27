[![Maven Central](https://img.shields.io/maven-central/v/com.fathzer/calvin-move-generator)](https://central.sonatype.com/artifact/com.fathzer/calvin-move-generator)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fathzer-games_calvin-move-generator&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fathzer-games_calvin-move-generator)
[![javadoc](https://javadoc.io/badge2/com.fathzer/calvin-move-generator/javadoc.svg)](https://javadoc.io/doc/com.fathzer/calvin-move-generator)

# calvin-move-generator

A port of [Calvin chess engine](https://github.com/kelseyde/calvin-chess-engine)'s move generator and some chess related utilities ([FEN](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation), [SAN](https://en.wikipedia.org/wiki/Algebraic_notation_(chess)) and [PGN](https://en.wikipedia.org/wiki/Portable_Game_Notation) notations, [Perft](https://www.chessprogramming.org/Perft) test).

It supports standard chess and [chess960 variant](https://en.wikipedia.org/wiki/Chess960).

## Requirements

- Java 17+

Add the following dependency to your project:
```xml
<dependency>
    <groupId>com.fathzer</groupId>
    <artifactId>calvin-move-generator</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage example

The following code prints all moves from a position represented in FEN notation
```java
// Here is a fen representation of a chess position
String fen = "b3r1k1/5pp1/1p5p/1qb1p3/2N2n2/3P4/3B1PPP/R4QK1 b - - 5 28";
// Create the board from the fen
Board board = FEN.toBoard(fen);
// Gets the legal moves
List<Move> moves = new MoveGenerator().generateMoves(board);
moves.forEach(move -> {
  // Print move in SAN notation
  System.out.println(SAN.fromMove(move, board));
});
```

## Note

The original project of extracting the move generator of the [Calvin chess engine](https://github.com/kelseyde/calvin-chess-engine) is [here](https://github.com/kelseyde/calvin-chess).  
It was archived on January 27, 2026.
