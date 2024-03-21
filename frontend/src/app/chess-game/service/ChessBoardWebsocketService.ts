import { Injectable } from '@angular/core';
import { webSocket } from 'rxjs/webSocket';
import { RxStomp } from "@stomp/rx-stomp";
import { Subscription } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class ChessBoardWebsocketService {
    async subscribe(topic: string, callback: any): Promise<Subscription> {
        const rxStomp = new RxStomp();
        rxStomp.configure({
            brokerURL: 'ws://192.168.1.49:8080/chess',
        });

        rxStomp.activate();

        return rxStomp
            .watch(topic)
            .subscribe((message) => {
                callback(message.body)
            });
    }
}