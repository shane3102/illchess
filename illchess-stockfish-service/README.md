# Illchess stockfish service

Serwis używany do komunikacji z silnikiem szachowym stockfish przez pozostałe serwisy aplikacji.

Konfiguracja:

* **working.mode**<br>
    Określa sposób kominikacji z silnikiem stockfish<br>
    Wartości:
    1. **ENGINE** - aplikacja komunikuje się z lokalnym silnikiem
    2. **API** - aplikacja komunikuje się z silnikiem udostępnionym przez api
* **stockfish.path**<br>
    Określa ścieżkę do silnika stockfish na maszynie. Jeżeli wartość nie jest określona aplikacja zakłada, że silnik znajduje sie w zmiennej środowiskowej.
* **urls.game-service**<br>
    Określa url serwisu udostępniającego informacje o stanie gry
* **urls.stockfish-api**<br>
    Określa url serwisu z silnikiem stockfish (używane wyłącznie w przypadku gdy **working.mode** jest ustawiony na **API**, domyślna wartość wynosi **https://stockfish.online/api/s/v2.php**)
    