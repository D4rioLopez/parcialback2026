package org.example;


import org.example.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

public class AppTest {
    @BeforeEach
    public void init(){
        EntityManagerUtil.init("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }
    @AfterEach
    public void close(){ EntityManagerUtil.close(); }

    @Test
    public void smokeTest(){
        EntityManager em = EntityManagerUtil.getEntityManager();
        Assertions.assertNotNull(em);
        em.close();
    }
}
