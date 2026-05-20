package org.example.repository;

import org.example.entity.Moneda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MonedaRepository {
    private final EntityManager em;

    public MonedaRepository(EntityManager em) {
        this.em = em;
    }

    public Moneda findByMoneda(String moneda) {
        return em.find(Moneda.class, moneda);
    }

    public List<Moneda> findAll() {
        TypedQuery<Moneda> mo = em.createQuery("SELECT m FROM Moneda m", Moneda.class);
        return mo.getResultList();
    }

    public void save(Moneda mo) {
        em.getTransaction().begin();
        try {
            if (mo.getMoneda() == null) {
                em.persist(mo);
            } else {
                em.merge(mo);
            }
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    public void delete(Moneda mo) {
        em.getTransaction().begin();
        try {
            em.remove(em.contains(mo) ? mo : em.merge(mo));
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    public Moneda findByTasaCambio(String tasaCambio) {
        TypedQuery<Moneda> mo = em.createQuery("SELECT m FROM Moneda m WHERE m.tasaCambio = :tasaCambio", Moneda.class);
        mo.setParameter("tasaCambio", tasaCambio);
        return mo.getResultStream().findFirst().orElse(null);
    }
}
