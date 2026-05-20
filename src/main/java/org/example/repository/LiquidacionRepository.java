package org.example.repository;

import org.example.entity.Cotizacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class LiquidacionRepository {
    private final EntityManager em;
    public LiquidacionRepository(EntityManager em){ this.em = em; }

    public Cotizacion findByTarjetaNumeroMesAnio(String numero, int mes, int anio){
        TypedQuery<Cotizacion> q = em.createQuery(
                "SELECT l FROM Liquidacion l WHERE l.tarjeta.numero = :numero AND l.mes = :mes AND l.anio = :anio", Cotizacion.class);
        q.setParameter("numero", numero);
        q.setParameter("mes", mes);
        q.setParameter("anio", anio);
        return q.getResultStream().findFirst().orElse(null);
    }

    public void save(Cotizacion l){
        em.getTransaction().begin();
        if (l.getId() == null) em.persist(l); else em.merge(l);
        em.getTransaction().commit();
    }
}

