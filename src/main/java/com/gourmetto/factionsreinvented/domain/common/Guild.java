package com.gourmetto.factionsreinvented.domain.common;

/**
 * Guild is merely a convenience wrapper around a {@link Domain} to make the typical default usage easier.
 */
public class Guild extends Domain {

    /**
     * Contrary to the generic {@link Domain} class, we require the displayName arg to create a Guild.
     * @param displayName: the human-readable name of the Guild
     */
    public Guild(String displayName) {
        DomainId id = DomainId.root("guild", displayName);
        super(id);
    }
}
