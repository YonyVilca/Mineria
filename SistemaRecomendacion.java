import java.io.*;
import java.util.*;

public class SistemaRecomendacion {

    public static void main(String[] args) {
        // Leer datos desde el archivo CSV
        Map<String, Map<String, Double>> calificaciones = leerDatosDesdeCSV("/workspaces/Mineria/lab01/Movie_Ratings.csv");

        // Obtener la lista de usuarios del mapa de calificaciones
        List<String> usuarios = new ArrayList<>(calificaciones.keySet());

        // Imprimir la tabla de distancias de Manhattan y Euclidiana
        System.out.printf("%-15s%-15s%-20s%-20s%n", "Usuario 1", "Usuario 2", "Distancia Manhattan", "Distancia Euclidiana");
        System.out.println("----------------------------------------------------------------------------");

        // Iterar sobre todos los pares de usuarios
        for (int i = 0; i < usuarios.size(); i++) {
            for (int j = i + 1; j < usuarios.size(); j++) {
                String usuario1 = usuarios.get(i);
                String usuario2 = usuarios.get(j);

                // Calcular la distancia de Manhattan
                double distanciaManhattan = calcularDistanciaManhattan(calificaciones, usuario1, usuario2);

                // Calcular la distancia Euclidiana
                double distanciaEuclidiana = calcularDistanciaEuclidiana(calificaciones, usuario1, usuario2);

                // Imprimir el resultado en formato de tabla
                System.out.printf("%-15s%-15s%-20.2f%-20.2f%n", usuario1, usuario2, distanciaManhattan, distanciaEuclidiana);
            }
        }
    }

    // Método para leer los datos desde un archivo CSV y almacenarlos en un HashMap
    private static Map<String, Map<String, Double>> leerDatosDesdeCSV(String archivoCSV) {
        Map<String, Map<String, Double>> calificaciones = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea = br.readLine(); // Leer la primera línea (cabecera)
            String[] usuarios = linea.split(","); // Nombres de los usuarios

            // Leer cada línea siguiente que contiene los productos y sus calificaciones
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                String producto = valores[0].trim(); // El nombre del producto está en la primera columna

                // Para cada usuario, agregamos sus calificaciones de este producto
                for (int i = 1; i < valores.length; i++) {
                    String usuario = usuarios[i].trim();
                    String valor = valores[i].trim();
                    
                    if (!valor.isEmpty()) {
                        try {
                            double calificacion = Double.parseDouble(valor);
                            calificaciones.putIfAbsent(usuario, new HashMap<>());
                            calificaciones.get(usuario).put(producto, calificacion);
                        } catch (NumberFormatException e) {
                            System.out.println("Error en formato numérico para el usuario " + usuario + " en el producto " + producto);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return calificaciones;
    }

    // Método para calcular la distancia de Manhattan entre dos usuarios
    private static double calcularDistanciaManhattan(Map<String, Map<String, Double>> calificaciones, String usuario1, String usuario2) {
        double distancia = 0.0;

        // Obtener las calificaciones de ambos usuarios
        Map<String, Double> calificacionesUsuario1 = calificaciones.get(usuario1);
        Map<String, Double> calificacionesUsuario2 = calificaciones.get(usuario2);

        // Iteramos sobre los productos que ambos usuarios han calificado
        for (String producto : calificacionesUsuario1.keySet()) {
            if (calificacionesUsuario2.containsKey(producto)) {
                distancia += Math.abs(calificacionesUsuario1.get(producto) - calificacionesUsuario2.get(producto));
            }
        }

        return distancia;
    }

    // Método para calcular la distancia Euclidiana entre dos usuarios
    private static double calcularDistanciaEuclidiana(Map<String, Map<String, Double>> calificaciones, String usuario1, String usuario2) {
        double sumaCuadrados = 0.0;

        // Obtener las calificaciones de ambos usuarios
        Map<String, Double> calificacionesUsuario1 = calificaciones.get(usuario1);
        Map<String, Double> calificacionesUsuario2 = calificaciones.get(usuario2);

        // Iteramos sobre los productos que ambos usuarios han calificado
        for (String producto : calificacionesUsuario1.keySet()) {
            if (calificacionesUsuario2.containsKey(producto)) {
                sumaCuadrados += Math.pow(calificacionesUsuario1.get(producto) - calificacionesUsuario2.get(producto), 2);
            }
        }

        // Devolvemos la raíz cuadrada de la suma de los cuadrados
        return Math.sqrt(sumaCuadrados);
    }
}
