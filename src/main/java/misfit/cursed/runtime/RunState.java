package misfit.cursed.runtime;

import java.util.UUID;

/**
 * RunState represents the complete persistent state of a CURSED run.
 *
 * A "run" is the active campaign instance for the server. Only one run
 * exists at a time, and its state is stored on disk so that the game
 * can survive server restarts safely.
 *
 * This class intentionally contains only data and no gameplay logic.
 * All logic that manipulates this state belongs in RunManager or
 * other runtime systems.
 *
 * The RunState object is serialized to JSON and written to:
 *
 *   <world>/cursed/run_state.json
 *
 * Because this object is persisted directly, its structure must remain
 * stable and predictable.
 */
public class RunState {

    /**
     * Unique identifier for the run.
     *
     * A new UUID is generated whenever a brand new run is created.
     * This allows runs to be uniquely identified in logs and debugging.
     */
    private UUID runId;

    /**
     * Current lifecycle state of the run.
     *
     * Determines whether gameplay systems are allowed to operate.
     */
    private RunLifecycleState lifecycleState;

    /**
     * Current global phase of the run.
     *
     * Phase progression is implemented in later milestones,
     * but the field exists now so it can be persisted safely.
     */
    private int currentPhase;

    /**
     * Timestamp when the run was created.
     *
     * Stored as epoch milliseconds.
     */
    private long createdAt;

    /**
     * Timestamp of the most recent state update.
     *
     * Used for debugging and persistence tracking.
     */
    private long lastUpdated;

    /**
     * Default constructor required for JSON deserialization.
     */
    public RunState() {
    }

    /**
     * Creates a brand-new run state.
     */
    public RunState(UUID runId) {
        this.runId = runId;
        this.lifecycleState = RunLifecycleState.PAUSED;
        this.currentPhase = 1;

        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.lastUpdated = now;
    }

    public UUID getRunId() {
        return runId;
    }

    public void setRunId(UUID runId) {
        this.runId = runId;
    }

    public RunLifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public void setLifecycleState(RunLifecycleState lifecycleState) {
        this.lifecycleState = lifecycleState;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}