import { Injectable } from '@angular/core';
import { RxStomp } from "@stomp/rx-stomp";
import { Subscription } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class ChessBoardWebsocketService {
    async subscribe(topic: string, callback: any): Promise<Subscription> {
        const rxStomp = new RxStomp();
        rxStomp.configure({
            brokerURL: `/chess`,
        });

        rxStomp.activate();

        return rxStomp
            .watch(topic)
            .subscribe((message) => {
                callback(message.body)
            });
    }
}