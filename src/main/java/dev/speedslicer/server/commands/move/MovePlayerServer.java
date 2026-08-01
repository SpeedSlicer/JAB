package dev.speedslicer.server.commands.move;

import dev.speedslicer.server.instances.lobby.LobbyInstanceManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class MovePlayerServer extends Command {

    private final LobbyInstanceManager lobbyInstanceManager;

    public MovePlayerServer(LobbyInstanceManager lobbyInstanceManager) {
        super("movePlayerToLobby");

        this.lobbyInstanceManager = lobbyInstanceManager;

        var playerArgument = ArgumentType.String("player_username");
        var lobbyNumberArgument = ArgumentType.Integer("lobby_number");

        setDefaultExecutor((sender, context) ->
                sender.sendMessage(
                        "Usage: /movePlayerToLobby <player_username> <lobby_number>"
                )
        );

        addSyntax((sender, context) -> {
            String username = context.get(playerArgument);
            int lobbyNumber = context.get(lobbyNumberArgument);

            Player target = MinecraftServer.getConnectionManager()
                    .getOnlinePlayerByUsername(username);

            if (target == null) {
                sender.sendMessage("Player '" + username + "' is not online.");
                return;
            }

            sender.sendMessage(
                    "Moving " + target.getUsername()
                            + " to lobby " + lobbyNumber
            );

            lobbyInstanceManager.movePlayerToLobby(target, lobbyNumber);

        }, playerArgument, lobbyNumberArgument);
    }
}