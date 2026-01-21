package com.gourmetto.factionsreinvented.auth.catalog.permission;

import com.gourmetto.factionsreinvented.auth.core.permission.PermissionKey;

import java.util.Collection;
import java.util.Optional;

/**
 * Read-only catalog describing which permissions exist and how they should be presented.
 *
 * <p>A {@code PermissionCatalog}:
 * <ul>
 *   <li>does <b>not</b> grant permissions</li>
 *   <li>does <b>not</b> perform authorization</li>
 *   <li>exists solely to describe available permissions</li>
 * </ul>
 *
 * <p>Typical consumers:
 * <ul>
 *   <li>in-game role editor GUI</li>
 *   <li>validation when loading role definitions</li>
 *   <li>admin/debug tooling</li>
 * </ul>
 *
 * <p>The catalog is expected to be immutable and safe to share.
 */
public interface PermissionCatalog {

    /**
     * Returns {@code true} if the given permission key exists in the catalog.
     *
     * <p>This is useful for validating role definitions loaded from storage
     * or external configuration.</p>
     *
     * @param key permission key
     * @return whether the permission is recognized
     */
    boolean isValid(PermissionKey key);

    /**
     * Returns the catalog entry for the given permission key, if present.
     *
     * <p>Use this to retrieve UI metadata (display name, description).</p>
     *
     * @param key permission key
     * @return catalog entry if known
     */
    Optional<PermissionCatalogEntry> get(PermissionKey key);

    /**
     * Returns all permission catalog entries.
     *
     * <p>The returned collection should be treated as read-only.
     * Ordering is implementation-defined but should be stable.</p>
     *
     * @return all permission entries
     */
    Collection<PermissionCatalogEntry> all();
}
