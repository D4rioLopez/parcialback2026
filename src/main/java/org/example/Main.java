/* Lopez Dario Leg 26413
 https://labsys.frc.utn.edu.ar/gitlab/lopez26413
 RiverPlate2023
 github 26413@sistemas.frc.utn.edu.ar
 D4r10l0p3z2025
 */
package org.example;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.*;
import org.example.repository.*;
import org.example.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Metodo para ejecutar archivos SQL
    public static void ejecutarSQL(EntityManager em, String archivo) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(archivo)))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            String[] sentencias = sb.toString().split(";");
            em.getTransaction().begin();
            for (String sql : sentencias) {
                if (!sql.trim().isEmpty() && !sql.trim().startsWith("--")) {
                    em.createNativeQuery(sql).executeUpdate();
                }
            }
            em.getTransaction().commit();
            System.out.println(archivo + " ejecutado correctamente");
        } catch (Exception e) {
            System.out.println("Error ejecutando " + archivo + ": " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        // ...
        System.out.println("Elija modo H2: 1) memoria  2) archivo (./data/tarjetas-db)");
        Scanner sc = new Scanner(System.in);
        String opt = sc.nextLine().trim();
        String jdbcUrl;
        if ("2".equals(opt)) {
            jdbcUrl = "jdbc:h2:file:./data/tarjetas-db;AUTO_SERVER=TRUE";
        } else {
            jdbcUrl = "jdbc:h2:mem:tarjetas;DB_CLOSE_DELAY=-1";
        }
        // Inicializar EntityManagerUtil con la URL elegida
        EntityManagerUtil.init(jdbcUrl);
        var em = EntityManagerUtil.getEntityManager();

        // Ejecutar los scripts SQL
        ejecutarSQL(em, "schema.sql");
        ejecutarSQL(em, "data.sql");

        // demo simple: listar tarjetas y consumos de marzo 2026 para tarjeta 1
        TarjetaRepository tr = new TarjetaRepository(em);
        ConsumoRepository cr = new ConsumoRepository(em);
        LiquidacionRepository lr = new LiquidacionRepository(em);

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Listar tarjetas registradas");
            System.out.println("2. Listar los consumos de usuario tarjeta X en marzo 2026");
            System.out.println("3. Listar las liquidaciones por un usuario específico y mes/año");
            System.out.println("4. Listar las tarjetas y consumos por un usuario específico");
            System.out.println("5. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    try {
                        // Crear las tablas
                        List<Tarjeta> tarjetas = tr.findAll();

                        if (tarjetas.isEmpty()) {
                            System.out.println("No hay tarjetas registradas ...");
                        } else {
                            System.out.println("Tarjetas registradas: ");
                            tarjetas.forEach(t -> System.out.println(t.getNumero() + " - " + t.getTitular()));
                        }
                    } catch (Exception e) {
                        System.out.println("Error " + e.getMessage());
                    }
                    System.out.print("Presione Enter para continuar...");
                    sc.nextLine();
                    break;
                case 2: //2. Listar los consumos de un usuario específico en un mes determinad
                    System.out.print("Ingrese número de tarjeta: ");
                    String numeroTarjeta2 = sc.nextLine();

                    // Primero buscar la tarjeta por número
                    Tarjeta tarjeta = tr.findByNumero(numeroTarjeta2);

                    if (tarjeta == null) {
                        System.out.println("No existe tarjeta con ese número");
                    } else { List<Consumo> consumos = cr.consumosPorTarjetaMesAnio(tarjeta.getId(), 5, 2026);
                            System.out.println("\nLos consumos de la tarjeta " + numeroTarjeta2 + " en mayo de  2026 son:");
                            consumos.forEach(consumo -> System.out.println("Monto: " + consumo.getMonto()));
                    }
                    System.out.print("Presione Enter para continuar...");
                    sc.nextLine();
                    break;
                case 3: //3. Listar las liquidaciones por un usuario específico y mes/año
                    System.out.print("Ingrese número de tarjeta: ");
                    String numeroTarjeta = sc.nextLine();
                    System.out.print("Ingrese mes: ");
                    int mes = sc.nextInt();
                    System.out.print("Ingrese año: ");
                    int anio = sc.nextInt();
                    sc.nextLine();

                    // Buscar tarjeta por número
                    Tarjeta tarjetaL = tr.findByNumero(numeroTarjeta);
                    if (tarjetaL == null) {
                        System.out.println("No existe tarjeta con ese número");
                    } else {
                        // Calcular liquidación
                        Liquidacion liq = lr.calcularLiquidacion(tarjetaL.getId(), mes, anio);

                        if (liq == null) {
                            System.out.println("No hay consumos para esa tarjeta en " + mes + "/" + anio);
                        } else {
                            System.out.println("\n=== LIQUIDACIÓN ===");
                            System.out.println("Tarjeta: " + liq.getTarjeta().getNumero());
                            System.out.println("Período: " + liq.getMes() + "/" + liq.getAnio());
                            System.out.println("Total Consumos: $" + liq.getTotalConsumos());
                            System.out.println("Impuestos (21%): $" + liq.getTotalImpuestos());
                            System.out.println("Descuentos: $" + liq.getTotaDescuestos());
                            System.out.println("TOTAL A PAGAR: $" + liq.getTotalAPagar());
                        }
                    }

                    System.out.print("Presione Enter para continuar...");
                    sc.nextLine();
                    break;
                case 4:
                    //**4. Listar las tarjetas y consumos por un usuario específico

                    System.out.print("Ingrese número de tarjeta: ");
                    String numeroTarjeta4 = sc.nextLine();

                    // Buscar tarjeta por número
                    Tarjeta tarjeta4 = tr.findByNumero(numeroTarjeta4);

                    if (tarjeta4 == null) {
                        System.out.println("No existe tarjeta con ese número");
                    } else {
                        // Obtener consumos de esa tarjeta (podés elegir mes/año o traer todos)
                        System.out.print("Ingrese mes: ");
                        int m = sc.nextInt();
                        System.out.print("Ingrese año: ");
                        int a = sc.nextInt();
                        List<Consumo> consumos4 = cr.consumosPorTarjetaMesAnio(tarjeta4.getId(),m, a);
                        sc.nextLine();

                        System.out.println("\n=== TARJETA Y CONSUMOS ===");
                        System.out.println("Tarjeta: " + tarjeta4.getNumero() + " - Titular: " + tarjeta4.getTitular());
                        System.out.println("Consumos en marzo 2026:");

                        if (consumos4.isEmpty()) {
                            System.out.println("No hay consumos registrados");
                        } else {
                            consumos4.forEach(c -> System.out.println(
                                    "  - Monto: $" + c.getMonto() +
                                            " | Moneda: " + c.getMoneda() +
                                            " | Rubro: " + c.getRubro() +
                                            " | Día: " + c.getDia()
                            ));
                        }

                        // O si querés TODOS los consumos sin importar el mes:
                         List<Consumo> consumos5 = em.createQuery(
                             "SELECT c FROM Consumo c WHERE c.tarjeta.id = :id", Consumo.class)
                             .setParameter("id", tarjeta4.getId())
                             .getResultList();
                    }

                    System.out.print("Presione Enter para continuar...");
                    sc.nextLine();
                    break;

                case 5:
                    em.close();
                    EntityManagerUtil.close();
                    System.out.println("¡Hasta luego!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

}

