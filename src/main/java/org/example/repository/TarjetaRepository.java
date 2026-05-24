package org.example.repository;

import org.example.model.Tarjeta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TarjetaRepository {
    private final EntityManager em;
    public TarjetaRepository(EntityManager em){ this.em = em; }

    public Tarjeta findByNumero(String numero){
        TypedQuery<Tarjeta> t = em.createQuery("SELECT t FROM Tarjeta t WHERE t.numero = :n", Tarjeta.class);
        t.setParameter("n", numero);
        return t.getResultStream().findFirst().orElse(null);
    }

    public List<Tarjeta> findAll(){
        return em.createQuery("SELECT t FROM Tarjeta t", Tarjeta.class).getResultList();
    }

    public void save(Tarjeta t){
        em.getTransaction().begin();
        if (t.getNumero() == null) em.persist(t); else em.merge(t);
        em.getTransaction().commit();
    }

    public void delete(Tarjeta t){
        em.getTransaction().begin();
        em.remove(em.contains(t) ? t : em.merge(t));
        em.getTransaction().commit();
    }

    public List<Tarjeta> tarjetasSinLiquidacion(String numero,int mes, int anio){
        TypedQuery<Tarjeta> t = em.createQuery(
                "SELECT t FROM Tarjeta t WHERE NOT EXISTS (" +
                        "SELECT 1 FROM Liquidacion l WHERE l.tarjeta = t  AND l.numero =:numero AND l.mes = :mes AND l.anio = :anio)", Tarjeta.class);
        t.setParameter("numero",numero);
        t.setParameter("mes", mes);
        t.setParameter("anio", anio);
        return t.getResultList();
    }
}
