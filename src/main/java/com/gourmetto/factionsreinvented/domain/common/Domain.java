package com.gourmetto.factionsreinvented.domain.common;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Pure domain model representing an authorization boundary ("Domain") such as a guild or subguild.
 *
 * <p>This class is intentionally small:
 * <ul>
 *   <li>It stores identity and hierarchy ({@link DomainId}).</li>
 *   <li>It may store UI-oriented metadata (display name) if you want it here.</li>
 *   <li>It does <b>not</b> embed members/roles/permissions; those live in repositories/services keyed by {@link DomainId}.</li>
 * </ul>
 *
 * <p><b>Important:</b> With UUID-backed {@link DomainId}, your persistence model should typically store one row per
 * domain node (each segment UUID) with a {@code parent_uuid} foreign key. This {@code Domain} class remains a
 * business object and does not validate parent existence (that is enforced by services/DB constraints).
 */
public class Domain {

    /**
     * Full hierarchical identifier (UUID-backed segments).
     *
     * <p>Example stable form:
     * {@code guild:<uuid>/sub:<uuid>/sub:<uuid>}
     */
    private final DomainId domainId;

    /**
     * Creates a Domain with a hierarchical {@link DomainId} and optional leaf display name.
     *
     * @param domainId hierarchical id (must not be null)
     */
    public Domain(DomainId domainId) {
        this.domainId = Objects.requireNonNull(domainId, "domainId");
    }

    /**
     * Convenience factory for a root guild domain.
     *
     * <p>Identity is UUID-backed; the display name is UI metadata.
     *
     * @param displayName human display name (optional)
     * @return root guild domain
     */
    public static Domain guild(String displayName) {
        DomainId id = DomainId.root("guild", displayName);
        return new Domain(id);
    }

    /**
     * Creates a child domain (e.g., subguild) under this domain.
     *
     * <p><b>Warning:</b> This does not check whether the parent exists in storage.
     * Enforce that constraint in your service layer and/or via a DB foreign key.
     *
     * @param prefix segment prefix (e.g., "sub")
     * @param displayName human display name (optional)
     * @return new child domain instance
     */
    public Domain child(String prefix, String displayName) {
        DomainId childId = this.domainId.child(prefix, displayName);
        return new Domain(childId);
    }

    /**
     * Returns the hierarchical id for this domain.
     */
    public DomainId id() {
        return domainId;
    }

    /**
     * Returns the stable UUID of the leaf segment (the current domain node).
     *
     * <p>This is typically what you use as the primary key for a {@code domains} table row.
     */
    public UUID leafUuid() {
        return domainId.leaf().uuid();
    }

    /**
     * Returns the prefix of the leaf segment (e.g. "guild", "sub").
     */
    public String leafPrefix() {
        return domainId.leaf().prefix();
    }

    /**
     * Returns this domain's display name if available. Returns only the display name of the leaf, not the whole domain inheritance.
     *
     * <p>If not set on this object, callers may fall back to {@link DomainId#toDisplayString()}
     * or to a display-name lookup from storage.
     */
    public Optional<String> displayName() {
        return Optional.ofNullable(domainId.displayName());
    }

    /**
     * Returns a stable string suitable for DB keys/caches/logs.
     *
     * <p>Uses UUID-backed serialization: {@link DomainId#toString()}.
     */
    public String stableKey() {
        return domainId.toString();
    }

    /**
     * Returns a human-friendly representation for debugging/logging.
     *
     * <p>Not stable; display names can change.
     */
    public String displayPath() {
        return domainId.toDisplayString();
    }

    /**
     * Returns the parent domain id if this domain is not the root.
     */
    public Optional<DomainId> parentId() {
        return domainId.parent();
    }

    /**
     * Returns true if this domain has no parent.
     */
    public boolean isRoot() {
        return domainId.isRoot();
    }

    @Override
    public String toString() {
        // Prefer stable form for toString to avoid surprising changes when display names update.
        return "Domain{id=" + stableKey() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Domain other)) return false;
        return domainId.equals(other.domainId);
    }

    @Override
    public int hashCode() {
        return domainId.hashCode();
    }

    private static String normalizeDisplayName(String displayName) {
        if (displayName == null) return null;
        String d = displayName.trim();
        if (d.isEmpty()) return null;
        return d;
    }
}
