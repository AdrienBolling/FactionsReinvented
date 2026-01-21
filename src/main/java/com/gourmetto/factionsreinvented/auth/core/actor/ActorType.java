package com.gourmetto.factionsreinvented.auth.core.actor;

/**
 * Discriminator describing the kind of {@link Actor}.
 *
 * <p>This enum is intentionally small and stable.
 * Behavior differences between actor types are enforced by policy
 * rather than by subclassing {@code Actor}.
 */
public enum ActorType {

    /**
     * Human player account.
     */
    USER,

    /**
     * Non-human service account (backend jobs, integrations, etc.).
     */
    SERVICE,

    /**
     * Automated or scripted actor (NPC controller, bot, etc.).
     */
    BOT
}
