package com.gourmetto.factionsreinvented.auth.core.actor;

import java.util.Objects;
import java.util.UUID;

/**
 * Pure domain model representing an authorization principal ("Actor").
 *
 * <p>An {@code Actor} is any entity that can participate in authorization decisions:
 * <ul>
 *   <li>a human user</li>
 *   <li>a service account</li>
 *   <li>a bot or automation</li>
 * </ul>
 *
 * <p>This class is intentionally:
 * <ul>
 *   <li><b>final</b> (no subclassing for different actor kinds)</li>
 *   <li>immutable</li>
 *   <li>free of authorization logic</li>
 * </ul>
 *
 * <p>Differences between actors are expressed via {@link ActorType} and by external
 * profile/metadata objects (not by inheritance).
 */
public final class Actor {

    /**
     * Stable identifier for this actor (UUID-backed).
     */
    private final ActorId id;

    /**
     * Discriminator describing the kind of actor.
     *
     * <p>Used by policies to apply type-specific rules
     * (e.g. "service accounts cannot accept invites").
     */
    private final ActorType type;

    /**
     * Creates a new actor.
     *
     * @param id   stable actor identifier (must not be null)
     * @param type actor type discriminator (must not be null)
     */
    public Actor(ActorId id, ActorType type) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
    }

    /**
     * Convenience factory creating a new actor with a random UUID.
     *
     * @param type actor type discriminator
     * @return newly created actor
     */
    public static Actor create(ActorType type) {
        return new Actor(new ActorId(UUID.randomUUID()), type);
    }

    /**
     * Returns the stable identifier for this actor.
     *
     * @return actor identifier
     */
    public ActorId id() {
        return id;
    }

    /**
     * Returns the UUID backing this actor identifier.
     *
     * <p>Useful when interacting with persistence or external systems.
     *
     * @return backing UUID
     */
    public UUID uuid() {
        return id.uuid();
    }

    /**
     * Returns the actor type discriminator.
     *
     * @return actor type
     */
    public ActorType type() {
        return type;
    }

    @Override
    public String toString() {
        return "Actor{id=" + id + ", type=" + type + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
