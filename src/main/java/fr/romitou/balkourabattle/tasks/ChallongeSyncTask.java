package fr.romitou.balkourabattle.tasks;

import fr.romitou.balkourabattle.utils.JsonRequest;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

public class ChallongeSyncTask extends BukkitRunnable {

    @Override
    public void run() {
        JSONObject response = JsonRequest.getJsonRequest("tournaments/balkoura_1vs1_test");
        assert response != null;
        System.out.println(response.get("tournament"));
        // </>
    }
}
