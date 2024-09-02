import pandas as pd
import numpy as np

# Cargar el archivo CSV
file_path = '/workspaces/Mineria/lab01/dimen.csv'
df = pd.read_csv(file_path)

# Función para calcular distancia de Manhattan
def manhattan_distance(vec1, vec2):
    return np.sum(np.abs(vec1 - vec2))

# Función para calcular distancia Euclidiana
def euclidean_distance(vec1, vec2):
    return np.sqrt(np.sum((vec1 - vec2) ** 2))

# Crear una tabla de comparaciones de distancias
def calculate_distances(df):
    names = df.columns[2:]  # Suponiendo que la primera columna es el nombre
    comparison_results = []

    for i, name1 in enumerate(names):
        for j, name2 in enumerate(names):
            if i < j:
                vec1 = df[name1].values
                vec2 = df[name2].values
                manhattan_dist = manhattan_distance(vec1, vec2)
                euclidean_dist = euclidean_distance(vec1, vec2)
                comparison_results.append({
                    'Person 1': name1,
                    'Person 2': name2,
                    'Manhattan Distance': manhattan_dist,
                    'Euclidean Distance': euclidean_dist
                })

    return pd.DataFrame(comparison_results)

# Calcular distancias y crear tabla
distance_table = calculate_distances(df)

# Mostrar la tabla en la consola
print(distance_table)

# Guardar la tabla como un archivo CSV
distance_table.to_csv('/workspaces/Mineria/lab01/distance_comparison.csv', index=False)
