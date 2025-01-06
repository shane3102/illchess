import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class GenerateRandomNameService {
    randomNames: string[] = ["Valerie", "Vincent", "James", "Michael", "Patricia", "Robert", "Linda", "Johnny", "David", "Susan", "Sarah"]

    generateRandomName(): string { 
        return this.randomNames[Math.floor(Math.random() * this.randomNames.length)] + Math.floor(100 * Math.random())
    }
}