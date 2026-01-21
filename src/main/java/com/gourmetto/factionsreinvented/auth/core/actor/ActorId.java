package com.gourmetto.factionsreinvented.auth.core.actor;

import java.util.Objects;
import java.util.UUID;

/**
 * Strongly-typed identifier for {@link Actor}.
 *
 * <p>Wrapping {@link UUID} prevents accidental mixing of identifiers from different aggregates
 * (domains, roles, etc.) and makes APIs self-documenting.
 */
public final class ActorId {

    private final UUID uuid;

    /**
     * Creates an {@code ActorId} backed by a UUID.
     *
     * @param uuid stable UUID (must not be null)
     */
    public ActorId(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
    }

    /**
     * Returns the backing UUID.
     *
     * @return UUID backing this actor id
     */
    public UUID uuid() {
        return uuid;
    }

    /**
     * Parses an {@code ActorId} from a UUID string.
     *
     * @param uuid UUID string
     * @return parsed actor identifier
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static ActorId fromString(String uuid) {
        Objects.requireNonNull(uuid, "uuid");
        return new ActorId(UUID.fromString(uuid.trim()));
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorId other)) return false;
        return uuid.equals(other.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
