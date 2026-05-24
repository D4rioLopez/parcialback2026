package org.example.repository;

import org.example.model.Consumo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ConsumoRepository {
    private final EntityManager em;
    public ConsumoRepository(EntityManager em){ this.em = em; }

    public Consumo findById(Long id){ return em.find(Consumo.class, id); }

    public void save(Consumo c){
        em.getTransaction().begin();
        if (c.getId() == null) em.persist(c); else em.merge(c);
        em.getTransaction().commit();
    }

    public void delete(Consumo c){
        em.getTransaction().begin();
        em.remove(em.contains(c) ? c : em.merge(c));
        em.getTransaction().commit();
    }

    public List<Consumo> consumosPorTarjetaMesAnio(Long idTarjeta, int mes, int anio){
        TypedQuery<Consumo> q = em.createQuery(
                "SELECT c FROM Consumo c WHERE c.tarjeta.id = :idTarjeta AND c.mes = :mes AND c.anio = :anio", Consumo.class);
        q.setParameter("idTarjeta", idTarjeta);
        q.setParameter("mes", mes);
        q.setParameter("anio", anio);
        return q.getResultList();
    }
}

