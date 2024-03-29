package org.alexdev.havana.game.commands.registered.moderation;

import org.alexdev.havana.game.achievements.AchievementManager;
import org.alexdev.havana.game.ads.AdManager;
import org.alexdev.havana.game.catalogue.CatalogueManager;
import org.alexdev.havana.game.catalogue.collectables.CollectablesManager;
import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.commands.CommandFormatBuilder;
import org.alexdev.havana.game.commands.CommandManager;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.events.EventsManager;
import org.alexdev.havana.game.games.GameManager;
import org.alexdev.havana.game.games.snowstorm.SnowStormMapsManager;
import org.alexdev.havana.game.item.ItemManager;
import org.alexdev.havana.game.item.ItemVersionManager;
import org.alexdev.havana.game.navigator.NavigatorManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerManager;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.game.room.models.RoomModelManager;
import org.alexdev.havana.game.texts.TextsManager;
import org.alexdev.havana.game.wordfilter.WordfilterManager;
import org.alexdev.havana.messages.incoming.catalogue.GET_CATALOG_INDEX;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;
import org.alexdev.havana.util.config.GameConfiguration;
import org.alexdev.havana.util.config.writer.GameConfigWriter;

public class ReloadCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.COMMUNITY_MANAGER);
    }

    @Override
    public void addArguments() {
        this.arguments.add("component");
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) entity;

        if (player.getRoomUser().getRoom() == null) {
            return;
        }

        String component = args[0];
        String componentName = null;

        if (component.equalsIgnoreCase("catalogue")
                || component.equalsIgnoreCase("shop")
                || component.equalsIgnoreCase("items")) {
            ItemManager.reset();
            CatalogueManager.reset();
            CollectablesManager.reset();

            // Regenerate collision map with proper height differences (if they changed).
            player.getRoomUser().getRoom().getMapping().regenerateCollisionMap();
            player.getRoomUser().getRoom().getMapping().sendMap();

            for (Player p : PlayerManager.getInstance().getPlayers()) {
                new GET_CATALOG_INDEX(false).handle(p, null);
            }

            componentName = "Catalogue and item definitions";
        }

        if (component.equalsIgnoreCase("wordfilter")) {
            WordfilterManager.reset();
            componentName = "Wordfilter";
        }

        if (component.equalsIgnoreCase("commands")) {
            CommandManager.reset();
            componentName = "Commands";
        }

        if (component.equalsIgnoreCase("badgebuy")) {
            CatalogueManager.getInstance().reloadBadgeRewards();
            componentName = "Badge sale rewards";
        }

        if (component.equalsIgnoreCase("ads")) {
            AdManager.getInstance().reset();
            componentName = "Advertisements";
        }

        if (component.equalsIgnoreCase("navigator")) {
            NavigatorManager.reset();
            componentName = "Navigator";
        }

        if (component.equalsIgnoreCase("texts")) {
            TextsManager.reset();
            componentName = "Texts";
        }

        if (component.equalsIgnoreCase("games")) {
            GameManager.reset();
            componentName = "Games";
        }

        if (component.equalsIgnoreCase("events")) {
            EventsManager.reset();
            componentName = "Events";
        }

        if (component.equalsIgnoreCase("gamemaps")) {
            SnowStormMapsManager.reset();
            componentName = "game maps";
        }


        if (component.equalsIgnoreCase("achievements") ||
                component.equalsIgnoreCase("ach")) {
            AchievementManager.reset();
            componentName = "Achievements";
        }

        if (component.equalsIgnoreCase("models")) {
            RoomModelManager.reset();
            componentName = "Room models";
        }

        if (component.equalsIgnoreCase("collectables")) {
            CollectablesManager.reset();
            componentName = "Collectables";
        }

        if (component.equalsIgnoreCase("settings") ||
            component.equalsIgnoreCase("config")) {
            
            GameConfiguration.reset(new GameConfigWriter());
            componentName = "Game settings";
        }

        if (component.equalsIgnoreCase("versions")) {

            ItemVersionManager.reset();
            componentName = "Furni settings";
        }


        if (componentName != null) {
            player.send(new ALERT(componentName + " have been reloaded."));
        } else {
            var alert = new CommandFormatBuilder(entity);

            alert.append("You did not specify which component to reload!");
            alert.newLine();
            alert.append("You may reload either the catalogue/shop/items, advertisements, events, commands,");
            alert.newLine();
            alert.append("navigator, collectables, models, texts, plugins, wordfitler, games, badgebuy,");
            alert.newLine();
            alert.append("rewards, versions or settings.");

            player.send(new ALERT(alert.toString()));
        }
    }

    @Override
    public String getDescription() {
        return "Refresh the settings/items/texts";
    }
}