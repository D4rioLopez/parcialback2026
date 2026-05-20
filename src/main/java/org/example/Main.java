/* Lopez Dario Leg 26413
 https://labsys.frc.utn.edu.ar/gitlab/lopez26413
 RiverPlate2023
 github 26413@sistemas.frc.utn.edu.ar
 D4r10l0p3z2025
 */
package org.example;


import org.example.entity.*;
import org.example.repository.*;
import org.example.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Elija modo H2: 1) memoria  2) archivo (./data/tarjetas-db)");
        Scanner sc = new Scanner(System.in);
        String opt = sc.nextLine().trim();
        String jdbcUrl;
        if ("2".equals(opt)) {
            jdbcUrl = "jdbc:h2:file:./data/tarjetas-db;AUTO_SERVER=TRUE";
        } else {
            jdbcUrl = "jdbc:h2:mem:tarjetas;DB_CLOSE_DELAY=-1";
        }

        EntityManagerUtil.init(jdbcUrl);
        EntityManager em = EntityManagerUtil.getEntityManager();

        // demo simple: listar tarjetas y consumos de marzo 2026 para tarjeta 1
        TarjetaRepository tr = new TarjetaRepository(em);
        ConsumoRepository cr = new ConsumoRepository(em);
        LiquidacionRepository lr = new LiquidacionRepository(em);

        List<Tarjeta> tarjetas = tr.findAll();
        System.out.println("Tarjetas en BD:");
        tarjetas.forEach(t -> System.out.println(t.getNumero() +  " - " + t.getTitular()));

        System.out.println("\nConsumos tarjeta id=1 mes=3 anio=2026:");
        List<Consumo> consumos = cr.consumosPorTarjetaMesAnio(1L, 3, 2026);
        consumos.forEach(c -> System.out.println(c.getId() + " " + c.getMonto() + " " + c.getMoneda() + " rubro:" + c.getRubro()));

        /* consumos.forEach(c2 -> {
            if (c2.getMonto() == 0) {
            System.out.println(c2.getId() + " " + c2.getMoneda() + " rubro:" + c2.getRubro());
           }
         });
        */

        System.out.println("\nLiquidacion por numero '4111111111111111' mes=3 anio=2026:");
        System.out.println(lr.findByTarjetaNumeroMesAnio("4111111111111111",3,2026));

        em.close();
        EntityManagerUtil.close();
    }
}

