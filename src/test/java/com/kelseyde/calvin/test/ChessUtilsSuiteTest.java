package com.kelseyde.calvin.test;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.fathzer.chess.utils.test.Chess960Test;
import com.fathzer.chess.utils.test.SANTest;

@Suite
@SelectClasses({Chess960Test.class, SANTest.class, CustomizedPGNTest.class})
public class ChessUtilsSuiteTest {}