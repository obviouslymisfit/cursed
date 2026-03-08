package misfit.cursed.runtime;

import misfit.cursed.runtime.persistence.RunPersistence;

import java.nio.file.Path;
import java.util.UUID;

/**
 * RunManager is the central controller for the runtime engine.
 *
 * It owns the active RunState and provides the only safe way
 * to access or modify it.
 *
 * Responsibilities:
 * - load run state on server start
 * - save run state
 * - create new runs
 * - expose run state to other systems
 *
 * All gameplay systems must interact with the run through this manager
 * rather than reading or writing persistence directly.
 */
public class RunManager {

    private RunState runState;

    private final Path worldFolder;

    /**
     * Constructs the runtime manager for a specific world.
     */
    public RunManager(Path worldFolder) {
        this.worldFolder = worldFolder;
    }

    /**
     * Loads run state from disk.
     *
     * If no run exists yet, the manager will remain in NO_RUN state.
     */
    public void load() {

        runState = RunPersistence.load(worldFolder);

        if (runState == null) {
            runState = new RunState();
            runState.setLifecycleState(RunLifecycleState.NO_RUN);
        }
    }

    /**
     * Saves the current run state to disk.
     */
    public void save() {

        if (runState == null) {
            return;
        }

        runState.setLastUpdated(System.currentTimeMillis());

        RunPersistence.save(worldFolder, runState);
    }

    /**
     * Creates a brand-new run instance.
     *
     * This resets all runtime state and assigns a new run ID.
     */
    public void createNewRun() {

        runState = new RunState(UUID.randomUUID());

        save();
    }

    /**
     * Returns the current run state.
     */
    public RunState getRunState() {
        return runState;
    }

    /**
     * Returns the current lifecycle state.
     */
    public RunLifecycleState getLifecycleState() {
        return runState.getLifecycleState();
    }
}