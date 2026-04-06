package ru.otus.socialnetwork.service;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@Component
public class DialogIdGenerator {

    private static final UUID NAMESPACE = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");

    public UUID generate(UUID user1, UUID user2) {
        var min = user1.compareTo(user2) < 0 ? user1 : user2;
        var max = user1.compareTo(user2) < 0 ? user2 : user1;
        var name = min + ":" + max;
        return uuid5(NAMESPACE, name);
    }

    private UUID uuid5(UUID namespace, String name) {
        var nsBytes = toBytes(namespace);
        var nameBytes = name.getBytes(StandardCharsets.UTF_8);

        byte[] data = new byte[nsBytes.length + nameBytes.length];
        System.arraycopy(nsBytes, 0, data, 0, nsBytes.length);
        System.arraycopy(nameBytes, 0, data, nsBytes.length, nameBytes.length);

        try {
            var md = MessageDigest.getInstance("SHA-1");
            var hash = md.digest(data);

            hash[6] = (byte) ((hash[6] & 0x0f) | 0x50);
            hash[8] = (byte) ((hash[8] & 0x3f) | 0x80);

            long msb = 0;
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xff);
            }
            long lsb = 0;
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xff);
            }
            return new UUID(msb, lsb);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate UUID5", e);
        }
    }

    private byte[] toBytes(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] bytes = new byte[16];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (msb >>> (8 * (7 - i)));
        }
        for (int i = 0; i < 8; i++) {
            bytes[8 + i] = (byte) (lsb >>> (8 * (7 - i)));
        }
        return bytes;
    }
}
