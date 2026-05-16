package peonni.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("peonniPU");

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public static void fechar() {
        factory.close();
    }
}
