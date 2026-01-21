package com.gourmetto.factionsreinvented.auth.core.resource;

/**
 * High-level resource categories for permissions.
 *
 * <p>A {@code ResourceType} represents <b>what kind of feature</b> a permission applies to
 * inside a {@code Domain} (guild, sub-guild, etc.).
 *
 * <p>Resource types are:
 * <ul>
 *   <li>explicitly listed (enum)</li>
 *   <li>stable across time</li>
 *   <li>used by policies (inheritance, restrictions)</li>
 * </ul>
 *
 * <p>They are <b>not</b> authorization boundaries and must not be confused with {@code Domain}s.
 */
public enum ResourceType {

    /**
     * Shared storage and economy features.
     */
    BANK,

    /**
     * Text or voice communication.
     */
    CHAT,

    /**
     * Membership and roster management.
     */
    MEMBER,

    /**
     * Sub-groups management (Sub-guilds etc).
     */
    SUB,

    /**
     * Role and permission management.
     */
    ROLE,

    /**
     * Diplomacy, war, alliances.
     */
    DIPLOMACY,

    /**
     * Guild or sub-guild configuration.
     */
    ADMIN,

    /**
     * Events, raids, activities.
     */
    EVENT
}
