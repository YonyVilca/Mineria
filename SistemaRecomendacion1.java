import java.io.*;
import java.util.*;

public class SistemaRecomendacion1 {

    public static void main(String[] args) {
        // Leer datos desde el archivo CSV
        Map<String, Map<String, Double>> calificaciones = leerDatosDesdeCSV("/workspaces/Mineria/lab01/Movie_Ratings.csv");

        // Obtener la lista de usuarios del mapa de calificaciones
        List<String> usuarios = new ArrayList<>(calificaciones.keySet());

        // Imprimir la tabla de correlación de Pearson y similitud del coseno
        System.out.printf("%-15s%-15s%-20s%-20s%n", "Usuario 1", "Usuario 2", "Correlación Pearson", "Similitud Coseno");
        System.out.println("----------------------------------------------------------------------------");

        // Iterar sobre todos los pares de usuarios
        for (int i = 0; i < usuarios.size(); i++) {
            for (int j = i + 1; j < usuarios.size(); j++) {
                String usuario1 = usuarios.get(i);
                String usuario2 = usuarios.get(j);

                // Calcular la Correlación de Pearson
                double correlacionPearson = calcularCorrelacionPearson(calificaciones, usuario1, usuario2);

                // Calcular la Similitud del Coseno
                double similitudCoseno = calcularSimilitudCoseno(calificaciones, usuario1, usuario2);

                // Imprimir el resultado en formato de tabla
                System.out.printf("%-15s%-15s%-20.2f%-20.2f%n", usuario1, usuario2, correlacionPearson, similitudCoseno);
            }
        }
    }

    // Método para calcular la correlación de Pearson entre dos usuarios
    private static double calcularCorrelacionPearson(Map<String, Map<String, Double>> calificaciones, String usuario1, String usuario2) {
        Map<String, Double> calificacionesUsuario1 = calificaciones.get(usuario1);
        Map<String, Double> calificacionesUsuario2 = calificaciones.get(usuario2);

        double suma1 = 0, suma2 = 0, suma1Cuadrado = 0, suma2Cuadrado = 0, sumaProductos = 0;
        int n = 0;

        for (String producto : calificacionesUsuario1.keySet()) {
            if (calificacionesUsuario2.containsKey(producto)) {
                double cal1 = calificacionesUsuario1.get(producto);
                double cal2 = calificacionesUsuario2.get(producto);

                suma1 += cal1;
                suma2 += cal2;
                suma1Cuadrado += Math.pow(cal1, 2);
                suma2Cuadrado += Math.pow(cal2, 2);
                sumaProductos += cal1 * cal2;
                n++;
            }
        }

        if (n == 0) return 0;

        double numerador = sumaProductos - (suma1 * suma2 / n);
        double denominador = Math.sqrt((suma1Cuadrado - Math.pow(suma1, 2) / n) * (suma2Cuadrado - Math.pow(suma2, 2) / n));

        return (denominador == 0) ? 0 : numerador / denominador;
    }

    // Método para calcular la similitud del coseno entre dos usuarios
    private static double calcularSimilitudCoseno(Map<String, Map<String, Double>> calificaciones, String usuario1, String usuario2) {
        Map<String, Double> calificacionesUsuario1 = calificaciones.get(usuario1);
        Map<String, Double> calificacionesUsuario2 = calificaciones.get(usuario2);

        double productoPunto = 0, magnitudUsuario1 = 0, magnitudUsuario2 = 0;

        for (String producto : calificacionesUsuario1.keySet()) {
            if (calificacionesUsuario2.containsKey(producto)) {
                double cal1 = calificacionesUsuario1.get(producto);
                double cal2 = calificacionesUsuario2.get(producto);

                productoPunto += cal1 * cal2;
                magnitudUsuario1 += Math.pow(cal1, 2);
                magnitudUsuario2 += Math.pow(cal2, 2);
            }
        }

        double magnitud = Math.sqrt(magnitudUsuario1) * Math.sqrt(magnitudUsuario2);

        return (magnitud == 0) ? 0 : productoPunto / magnitud;
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
                String producto = valores[0]; // El nombre del producto está en la primera columna

                // Para cada usuario, agregamos sus calificaciones de este producto
                for (int i = 1; i < valores.length; i++) {
                    String usuario = usuarios[i];
                    if (!valores[i].isEmpty()) {
                        double calificacion = Double.parseDouble(valores[i]);

                        // Si el usuario no existe en el mapa, lo agregamos
                        calificaciones.putIfAbsent(usuario, new HashMap<>());
                        // Agregamos la calificación del producto al usuario
                        calificaciones.get(usuario).put(producto, calificacion);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return calificaciones;
    }
}
