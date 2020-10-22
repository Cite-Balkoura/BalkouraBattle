package fr.romitou.balkoura1v1.tasks;

import fr.romitou.balkoura1v1.utils.JsonRequest;
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
