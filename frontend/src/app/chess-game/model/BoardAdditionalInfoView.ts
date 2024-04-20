export interface BoardAdditionalInfoView {
    boardId: string,
    currentPlayerColor: string,
    whitePlayer: string,
    blackPlayer: string,
    gameState: 'CONTINUE' | 'CHECKMATE' | 'STALEMATE' | 'RESIGNED' | 'DRAW'
    victoriousPlayerColor?: string,
    capturedWhitePieces: string[],
    capturedBlackPieces: string[],
    performedMoves: string[]
}