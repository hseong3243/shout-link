package com.seong.shoutlink.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class BaseIntegrationTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }

    protected void persist(Object entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(entity);
        transaction.commit();
    }
}
