# calvin-move-generator
A port of [Calvin chess engine](https://github.com/kelseyde/calvin-chess-engine)'s move generator

## WARNING: This project is just a discussion base. It can be deleted soon without any notice!

## Differences with the original project
- Removes dependencies to:
  - com.kelseyde.calvin.uci.UCI
  - com.kelseyde.calvin.search.Search
- Removes the Board.from(String) method to cur the dependency to FEN parser?
- Removes Board.print and partly replaces it with board.toString. //TODO
- In tests:
  - Removes the disabled ```com.kelseyde.calvin.movegen.testFens``` test and its dependency to SEARCHER.
  - Copy ```com.kelseyde.calvin.utils.Bench#FENS``` to com.kelseyde.calvin.movegen.PseudoLegalTest in order to break the dependency to Bench class.
  
## TODO
- Effectively remove the dependencies to ```com.kelseyde.calvin.uci.UCI``` and ```com.kelseyde.calvin.search.Search```.
- Enable ```com.fathzer.calvin.Chess960BoardTest#testTrickyLegalCastling``` test and fix the bug.