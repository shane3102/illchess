import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class GenerateRandomNameService {
    randomNames: string[] = ["Valerie", "Vincent", "Ciri", "Geralt", "Judy", "Yennefer", "Case", "Roach", "Johnny", "Eren", "Levi", "Jinx", "Ekko"]

    generateRandomName(): string { 
        return this.randomNames[Math.floor(Math.random() * this.randomNames.length)] + Math.floor(100 * Math.random())
    }
}