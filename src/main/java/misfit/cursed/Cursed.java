package misfit.cursed;

import misfit.cursed.runtime.RunManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.storage.LevelResource;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

/**
 * Main entry point for the CURSED Fabric mod.
 *
 * This class is responsible for initializing the runtime engine
 * and connecting it to the Minecraft server lifecycle.
 */
public class Cursed implements ModInitializer {

	public static final String MOD_ID = "cursed";

	private static RunManager runManager;

	/**
	 * Provides global access to the RunManager.
	 */
	public static RunManager getRunManager() {
		return runManager;
	}

	@Override
	public void onInitialize() {

        /*
         SERVER START EVENT

         This is triggered when the Minecraft server has fully started.
         At this point the world save folder is available.
         */
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);

        /*
         SERVER STOP EVENT

         Ensures the run state is saved safely before shutdown.
         */
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
	}

	private void onServerStarted(MinecraftServer server) {

		Path worldFolder = server.getWorldPath(LevelResource.ROOT);

		runManager = new RunManager(worldFolder);

		runManager.load();
	}

	private void onServerStopping(MinecraftServer server) {

		if (runManager != null) {
			runManager.save();
		}
	}
}