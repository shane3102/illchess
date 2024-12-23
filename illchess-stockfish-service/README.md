# Illchess stockfish service

Service used to communicate with stockfish game engine by other application services.

Configuration:

* **working.mode**<br>
    Determines communication mode with stockfish engine. Available values:
    1. **ENGINE** - application is communicating with local engine
    2. **API** - application is communicating with engine available by api
* **stockfish.path**<br>
    Determines the path to stockfish engine present on machine. 
    If value is not provided application assumes that engine is present in path variables
* **urls.game-service**<br>
    Determines url of service providing info of current game state (**illchess-game-service**)
* **urls.stockfish-api**<br>
    Determines url of engine provided by api
    (used only when **working.mode** is set to **API**, default value is **https://stockfish.online/api/s/v2.php**)
    