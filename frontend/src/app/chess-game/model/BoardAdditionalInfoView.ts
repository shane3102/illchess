export interface BoardAdditionalInfoView {
    boardId: string,
    currentPlayerColor: string,
    whitePlayer?: PlayerView,
    blackPlayer?: PlayerView,
    gameState: 'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | 'RESIGNED' | 'DRAW'
    victoriousPlayerColor?: string,
    capturedWhitePieces: string[],
    capturedBlackPieces: string[],
    performedMoves: string[]
}

export interface PlayerView {
    username: string,
    isProposingDraw: boolean
} 