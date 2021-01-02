
<p align="center"><img height=160 src="https://avatars3.githubusercontent.com/u/76783269?s=200&v=4"></p>  
<h2 align="center">⚔️ BalkouraBattle ⚔️</h2>  
<p align="center">Plugin created for an event in the Cité of Balkoura, making different players fight against each other via a Challonge online ranking.</p>  

<br />  

## 🚀 Download and installation
You can download BalkouraBattle by going to actions by [clicking here](https://github.com/Romitou/BalkouraBattle/actions?query=actor:Romitou%20is:success%20%20).  
Then, you can drag `BalkouraBattle-1.0.jar` into your `plugins` folder.

## 📖 Commands and usage

### /battle register
This command registers all players who are in spectator mode as participants in the event. Those who are already registered with Challonge will not be registered again. Players will receive a message to confirm their participation.

### /battle start
This command marks the tournament as started, and generates the tournaments. From this moment, the plugin will periodically retrieve the matches from Challonge who takes care of all the score management and the distribution of the players. You don't need to manage anything. These matches will then be proposed to the moderators so that they will accept the progress of the match, classified by order of arrival and priority. It is therefore preferable to accept the first matches.

### /battle players
This order returns the list of all participants registered with Challonge, whether they are logged out or not. To delete a participant, you will have to go to Challonge and delete the participant directly.

### /battle arenas
This command returns a list of all the arenas. Their statuses will be displayed: free, associated match awaiting validation, busy. The type of arena will also be displayed: classic or final arena.

### /battle info [<ID match\>]
If this command is executed without arguments, it returns all the matches of a player, regardless of whether they have already been played or are scheduled later. This list is final.

If a match number is specified, it returns all the information for a particular match: sets, status, players and player scores with the number of sets won. If a person wishes to have information about a future match, the information may not be available because Challonge has not yet completed the composition of the match. We will therefore have to wait until the matches allowing to define the players of this match have been completed.

### /battle reset
This command resets the tournament and its matches, excluding participants.
