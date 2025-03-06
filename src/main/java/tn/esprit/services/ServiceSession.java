package tn.esprit.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceSession {

    private static final Map<String, String> sessionStore = new HashMap<>();
    private static final int SESSION_TIMEOUT = 30 * 60 * 1000;


    public static String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, userId);
        return sessionId;
    }


    public static boolean isValidSession(String sessionId) {
        return sessionStore.containsKey(sessionId);
    }


    public static String getUserIdFromSession(String sessionId) {
        return sessionStore.get(sessionId);
    }


    public static void invalidateSession(String sessionId) {
        sessionStore.remove(sessionId);
    }
}
