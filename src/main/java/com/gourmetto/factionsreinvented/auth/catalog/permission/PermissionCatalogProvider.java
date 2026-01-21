package com.gourmetto.factionsreinvented.auth.catalog.permission;

/**
 * Provides access to the permission catalog used by the application.
 *
 * <p>This indirection allows swapping implementations later
 * (e.g. JSON-backed, DB-backed) without changing consumers.</p>
 */
public interface PermissionCatalogProvider {

    /**
     * Returns the permission catalog.
     */
    PermissionCatalog getCatalog();
}
