package com.gourmetto.factionsreinvented.auth.core.permission;

import com.gourmetto.factionsreinvented.auth.core.resource.ResourceType;

import java.util.Objects;
import java.util.Optional;

/**
 * Stable identifier for an authorization capability.
 *
 * <p>A {@code PermissionKey} is an <b>ALLOW-only</b> capability token:
 * <ul>
 *   <li>"Not allowed" is represented by the absence of an allow.</li>
 *   <li>Explicit blocking is handled by constraint relationships/policies (e.g., bans, mutes),
 *       not by "DENY permissions".</li>
 * </ul>
 *
 * <p>Identity is defined by:
 * <ul>
 *   <li>{@code action} (required)</li>
 *   <li>{@link ResourceType} (optional)</li>
 * </ul>
 *
 * <p>Canonical stable form (for DB keys/caches/network):
 * <ul>
 *   <li>{@code ACTION} if no resource type</li>
 *   <li>{@code ACTION@RESOURCE} if resource type present</li>
 * </ul>
 *
 * <p>Examples:
 * <pre>{@code
 * PermissionKey INVITE_MEMBER = PermissionKey.of("INVITE", ResourceType.MEMBER);
 * PermissionKey WITHDRAW_BANK = PermissionKey.of("WITHDRAW", ResourceType.BANK);
 * PermissionKey PING          = PermissionKey.of("PING");
 * }</pre>
 */
public final class PermissionKey {

    private final String action;
    private final ResourceType resourceType; // nullable

    private PermissionKey(String action, ResourceType resourceType) {
        this.action = normalizeAction(action);
        this.resourceType = resourceType; // optional
    }

    /**
     * Creates a permission key with an action and resource type.
     *
     * @param action       action token (must not be blank; recommended UPPER_SNAKE_CASE)
     * @param resourceType resource category (must not be null)
     * @return permission key
     */
    public static PermissionKey of(String action, ResourceType resourceType) {
        Objects.requireNonNull(resourceType, "resourceType");
        return new PermissionKey(action, resourceType);
    }

    /**
     * Creates a permission key with an action only (no resource category).
     *
     * @param action action token (must not be blank; recommended UPPER_SNAKE_CASE)
     * @return permission key
     */
    public static PermissionKey of(String action) {
        return new PermissionKey(action, null);
    }

    /**
     * Returns the action token.
     */
    public String action() {
        return action;
    }

    /**
     * Returns the resource type if present.
     */
    public Optional<ResourceType> resourceType() {
        return Optional.ofNullable(resourceType);
    }

    /**
     * Returns a stable canonical string suitable for DB keys/caches/logs/network.
     *
     * <p>Format:
     * <ul>
     *   <li>{@code ACTION}</li>
     *   <li>{@code ACTION@RESOURCE}</li>
     * </ul>
     *
     * @return stable string key
     */
    public String stableKey() {
        return resourceType == null ? action : action + "@" + resourceType.name();
    }

    /**
     * Parses a {@code PermissionKey} from {@link #stableKey()} format.
     *
     * <p>Accepted formats:
     * <ul>
     *   <li>{@code ACTION}</li>
     *   <li>{@code ACTION@RESOURCE}</li>
     * </ul>
     *
     * @param stableKey stable key string
     * @return parsed permission key
     * @throws IllegalArgumentException if format is invalid or resource type is unknown
     */
    public static PermissionKey parse(String stableKey) {
        Objects.requireNonNull(stableKey, "stableKey");
        String s = stableKey.trim();
        if (s.isEmpty()) throw new IllegalArgumentException("stableKey cannot be blank");

        int at = s.indexOf('@');
        if (at < 0) {
            return PermissionKey.of(s);
        }

        if (at == 0 || at == s.length() - 1) {
            throw new IllegalArgumentException("PermissionKey must be 'ACTION' or 'ACTION@RESOURCE', got: " + s);
        }

        if (s.indexOf('@', at + 1) >= 0) {
            throw new IllegalArgumentException("PermissionKey must contain at most one '@', got: " + s);
        }

        String action = s.substring(0, at).trim();
        String rt = s.substring(at + 1).trim();

        if (action.isEmpty() || rt.isEmpty()) {
            throw new IllegalArgumentException("PermissionKey must be 'ACTION' or 'ACTION@RESOURCE', got: " + s);
        }

        ResourceType resourceType;
        try {
            resourceType = ResourceType.valueOf(rt);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown ResourceType '" + rt + "' in PermissionKey: " + s, e);
        }

        return PermissionKey.of(action, resourceType);
    }

    @Override
    public String toString() {
        return stableKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionKey other)) return false;
        return action.equals(other.action) && resourceType == other.resourceType;
    }

    @Override
    public int hashCode() {
        return 31 * action.hashCode() + (resourceType == null ? 0 : resourceType.hashCode());
    }

    private static String normalizeAction(String action) {
        Objects.requireNonNull(action, "action");
        String a = action.trim();
        if (a.isEmpty()) throw new IllegalArgumentException("action cannot be blank");
        if (a.indexOf('@') >= 0) throw new IllegalArgumentException("action cannot contain '@': " + a);
        return a;
    }
}
