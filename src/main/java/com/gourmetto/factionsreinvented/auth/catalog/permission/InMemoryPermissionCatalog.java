package com.gourmetto.factionsreinvented.auth.catalog.permission;

import com.gourmetto.factionsreinvented.auth.core.permission.PermissionKey;

import java.util.*;

/**
 * Simple in-memory implementation of {@link PermissionCatalog}.
 *
 * <p>This catalog is immutable and thread-safe after construction.
 * It is intended to be used as a read-only source of permission metadata
 * for UI, validation, and tooling.</p>
 */
public final class InMemoryPermissionCatalog implements PermissionCatalog {

    private final Map<PermissionKey, PermissionCatalogEntry> entries;

    /**
     * Creates an in-memory catalog from the given entries.
     *
     * @param entries permission catalog entries
     * @throws IllegalArgumentException if duplicate permission keys are provided
     */
    public InMemoryPermissionCatalog(Collection<PermissionCatalogEntry> entries) {
        Objects.requireNonNull(entries, "entries");

        Map<PermissionKey, PermissionCatalogEntry> tmp = new LinkedHashMap<>();
        for (PermissionCatalogEntry entry : entries) {
            PermissionCatalogEntry previous = tmp.put(entry.key(), entry);
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate PermissionKey in catalog: " + entry.key()
                );
            }
        }

        this.entries = Collections.unmodifiableMap(tmp);
    }

    @Override
    public boolean isValid(PermissionKey key) {
        return entries.containsKey(key);
    }

    @Override
    public Optional<PermissionCatalogEntry> get(PermissionKey key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public Collection<PermissionCatalogEntry> all() {
        return entries.values();
    }
}
