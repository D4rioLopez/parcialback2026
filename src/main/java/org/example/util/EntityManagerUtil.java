package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerUtil {
    private static EntityManagerFactory emf;

    public static void init(String jdbcUrl) {
        if (emf != null && emf.isOpen()) emf.close();
        Map<String, Object> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", jdbcUrl);
        emf = Persistence.createEntityManagerFactory("tarjetasPU", props);
    }

    public static EntityManager getEntityManager() {
        if (emf == null) throw new IllegalStateException("EMF no inicializado");
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null) emf.close();
    }
}
