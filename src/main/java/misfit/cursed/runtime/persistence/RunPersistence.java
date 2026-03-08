package misfit.cursed.runtime.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import misfit.cursed.runtime.RunLifecycleState;
import misfit.cursed.runtime.RunState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * RunPersistence handles loading and saving the RunState to disk.
 *
 * The run state is stored in the world save folder under:
 *
 *   <world>/cursed/run_state.json
 *
 * This class is responsible only for disk persistence.
 * It does not contain gameplay logic.
 *
 * Safety guarantees:
 * - JSON serialization via Gson
 * - atomic write using temporary files
 * - restart safety conversion RUNNING → PAUSED
 */
public class RunPersistence {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Loads run state from disk.
     *
     * If no run file exists, returns null.
     */
    public static RunState load(Path worldFolder) {

        Path cursedDir = worldFolder.resolve("cursed");
        Path runFile = cursedDir.resolve("run_state.json");

        if (!Files.exists(runFile)) {
            return null;
        }

        try {
            String json = Files.readString(runFile);
            RunState state = GSON.fromJson(json, RunState.class);

            /*
             Restart safety rule.

             If the server stopped while the run was RUNNING,
             the system must force the run back to PAUSED.

             This prevents gameplay progression while the
             server was offline.
             */
            if (state.getLifecycleState() == RunLifecycleState.RUNNING) {
                state.setLifecycleState(RunLifecycleState.PAUSED);
            }

            return state;

        } catch (IOException e) {
            throw new RuntimeException("Failed to load CURSED run state", e);
        }
    }

    /**
     * Saves the run state to disk.
     *
     * Uses a temporary file followed by rename to prevent
     * corruption if the server crashes during write.
     */
    public static void save(Path worldFolder, RunState state) {

        Path cursedDir = worldFolder.resolve("cursed");

        try {
            Files.createDirectories(cursedDir);

            Path runFile = cursedDir.resolve("run_state.json");
            Path tempFile = cursedDir.resolve("run_state.tmp");

            String json = GSON.toJson(state);

            Files.writeString(tempFile, json);

            /*
             Replace the existing run file atomically.
             */
            Files.move(tempFile, runFile,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save CURSED run state", e);
        }
    }
}