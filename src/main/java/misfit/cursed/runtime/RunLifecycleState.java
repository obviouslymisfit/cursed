package misfit.cursed.runtime;

/**
 * RunLifecycleState defines the lifecycle states of a CURSED run.
 *
 * A run represents a full campaign instance. Only one run exists at a time.
 * The lifecycle state controls whether gameplay systems are allowed to
 * operate or must remain paused.
 *
 * State transitions are intentionally strict to prevent undefined behavior.
 *
 * Lifecycle overview:
 *
 * NO_RUN
 *   No run has been created yet.
 *
 * PAUSED
 *   A run exists but gameplay is not progressing.
 *   This is also the state forced on server restart for safety.
 *
 * RUNNING
 *   Active gameplay state. All runtime systems may operate.
 *
 * COMPLETED
 *   Terminal state when victory conditions are met.
 *
 * FAILED
 *   Terminal state when failure conditions occur.
 *
 * ABORTED
 *   Terminal state when a run is manually terminated by an admin.
 *
 * Terminal states freeze gameplay systems and prevent further progression.
 */
public enum RunLifecycleState {

    /**
     * No run exists yet.
     */
    NO_RUN,

    /**
     * Run exists but gameplay is paused.
     * This is the safe default state after loading from disk.
     */
    PAUSED,

    /**
     * Active gameplay state.
     */
    RUNNING,

    /**
     * Run ended successfully.
     */
    COMPLETED,

    /**
     * Run ended due to failure conditions.
     */
    FAILED,

    /**
     * Run was manually aborted.
     */
    ABORTED
}