package com.gourmetto.factionsreinvented.auth.catalog.permission;

import com.gourmetto.factionsreinvented.auth.core.permission.PermissionKey;

import java.util.Objects;

/**
 * Descriptive metadata for a {@link PermissionKey}.
 *
 * <p>A {@code PermissionCatalogEntry} provides <b>human-facing information</b>
 * about a permission for UI, documentation, and tooling.
 *
 * <p>This class:
 * <ul>
 *   <li>does <b>not</b> grant permissions</li>
 *   <li>does <b>not</b> participate in authorization decisions</li>
 *   <li>exists solely for description and presentation</li>
 * </ul>
 *
 * <p>Instances are immutable and safe to share.</p>
 */
public final class PermissionCatalogEntry {

    /**
     * Stable permission identifier.
     */
    private final PermissionKey key;

    /**
     * Short human-friendly name (for buttons, checkboxes, lists).
     */
    private final String displayName;

    /**
     * Longer human-friendly description (for tooltips, help text).
     */
    private final String description;

    /**
     * Creates a catalog entry for a permission.
     *
     * @param key         stable permission key (must not be null)
     * @param displayName short human-readable name (must not be blank)
     * @param description longer description (must not be null; may be empty)
     */
    public PermissionCatalogEntry(
            PermissionKey key,
            String displayName,
            String description
    ) {
        this.key = Objects.requireNonNull(key, "key");
        this.displayName = requireNonBlank(displayName, "displayName");
        this.description = Objects.requireNonNull(description, "description");
    }

    /**
     * Returns the stable permission key.
     */
    public PermissionKey key() {
        return key;
    }

    /**
     * Returns the short display name.
     *
     * <p>Intended for labels in the GUI.</p>
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Returns the longer description.
     *
     * <p>Intended for tooltips, help panels, or documentation.</p>
     */
    public String description() {
        return description;
    }

    @Override
    public String toString() {
        return "PermissionCatalogEntry{" +
                "key=" + key +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    private static String requireNonBlank(String value, String field) {
        Objects.requireNonNull(value, field);
        String v = value.trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return v;
    }
}
