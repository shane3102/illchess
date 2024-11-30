docker tag illchess-gateway shane3102/illchess-gateway
docker tag illchess-game-service shane3102/illchess-game-service
docker tag illchess-player-info-service shane3102/illchess-player-info-service
docker tag illchess-stockfish-service shane3102/illchess-stockfish-service
docker tag illchess-frontend shane3102/illchess-frontend
docker login
docker push shane3102/illchess-gateway
docker push shane3102/illchess-game-service
docker push shane3102/illchess-player-info-service
docker push shane3102/illchess-stockfish-service
docker push shane3102/illchess-frontend