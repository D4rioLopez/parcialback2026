package org.example.repository;

import org.example.entity.Cotizacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

public class CotizacionRepository {
    private final EntityManager em;
    public CotizacionRepository(EntityManager em){ this.em = em; }

    public Cotizacion findById(Long id){
        return em.find(Cotizacion.class, id); }

    public void save(Cotizacion c){
        em.getTransaction().begin();
        if (c.getId() == null) em.persist(c); else em.merge(c);
        em.getTransaction().commit();
    }

    public void delete(Cotizacion c){
        em.getTransaction().begin();
        em.remove(em.contains(c) ? c : em.merge(c));
        em.getTransaction().commit();
    }
    //buscar solo por moneda
    public List<Cotizacion> cotizacionDeMoneda(String moneda){
        TypedQuery<Cotizacion> c = em.createQuery(
                "SELECT c FROM Cotizacion c WHERE c.moneda = :moneda", Cotizacion.class);
        c.setParameter("moneda", moneda);
        return c.getResultList();
    }
    //buscar por moneda y tasa de cambio
    public List<Cotizacion> cotizacionDeMonedaYTasa(String moneda, BigDecimal tasaDeCambio){
        TypedQuery<Cotizacion> c = em.createQuery(
                "SELECT c FROM Cotizacion c WHERE c.moneda = :moneda AND c.tasaCambio = :tasaDeCambio", Cotizacion.class);
        c.setParameter("moneda", moneda);
        c.setParameter("tasaCambio", tasaDeCambio);
        return c.getResultList();
    }


}