import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http"
import { Observable } from "rxjs";
import { BoardView } from "../model/BoardView";

@Injectable({
    providedIn: 'root'
})
export class BoardService {

    constructor(private http: HttpClient) {

    }

    public tmpInitializeBoard(): Observable<BoardView> {
        return this.http.get<BoardView>("/api/tmp/board/initialize")
    }

}