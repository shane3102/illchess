export interface BoardAdditionalInfoView {
    boardId: string,
    currentPlayerColor: string,
    whitePlayer: string,
    blackPlayer: string,
    gameState: string,
    victoriousPlayerColor: string,
    capturedWhitePieces: string[],
    capturedBlackPieces: string[],
    performedMoves: string[]
}