export interface GameFinishedView {
    id: string,
    whiteUserGameInfo: UserGameInfoView,
    blackUserGameInfo: UserGameInfoView
    gameResult: 'WHITE_WON' | 'DRAW' | 'BLACK_WON'
    endTime: Date,
    performedMoves: PerformedMoveView[]
}

export interface UserGameInfoView {
    username: string,
    rankingPointsBeforeGame: number,
    rankingPointsAfterGame: number,
    rankingPointsChange: number
}

export interface PerformedMoveView {
    startSquare: string,
    endSquare: string,
    stringValue: string,
    color: string
}