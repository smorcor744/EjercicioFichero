package org.example

import java.nio.file.Files
import java.nio.file.Path

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val raiz = Path.of("src","main", "resources","cotizacion.csv")
    val destino = Path.of("src","main", "resources","resultado.csv")

    val leer = leerCotizaciones(raiz)
    val calcular = calcularEstadisticas(leer)
    escribirEstadisticas(destino,calcular)


}

fun calcularEstadisticas(datos: Map<String, List<String>>): Map<String, Map<String, Double>> {
    val estadisticas = mutableMapOf<String, Map<String, Double>>()
    datos.forEach { (columna, valores) ->
        if (columna != "Nombre") {
            val valoresNumericos = valores.map { it.toDouble() }
            val minimo = valoresNumericos.minOrNull() ?: 0.0
            val maximo = valoresNumericos.maxOrNull() ?: 0.0
            val media = valoresNumericos.average()
            estadisticas[columna] = mapOf("Mínimo" to minimo, "Máximo" to maximo, "Media" to media)
        }
    }
    return estadisticas
}
fun escribirEstadisticas(path: Path, estadisticas: Map<String, Map<String, Double>>) {
    val bw = Files.newBufferedWriter(path)
    bw.use { writer ->
        writer.write("Columna,Mínimo,Máximo,Media\n")
        estadisticas.forEach { (columna, valores) ->
            writer.write("${columna},${valores["Mínimo"]},${valores["Máximo"]},${valores["Media"]}\n")
        }
    }
}

fun leerCotizaciones(path: Path): Map<String, List<String>>{
    val datos = mutableMapOf<String, MutableList<String>>()
    val br = Files.newBufferedReader(path)
    br.use {reader ->
        val encabezados = reader.readLine().split(";")
        encabezados.forEach { datos[it] = mutableListOf() }
        reader.forEachLine { linea ->
            val valores = linea.split(";")
            valores.forEachIndexed { index, valor ->
                datos[encabezados[index]]?.add(valor)
            }
        }
    }
    return datos
}