package com.example.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var state by mutableStateOf(GameState())

    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )
    var b1=0
    var b2=false
    var r1=0
    var s1=""
    var t1=state.currentTurn
    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.BoardTapped -> {
                addValueToBoard(action.cellNo)
                b1=1
                b2=false
                if (b1==1) {
                    for (i1 in 1..9) {
                        if (boardItems[i1] == BoardCellValue.NONE) {
                            boardItems[i1] = state.currentTurn
                            b2 = checkForVictory(state.currentTurn)
                            boardItems[i1] = BoardCellValue.NONE
                            if (b2) {
                                addValueToBoard(i1)
                                b1 = 0
                                break
                            }
                        }
                    }
                }
                if (b1==1) {
                    for (i1 in 1..9) {
                        if (boardItems[i1] == BoardCellValue.NONE) {
                            boardItems[i1] = f1(state.currentTurn)
                            b2 = checkForVictory(f1(state.currentTurn))
                            boardItems[i1] = BoardCellValue.NONE
                            if (b2) {
                                addValueToBoard(i1)
                                b1 = 0
                                break
                            }
                        }
                    }
                }
                if (b1==1 && boardItems[5] == BoardCellValue.NONE) {
                    addValueToBoard(5)
                    b1=0
                }
                if (b1==1) {
                    for (i1 in 1..9) {
                        if (boardItems[i1] == BoardCellValue.NONE) {
                            r1=(0..1).random()
                            if (r1==1) {
                                addValueToBoard(i1)
                                b1=0
                                break
                            }
                        }
                    }
                }
            }

            UserAction.PlayAgainButtonClicked -> {
                gameReset()
            }
        }
    }

    private fun f1(boardValue: BoardCellValue):BoardCellValue {
        if (boardValue == BoardCellValue.CIRCLE) {
            return BoardCellValue.CROSS
        }
        else if (boardValue == BoardCellValue.CROSS) {
            return BoardCellValue.CIRCLE
        }
        else {
            return BoardCellValue.NONE
        }
    }

    private fun gameReset() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        t1=f1(t1)
        if (t1 == BoardCellValue.CIRCLE) {
            s1="O"
        }
        else if (t1 == BoardCellValue.CROSS) {
            s1="X"
        }
        state = state.copy(
            hintText = "Player '"+s1+"' turn",
            currentTurn = t1,
            victoryType = VictoryType.NONE,
            hasWon = false
        )
    }

    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNo] = BoardCellValue.CIRCLE
            state = if (checkForVictory(BoardCellValue.CIRCLE)) {
                state.copy(
                    hintText = "Player 'O' Won",
                    playerCircleCount = state.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNo] = BoardCellValue.CROSS
            state = if (checkForVictory(BoardCellValue.CROSS)) {
                state.copy(
                    hintText = "Player 'X' Won",
                    playerCrossCount = state.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if (hasBoardFull()) {
                state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }

            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }

            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }

            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }

            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }

            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }

            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }

            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                state = state.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }

            else -> return false
        }
    }

    private fun hasBoardFull(): Boolean {
        return !boardItems.containsValue(BoardCellValue.NONE)
    }
}