export interface BoardAdditionalInfoView {
    boardId: string,
    currentPlayerColor: string,
    whitePlayer?: PlayerView,
    blackPlayer?: PlayerView,
    gameState: 'CONTINUE' | 'WHITE_WON' | 'BLACK_WON' | 'DRAW'
    gameResultCause?: 'CHECKMATE' | 'RESIGNATION' | 'STALEMATE' | 'INSUFFICIENT_MATERIAL' | 'PLAYER_AGREEMENT',
    capturedWhitePieces: string[],
    capturedBlackPieces: string[],
    performedMoves: string[]
}

export interface PlayerView {
    username: string,
    isProposingDraw: boolean,
    isProposingTakingBackMove: boolean
} 