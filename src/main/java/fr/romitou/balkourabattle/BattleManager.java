package fr.romitou.balkourabattle;

import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.Participant;
import at.stefangeyer.challonge.model.Tournament;
import at.stefangeyer.challonge.model.enumeration.MatchState;
import at.stefangeyer.challonge.model.enumeration.TournamentState;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fr.romitou.balkourabattle.elements.Arena;
import fr.romitou.balkourabattle.elements.ArenaStatus;
import fr.romitou.balkourabattle.elements.ArenaType;
import fr.romitou.balkourabattle.elements.MatchScore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class BattleManager {

    public static List<Match> waitingMatches = new ArrayList<>();
    public static List<OfflinePlayer> freeze = new ArrayList<>();
    public static List<OfflinePlayer> combat = new ArrayList<>();
    public static HashMap<Arena, Match> arenas = new HashMap<>();
    public static HashMap<Match, Integer> timers = new HashMap<>();
    public static HashMap<Match, Integer> disconnections = new HashMap<>();
    public static HashMap<OfflinePlayer, Integer> playerDisconnections = new HashMap<>();
    public static BiMap<Participant, UUID> registeredParticipants = HashBiMap.create();
    public static List<Integer> intAnnouncements = new LinkedList<>(List.of(50, 40, 30, 20, 10, 5, 3, 2, 1));
    public static Location spawn;

    public static void registerArenasFromConfig() {
        ConfigurationSection config = BalkouraBattle.getConfigFile().getConfigurationSection("arenas");
        assert config != null;
        System.out.println(config.getInt("1.firstLocation.x"));
        config.getKeys(false).forEach(key -> arenas.put(new Arena(
                config.getInt(key + ".id"),
                new Location[]{
                        getLocation(
                                config.getInt(key + ".firstLocation.x"),
                                config.getInt(key + ".firstLocation.y"),
                                config.getInt(key + ".firstLocation.z")
                        ),
                        getLocation(
                                config.getInt(key + ".secondLocation.x"),
                                config.getInt(key + ".secondLocation.y"),
                                config.getInt(key + ".secondLocation.z")
                        ),
                },
                ArenaStatus.FREE,
                config.getBoolean(key + ".isFinalArena") ? ArenaType.FINAL : ArenaType.CLASSIC
        ), null));
    }

    public static void stopDisconnectionTimer(Match match) {
        int taskId = Optional.ofNullable(disconnections.get(match)).orElse(0);
        if (taskId == 0) return;
        Bukkit.getScheduler().cancelTask(taskId);
        disconnections.remove(match);
    }

    public static void stopTimer(Match match) {
        int taskId = Optional.ofNullable(timers.get(match)).orElse(0);
        if (taskId == 0) return;
        Bukkit.getScheduler().cancelTask(taskId);
        timers.remove(match);
    }

    public static List<Match> getWaitingMatches() {
        return waitingMatches.stream()
                .filter(Objects::nonNull)
                .filter(match -> match.getUnderwayAt() == null
                        && match.getState() == MatchState.OPEN)
                .collect(Collectors.toList());
    }

    public static List<Match> getApprovalWaitingMatchs() {
        return arenas.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()) && Objects.nonNull(entry.getKey()))
                .filter(entry -> entry.getKey().getArenaStatus() == ArenaStatus.VALIDATING
                        && entry.getValue().getState() == MatchState.OPEN)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public static Arena getArenaByMatchId(long matchId) {
        return arenas.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()) && Objects.nonNull(entry.getKey()))
                .filter(entry -> entry.getValue().getId().equals(matchId))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static Match getCurrentMatchByPlayerId(long playerId) {
        return arenas.values()
                .stream()
                .filter(Objects::nonNull)
                .filter(match -> (match.getPlayer1Id().equals(playerId)
                        || match.getPlayer2Id().equals(playerId)))
                .findFirst()
                .orElse(null);
    }

    public static List<Arena> getAvailableArenas() {
        return arenas.keySet()
                .stream()
                .filter(match -> match.getArenaStatus() == ArenaStatus.FREE)
                .collect(Collectors.toList());
    }

    public static Boolean containsName(String name) {
        return registeredParticipants.keySet()
                .stream()
                .anyMatch(participant -> participant.getName().equals(name));
    }

    public static Match getMatch(long matchId) {
        return waitingMatches.stream()
                .filter(Objects::nonNull)
                .filter(match -> match.getId().equals(matchId))
                .findFirst()
                .orElse(null);
    }

    public static Match getHandledMatch(long matchId) {
        return arenas.values()
                .stream()
                .filter(Objects::nonNull)
                .filter(match -> match.getId().equals(matchId))
                .findFirst()
                .orElse(null);
    }


    public static OfflinePlayer getPlayer(long participantId) {
        UUID uuid = registeredParticipants.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getId().equals(participantId))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
        if (uuid == null) return null;
        return getBukkitOfflinePlayer(uuid);
    }

    public static OfflinePlayer getBukkitOfflinePlayer(UUID uuid) {
        long now = System.currentTimeMillis();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        System.out.println("Fetched " + offlinePlayer.getName() + " in " + (System.currentTimeMillis() - now) + "ms.");
        return offlinePlayer;
    }

    public static String getDisplayedPlayerName(long participantId) {
        OfflinePlayer player = getPlayer(participantId);
        if (player == null) return "Inconnu";
        return Optional.ofNullable(player.getName()).orElse("Inconnu");
    }

    public static List<String> getDisplayedPlayersNames(Match match) {
        return List.of(
                getDisplayedPlayerName(match.getPlayer1Id()),
                getDisplayedPlayerName(match.getPlayer2Id())
        );
    }

    public static List<OfflinePlayer> getPlayers(Match match) {
        OfflinePlayer player1 = getPlayer(match.getPlayer1Id());
        OfflinePlayer player2 = getPlayer(match.getPlayer2Id());
        if (player1 == null || player2 == null) return null;
        return List.of(player1, player2);
    }

    public static void initPlayers(List<OfflinePlayer> offlinePlayers) {
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        offlinePlayers.stream()
                .filter(player -> player.getPlayer() != null)
                .forEach(player -> {
                    player.getPlayer().getInventory().clear();
                    player.getPlayer().getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                    player.getPlayer().getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                    player.getPlayer().getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                    player.getPlayer().getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                    player.getPlayer().getInventory().setItem(0, sword);
                    player.getPlayer().getInventory().setItem(1, new ItemStack(Material.BOW));
                    player.getPlayer().getInventory().setItem(2, new ItemStack(Material.ARROW, 12));
                    player.getPlayer().getInventory().setItem(7, new ItemStack(Material.GOLDEN_APPLE, 3));
                    player.getPlayer().getInventory().setItem(8, new ItemStack(Material.GOLDEN_CARROT, 12));
                    player.getPlayer().updateInventory();
                    player.getPlayer().setHealth(20);
                    player.getPlayer().setFoodLevel(20);
                });
    }

    public static List<Match> getAllMatches(long participantId) {
        return waitingMatches
                .stream()
                .filter(match -> match.getPlayer1Id().equals(participantId)
                        || match.getPlayer2Id().equals(participantId))
                .collect(Collectors.toList());
    }

    public static List<Player> getOnlineModerators() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission("modo.event"))
                .collect(Collectors.toList());
    }

    public static List<Player> getAvailablePlayers() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getGameMode() == GameMode.SPECTATOR)
                .collect(Collectors.toList());
    }

    public static Participant getParticipant(UUID uuid) {
        return registeredParticipants.inverse().get(uuid);
    }

    public static void deathMatch(List<Player> players) {
        PotionEffect potionEffect = new PotionEffect(
                PotionEffectType.WITHER,
                30,
                1,
                false,
                false
        );
        players.forEach(potionEffect::apply);
    }

    public static void sendMatchInfo(OfflinePlayer offlinePlayer, long matchId) {
        if (offlinePlayer.getPlayer() == null) return;
        Player player = offlinePlayer.getPlayer();
        Match match = BattleManager.getMatch(matchId);
        ChatManager.sendBeautifulMessage(player, matchInfo(match).toArray(new String[0]));
    }

    public static void sendParticipantMatchesInfo(Player player) {
        Participant participant = getParticipant(player.getUniqueId());
        if (participant == null) {
            ChatManager.sendMessage(player, "Vous n'êtes pas inscrit pour participer à ce tournois.");
            return;
        }
        List<Match> matches = getAllMatches(participant.getId());
        List<TextComponent> textComponents = new LinkedList<>();
        textComponents.add(new TextComponent("   §f§l» §eVos matchs :"));
        textComponents.add(new TextComponent(""));
        if (matches == null || matches.size() == 0) {
            textComponents.add(new TextComponent("   §fInformations non disponibles."));
            textComponents.add(new TextComponent("   §7Réessayez dans quelques instants."));
        } else {
            matches.forEach(match -> {
                TextComponent base = new TextComponent("   §e● §fMatch " + match.getIdentifier() + " §7| ");
                TextComponent info = new TextComponent("§6[+ d'infos] ");
                info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/battle info " + match.getId()));
                base.addExtra(info);
                textComponents.add(base);
            });
        }
        ChatManager.sendBeautifulMessage(player, textComponents);
    }

    public static List<String> matchInfo(Match match) {
        List<String> stringList = new LinkedList<>();
        stringList.add("   §f§l» §eInformations sur le match :");
        stringList.add("");
        if (match == null) {
            stringList.add("   §fInformations non disponibles.");
            stringList.add("   §7Réessayez dans quelques instants.");
        } else {
            MatchScore scores = new MatchScore(match.getScoresCsv());
            stringList.add("   §e● §fMatch " + match.getIdentifier() + " :");
            stringList.add("      §e› §7Set : " + scores.getCurrentRound());
            stringList.add("      §e› §7Status : " + (
                    (match.getState() == MatchState.COMPLETE)
                            ? "§aTerminé"
                            : (match.getState() == MatchState.OPEN
                            || match.getState() == MatchState.PENDING)
                            ? (match.getUnderwayAt() == null)
                            ? "§eEn attente"
                            : "§6En cours"
                            : "§3Attente d'informations"
            ));
            long player1wins = scores.getWinSets(true);
            long player2wins = scores.getWinSets(false);
            List<String> playersNames = BattleManager.getDisplayedPlayersNames(match);
            stringList.add("      §e› §7Joueur 1 : " + playersNames.get(0) + " - "
                    + player1wins + " set" + (player1wins > 1 ? "s" : "") + " gagné" + (player1wins > 1 ? "s" : ""));
            stringList.add("      §e› §7Joueur 2 : " + playersNames.get(1) + " - "
                    + player2wins + " set" + (player2wins > 1 ? "s" : "") + " gagné" + (player2wins > 1 ? "s" : ""));
        }
        return stringList;
    }

    public static void sendArenaInfos(Player player) {
        Set<Arena> allArenas = arenas.keySet();
        List<String> stringList = new LinkedList<>();
        stringList.add("   §f§l» §eDisponibilité des arènes :");
        stringList.add("");
        if (allArenas.size() == 0) {
            stringList.add("   §fAucune arène enregistrée.");
            stringList.add("   §7Enregistrez-les dans le fichier de configuration.");
        } else {
            allArenas.forEach(arena -> stringList.add("   §e● §fArène " + arena.getId() + " §7| " + arena.getArenaStatus() + " §7| " + arena.getArenaType() + " §7| §fx:" + arena.getLocations()[0].getX() + ", y:" + arena.getLocations()[0].getY() + ", z:" + arena.getLocations()[0].getZ()));
        }
        ChatManager.sendBeautifulMessage(player, stringList.toArray(new String[0]));
    }

    public static void sendParticipantInfos(Player player) {
        Set<Participant> allParticipants = registeredParticipants.keySet();
        List<String> stringList = new LinkedList<>();
        stringList.add("   §f§l» §eParticipants enregistrés :");
        stringList.add("");
        if (allParticipants.size() == 0) {
            stringList.add("   §fAucun participant enregistré.");
            stringList.add("   §7Ajoutez-les via la commande /battle register.");
        } else {
            allParticipants.forEach(participant -> stringList.add("   §e● §f " + participant.getName()));
        }
        ChatManager.sendBeautifulMessage(player, stringList.toArray(new String[0]));
    }

    public static boolean isTournamentStarted() {
        Tournament tournament = ChallongeManager.getTournament();
        if (tournament == null) return false;
        return ChallongeManager.getTournament().getState() == TournamentState.UNDERWAY;
    }

    public static Location getLocation(int x, int y, int z) {
        return new Location(Bukkit.getWorld("world"), x, y, z);
    }

    public static Boolean hasPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) return true;
        ChatManager.sendMessage(sender, "Vous n'avez pas la permission d'exécuter cette commande.");
        return false;
    }

    public static Location getSpawn() {
        if (spawn != null) return spawn;
        int x = BalkouraBattle.getConfigFile().getInt("spawn.x"),
                y = BalkouraBattle.getConfigFile().getInt("spawn.y"),
                z = BalkouraBattle.getConfigFile().getInt("spawn.z");
        spawn = new Location(Bukkit.getWorld("world"), x, y, z);
        return spawn;
    }

}
