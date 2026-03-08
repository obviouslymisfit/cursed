package misfit.cursed.commands;

import com.mojang.brigadier.CommandDispatcher;
import misfit.cursed.Cursed;
import misfit.cursed.runtime.RunState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

/**
 * CurseStatusCommand registers the /curse status command.
 *
 * This command is intentionally simple and exists primarily
 * to verify that the CURSED runtime engine has initialized
 * correctly and that the RunManager is accessible during
 * gameplay.
 *
 * The command displays basic runtime information:
 * - run ID
 * - lifecycle state
 * - current phase
 *
 * These values come directly from the RunState managed by
 * the RunManager.
 */
public class CurseStatusCommand {

    /**
     * Registers the command with Minecraft's command dispatcher.
     *
     * The dispatcher is provided during the command registration
     * phase of server startup.
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("curse")

                        /*
                         Restrict this command to moderators/operators.

                         The current Fabric 1.21.11 command documentation uses the
                         command source's permissions() view together with the
                         Permissions.COMMANDS_MODERATOR constant for moderator-only
                         commands.

                         This keeps admin/debug commands hidden from normal players
                         and matches the intended CURSED command split:
                         admin/debug commands now, player-facing commands later.
                        */
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))

                        .then(Commands.literal("status")
                                .executes(context -> {

                                    /*
                                     Retrieve the active run state from the runtime engine.
                                     */
                                    RunState state = Cursed.getRunManager().getRunState();

                                    /*
                                     Send informational messages back to the command caller.
                                     */
                                    context.getSource().sendSuccess(
                                            () -> Component.literal("CURSED STATUS"),
                                            false
                                    );

                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Run ID: " + state.getRunId()),
                                            false
                                    );

                                    context.getSource().sendSuccess(
                                            () -> Component.literal("State: " + state.getLifecycleState()),
                                            false
                                    );

                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Phase: " + state.getCurrentPhase()),
                                            false
                                    );

                                    return 1;
                                })
                        )
        );
    }
}