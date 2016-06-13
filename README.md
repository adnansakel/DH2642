# Magum Opus: The Mobile Art Auction Game

Magnum Opus is a multiplayer Android game in which players compete in high-stakes art auctions. The project was completed for a class at KTH: DH2642 Interaction Programming and the Dynamic Web. The focus of the project was to use good programming architecture with the MVC pattern, but we also wanted to make something fun and unique.

## Inspiration

Our game is inspired by the 1970 board game "Masterpiece" (pictured below). Our implementation uses somewhat similar game mechanics, but adds new multiplayer connectivity and has a new layout that's suited for mobile interaction.

![Masterpiece](/Documentation/masterpiece.jpg?raw=true "Masterpiece board game, 1970")

## Architecture

Firebase:  
We are using Firebase as our database for game events and logic synchronization. The communication with Firebase happens both ways. The application makes changes in the Firebase database and listens to it in order to synchronize gameplay and apply game logic.

Cloudinary:  
Images required for the game are downloaded from Cloudinary using REST API.

![Architecure](/Documentation/architecture.jpg?raw=true)

The Controller classes communicate with Firebase using Firebase APIs and with Cloudinary using REST APIs. They are also responsible for making changes in the Model. The Controller classes also fetch data from the Model for applying the game logic. The Model notifies the View classes about changes in it made by Controllers. The View fetches data from Model to make necessary changes to the User Interface. 

## Team

Daniel Delgado  
Daniel Meusberger  
Geoffrey Cooper  
Jahidul Adnan Sakel  