package com.gourmetto.factionsreinvented.auth.defaults.permission;

import com.gourmetto.factionsreinvented.auth.catalog.permission.InMemoryPermissionCatalog;
import com.gourmetto.factionsreinvented.auth.catalog.permission.PermissionCatalog;
import com.gourmetto.factionsreinvented.auth.catalog.permission.PermissionCatalogEntry;
import com.gourmetto.factionsreinvented.auth.catalog.permission.PermissionCatalogProvider;

import java.util.List;

/**
 * Default provider for the built-in permission catalog.
 *
 * <p>Builds a read-only in-memory catalog with UI metadata for in-game screens.</p>
 */
public final class DefaultPermissionCatalogProvider implements PermissionCatalogProvider {

    private final PermissionCatalog catalog;

    public DefaultPermissionCatalogProvider() {
        this.catalog = new InMemoryPermissionCatalog(List.of(
                new PermissionCatalogEntry(
                        DefaultPermissions.BANK_DEPOSIT,
                        "Deposit to Bank",
                        "Allows depositing items or currency into the faction bank."
                ),
                new PermissionCatalogEntry(
                        DefaultPermissions.BANK_WITHDRAW,
                        "Withdraw from Bank",
                        "Allows withdrawing items or currency from the faction bank."
                ),
                new PermissionCatalogEntry(
                        DefaultPermissions.CHAT_SEND,
                        "Send Messages",
                        "Allows sending messages in faction chat."
                ),
                new PermissionCatalogEntry(
                        DefaultPermissions.CHAT_MODERATE,
                        "Moderate Chat",
                        "Allows muting members and deleting chat messages."
                ),
                new PermissionCatalogEntry(
                        DefaultPermissions.MEMBER_INVITE,
                        "Invite Members",
                        "Allows inviting players to the faction."
                ),
                new PermissionCatalogEntry(
                        DefaultPermissions.MEMBER_KICK,
                        "Kick Members",
                        "Allows removing members from the faction."
                )

                // TODO : same as with @DefaultPermissions, complete the list of permissions and descriptions
        ));
    }

    @Override
    public PermissionCatalog getCatalog() {
        return catalog;
    }
}