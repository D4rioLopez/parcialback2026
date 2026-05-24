package org.example.repository;
import org.example.model.Liquidacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Tarjeta;

public class LiquidacionRepository {
    private final EntityManager em;
    public LiquidacionRepository(EntityManager em){ this.em = em; }

    public Liquidacion findByTarjetaNumeroMesAnio(String numero, int mes, int anio){
        TypedQuery<Liquidacion> q = em.createQuery(
                "SELECT l FROM Liquidacion l WHERE l.tarjeta.numero = :numero AND l.mes = :mes AND l.anio = :anio", Liquidacion.class);
        q.setParameter("numero", numero);
        q.setParameter("mes", mes);
        q.setParameter("anio", anio);
        return q.getResultStream().findFirst().orElse(null);
    }

    public void save(Liquidacion l){
        em.getTransaction().begin();
        if (l.getId() == null) em.persist(l); else em.merge(l);
        em.getTransaction().commit();
    }

    public Liquidacion calcularLiquidacion(Long idTarjeta, int mes, int anio) {
        // 1. Buscar la tarjeta
        Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);
        if (tarjeta == null) {
            return null;
        }

        // 2. Calcular total de consumos del mes/año
        Double totalConsumos = em.createQuery(
                        "SELECT SUM(c.monto) FROM Consumo c WHERE c.tarjeta.id = :id AND c.mes = :mes AND c.anio = :anio",
                        Double.class)
                .setParameter("id", idTarjeta)
                .setParameter("mes", mes)
                .setParameter("anio", anio)
                .getSingleResult();

        if (totalConsumos == null) {
            return null; // No hay consumos
        }

        // 3. Calcular impuestos (ejemplo: 21%)
        Double totalImpuestos = totalConsumos * 0.21;

        // 4. Calcular descuentos (ejemplo: 0% - podés cambiar esto)
        Double totalDescuentos = 0.0;

        // 5. Calcular total a pagar
        Double totalAPagar = totalConsumos + totalImpuestos - totalDescuentos;

        // 6. Crear y retornar la liquidación
        Liquidacion liq = new Liquidacion();
        liq.setTarjeta(tarjeta);
        liq.setMes(mes);
        liq.setAnio(anio);
        liq.setTotalConsumos(totalConsumos);
        liq.setTotalImpuestos(totalImpuestos);
        liq.setTotaDescuestos(totalDescuentos); // Nota: tiene typo en la entity
        liq.setTotalAPagar(totalAPagar);

        return liq;
    }
}

