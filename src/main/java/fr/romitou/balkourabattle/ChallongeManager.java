package fr.romitou.balkourabattle;

import at.stefangeyer.challonge.Challonge;
import at.stefangeyer.challonge.model.Credentials;
import at.stefangeyer.challonge.model.Tournament;
import at.stefangeyer.challonge.rest.retrofit.RetrofitRestClient;
import at.stefangeyer.challonge.serializer.gson.GsonSerializer;
import org.bukkit.configuration.file.FileConfiguration;

public class ChallongeManager {

    private static final FileConfiguration configFile = BalkouraBattle.getConfigFile();

    private static final Challonge challonge = new Challonge(
            new Credentials(
                    configFile.getString("challonge.username"),
                    configFile.getString("challonge.password")
            ),
            new GsonSerializer(),
            new RetrofitRestClient()
    );

    private static Tournament tournament;

    public static Tournament getTournament() {
        assert tournament != null;
        return tournament;
    }

    public static void setTournament(Tournament tournament) {
        ChallongeManager.tournament = tournament;
    }

    public static Challonge getChallonge() {
        return challonge;
    }

}
