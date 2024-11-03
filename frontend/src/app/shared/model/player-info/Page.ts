export interface Page<T> {
    content: T[],
    pageNumber: number,
    pageSize: number,
    totalPages: number
}