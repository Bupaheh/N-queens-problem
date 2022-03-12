package matlogTask02

import org.sat4j.core.VecInt
import org.sat4j.minisat.SolverFactory
import org.sat4j.specs.ISolver

fun toId(pos: Pair<Int, Int>, size: Int) = pos.first * size + pos.second + 1
fun toPos(id: Int, size: Int) = Pair((id - 1) / size, (id - 1) % size)

fun atLeastOneInEachRow(solver: ISolver, size: Int) {
    for (row in (0 until size)) {
        val rowLiterals = (0 until size)
            .map { col -> Pair(row, col) }
            .map { pos -> toId(pos, size) }
            .toIntArray()

        solver.addAtLeast(VecInt(rowLiterals), 1)
    }
}

fun atMostOneInEachCol(solver: ISolver, size: Int) {
    for (col in (0 until size)) {
        val colLiterals = (0 until size)
            .map { row -> Pair(row, col) }
            .map { pos -> toId(pos, size) }
            .toIntArray()

        solver.addAtMost(VecInt(colLiterals), 1)
    }
}

fun atMostOneInEachDiag(solver: ISolver, size: Int) {
    val diag1 = (0 until size).map { ind -> Pair(ind, ind) }
    val diag2 = (0 until size).map { ind -> Pair(ind, size - ind - 1) }
    val diags = listOf(diag1, diag2)

    diags.forEach { diag ->
        for (shift in -(size - 1) until size) {
            val diagLiterals = diag.map { (row, col) ->
                    Pair(row, col + shift)
                }.filter { (_, col) ->
                    col in 0 until size
                }.map { pos -> toId(pos, size) }
                .toIntArray()

            solver.addAtMost(VecInt(diagLiterals), 1)
        }
    }
}

fun prettyPrinter(model: IntArray, size: Int) {
    val arr = Array(size) { Array(size) { '_' } }
    model.filter { it > 0 }
        .map { id -> toPos(id, size) }
        .forEach { (row, col) -> arr[row][col] = '+' }
    println(arr.joinToString( separator = "\n", transform = { it.joinToString("") }))
}


fun main() {
    val size = readLine()?.toInt() ?: -1

    if (size <= 0) {
        println("Invalid input")
        return
    }

    val solver = SolverFactory.newDefault()
    solver.newVar(size * size)

    atLeastOneInEachRow(solver, size)
    atMostOneInEachCol(solver, size)
    atMostOneInEachDiag(solver, size)

    if (solver.isSatisfiable) {
        val model = solver.model()
        model.filter { it > 0 }
            .forEach { id ->
                val (row, col) = toPos(id, size)
                println("(${row + 1}, ${col + 1})")
            }
//        prettyPrinter(model, size)
    } else {
        println("No solution")
    }
}