export interface GameSnippetView {
    id: string,
    whiteUsername: string,
    whiteUserPointChange: number,
    blackUsername: string,
    blackUserPointChange: number,
    gameResult: string,
    endTime: Date
}