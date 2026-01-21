package com.gourmetto.factionsreinvented.auth.core.domain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable hierarchical identifier for authorization domains (guild/subguild/etc),
 * where each segment is identified by a UUID (stable) and may have a human display name (mutable).
 *
 * <p>Identity is based ONLY on (prefix, uuid) for each segment. Display names are metadata and do not
 * affect equality.
 */
public final class DomainId {

    /**
     * One node of a DomainId path.
     *
     * <p>Identity:
     * - prefix + uuid (displayName is not part of identity)
     *
     * <p>Example: prefix="sub", uuid=..., displayName="Alpha Team"
     */
    public static final class Segment {
        private final String prefix;
        private final UUID uuid;
        private final String displayName; // may be null/blank if unknown

        public Segment(String prefix, UUID uuid, String displayName) {
            this.prefix = requireValidPrefix(prefix);
            this.uuid = Objects.requireNonNull(uuid, "uuid");
            this.displayName = normalizeDisplayName(displayName);
        }

        public String prefix() { return prefix; }
        public UUID uuid() { return uuid; }

        /**
         * Human-friendly label for UI/logging. Not used for identity.
         */
        public Optional<String> displayName() { return Optional.ofNullable(displayName); }

        /**
         * Stable canonical token: "<prefix>:<uuid>"
         */
        public String token() {
            return prefix + ":" + uuid;
        }

        /**
         * Human-friendly token: "<prefix>:<displayName>" (falls back to uuid if name missing).
         * Not stable; do not store as DB key.
         */
        public String displayToken() {
            return prefix + ":" + (displayName != null ? displayName : uuid.toString());
        }

        @Override public String toString() { return token(); }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Segment other)) return false;
            return prefix.equals(other.prefix) && uuid.equals(other.uuid);
        }

        @Override
        public int hashCode() {
            return 31 * prefix.hashCode() + uuid.hashCode();
        }

        private static String requireValidPrefix(String prefix) {
            Objects.requireNonNull(prefix, "prefix");
            String p = prefix.trim();
            if (p.isEmpty()) throw new IllegalArgumentException("prefix cannot be blank");
            if (p.indexOf('/') >= 0 || p.indexOf(':') >= 0)
                throw new IllegalArgumentException("prefix cannot contain '/' or ':' : " + p);
            return p;
        }

        private static String normalizeDisplayName(String displayName) {
            if (displayName == null) return null;
            String d = displayName.trim();
            if (d.isEmpty()) return null;
            // allow spaces; but forbid separators that would break debug strings
            if (d.indexOf('/') >= 0) throw new IllegalArgumentException("displayName cannot contain '/'");
            return d;
        }

        /**
         * Parses a stable token "<prefix>:<uuid>" (no display name).
         */
        public static Segment parseToken(String token) {
            Objects.requireNonNull(token, "token");
            String s = token.trim();
            if (s.isEmpty()) throw new IllegalArgumentException("segment token cannot be blank");

            int colon = s.indexOf(':');
            if (colon <= 0 || colon == s.length() - 1)
                throw new IllegalArgumentException("segment token must be '<prefix>:<uuid>', got: " + s);

            if (s.indexOf(':', colon + 1) >= 0)
                throw new IllegalArgumentException("segment token must contain only one ':', got: " + s);

            String prefix = s.substring(0, colon).trim();
            String uuidStr = s.substring(colon + 1).trim();
            UUID uuid = UUID.fromString(uuidStr);
            return new Segment(prefix, uuid, null);
        }
    }

    private final List<Segment> segments;

    /**
     * Constructor #1: create from optional parent + leaf segment.
     */
    public DomainId(DomainId parent, Segment leaf) {
        Objects.requireNonNull(leaf, "leaf");
        if (parent == null) {
            this.segments = List.of(leaf);
        } else {
            List<Segment> tmp = new ArrayList<>(parent.segments.size() + 1);
            tmp.addAll(parent.segments);
            tmp.add(leaf);
            this.segments = List.copyOf(tmp);
        }
    }

    /**
     * Constructor #2: parse a nested stable string "prefix:uuid/prefix:uuid/..."
     */
    public DomainId(String nestedStable) {
        this(parse(nestedStable).segments);
    }

    private DomainId(List<Segment> segments) {
        Objects.requireNonNull(segments, "segments");
        if (segments.isEmpty()) throw new IllegalArgumentException("DomainId must have at least one segment");
        this.segments = List.copyOf(segments);
    }

    /**
     * Parse stable nested form "prefix:uuid/prefix:uuid/..."
     */
    public static DomainId parse(String nestedStable) {
        Objects.requireNonNull(nestedStable, "nestedStable");
        String s = nestedStable.trim();
        if (s.isEmpty()) throw new IllegalArgumentException("DomainId cannot be blank");

        String[] parts = s.split("/");
        List<Segment> segs = new ArrayList<>(parts.length);
        for (String part : parts) {
            segs.add(Segment.parseToken(part));
        }
        return new DomainId(segs);
    }

    /**
     * Convenience root factory.
     * The UUID does not need to be provided.
     */
    public static DomainId root(String prefix, String displayName) {
        UUID uuid = UUID.randomUUID();
        return new DomainId(null, new Segment(prefix, uuid, displayName));
    }

    /**
     * Append a child segment (UUID identity + optional display name).
     * The UUID does not need to be provided.
     */
    public DomainId child(String prefix, String displayName) {
        UUID uuid = UUID.randomUUID();
        return new DomainId(this, new Segment(prefix, uuid, displayName));
    }

    public boolean isRoot() { return segments.size() == 1; }

    public List<Segment> segments() { return segments; }

    public Segment leaf() { return segments.get(segments.size() - 1); }

    public Optional<DomainId> parent() {
        if (segments.size() <= 1) return Optional.empty();
        return Optional.of(new DomainId(segments.subList(0, segments.size() - 1)));
    }

    /**
     * Canonical stable serialization using UUIDs only.
     * Suitable for DB keys, caches, network.
     *
     * Example: "guild:550e8400-e29b-41d4-a716-446655440000/sub:..."
     */
    @Override
    public String toString() {
        return segments.stream().map(Segment::token).collect(Collectors.joining("/"));
    }

    /**
     * Human-friendly display path. Not stable (names can change).
     *
     * Example: "guild:MyGuild/sub:Alpha Team/sub:Builders"
     */
    public String toDisplayString() {
        return segments.stream().map(Segment::displayToken).collect(Collectors.joining("/"));
    }

    /**
     * Human-friendly display-name of the leaf. Not stable (names can change).
     *
     * Example: "MyGuild"
     */
    public String displayName() {
        return this.leaf().displayName;
    }

    /**
     * Only the display names (missing names fall back to UUID).
     * Example: ["MyGuild", "Alpha Team", "Builders"]
     */
    public List<String> displayNames() {
        List<String> out = new ArrayList<>(segments.size());
        for (Segment s : segments) {
            out.add(s.displayName().orElse(s.uuid().toString()));
        }
        return Collections.unmodifiableList(out);
    }

    /**
     * Identity is defined by ordered (prefix, uuid) pairs, not display names.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainId other)) return false;
        return segments.equals(other.segments);
    }

    @Override
    public int hashCode() {
        return segments.hashCode();
    }
}
