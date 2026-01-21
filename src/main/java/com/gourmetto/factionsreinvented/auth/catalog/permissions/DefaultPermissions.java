package com.gourmetto.factionsreinvented.auth.catalog.permissions;

import com.gourmetto.factionsreinvented.auth.core.permission.PermissionKey;
import com.gourmetto.factionsreinvented.auth.core.resource.ResourceType;

public final class DefaultPermissions {

    private DefaultPermissions() {}

    public static final PermissionKey BANK_WITHDRAW =
            PermissionKey.of("WITHDRAW", ResourceType.BANK);

    public static final PermissionKey BANK_DEPOSIT =
            PermissionKey.of("DEPOSIT", ResourceType.BANK);

    public static final PermissionKey CHAT_SEND =
            PermissionKey.of("SEND", ResourceType.CHAT);

    public static final PermissionKey CHAT_MODERATE =
            PermissionKey.of("MODERATE", ResourceType.CHAT);

    public static final PermissionKey MEMBER_INVITE =
            PermissionKey.of("INVITE", ResourceType.MEMBER);

    public static final PermissionKey MEMBER_KICK =
            PermissionKey.of("KICK", ResourceType.MEMBER);

    // TODO : Write the full list of permissions, this serves only as an example
}
