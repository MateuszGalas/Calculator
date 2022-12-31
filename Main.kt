package calculator

import java.util.Stack

fun String.isDigit(): Boolean {
    return this.matches("""-?\d+""".toRegex())
}

class Calculator {
    val variables = mutableMapOf<String, Int>()

    fun variable(variable: String): Boolean {
        return variables.containsKey(variable)
    }

    fun variableToDigit(variable: String): Int {
        return variables[variable]!!
    }


    private fun changeInfixToPostfix(list: MutableList<String>): MutableList<String> {
        val result = mutableListOf<String>()
        val operators = Stack<String>()
        for (i in list) {
            when {
                (variable(i)) -> result.add(variableToDigit(i).toString())
                i.isDigit() -> result.add(i)
                i == "(" -> {
                    operators.push(i)
                }

                i == ")" -> {
                    if (operators.isEmpty()) throw Exception()
                    for (j in operators.indices) {
                        if (operators.last() != "(") {
                            result.add(operators.pop())
                        } else {
                            operators.pop()
                            break
                        }
                    }
                }

                i == "*" || i == "/" -> {
                    if (operators.isEmpty() || operators.last() == "(") {
                        operators.push(i)
                    } else {
                        for (j in operators.indices) {
                            if (operators.last() == "*" || operators.last() == "/") {
                                result.add(operators.pop())
                            }
                        }
                        operators.push(i)
                        //operators.forEach { if (it != "(") result.add(operators.pop())}
                    }
                }

                i == "+" || i == "-" -> {
                    if (operators.isEmpty() || operators.last() == "(") {
                        operators.push(i)
                    } else {
                        for (j in operators.indices) {
                            if (operators.last() != "(") {
                                result.add(operators.pop())
                            }
                        }
                        operators.push(i)
                    }
                }
            }
        }
        while (operators.isNotEmpty()) result.add(operators.pop())
        if (result.contains("(") || result.contains(")")) throw Exception()

        return result
    }

    fun calculateExpression(input: String) {
        if (input.matches(""".*(\*{2,}|/{2,}).*""".toRegex())) throw Exception()
        val res = mutableListOf<String>()
        input.split(" ").toMutableList().forEach {
            if (!it.isDigit()) it.split("").forEach { res.add(it) } else res.add(it)
        }
        res.removeAll { it == " " || it == "" }
        if (res.count { it == "(" } != res.count { it == ")" }) throw Exception()

        val postFix = changeInfixToPostfix(res)
        val finalResult = Stack<Int>()
        for (i in postFix) {
            if (i.isDigit()) {
                finalResult.push(i.toInt())
            } else {
                when (i) {
                    "+" -> {
                        finalResult.push(finalResult.pop() + finalResult.pop())
                    }

                    "-" -> {
                        val a = finalResult.pop()
                        val b = finalResult.pop()
                        finalResult.push(b - a)
                    }

                    "/" -> {
                        val a = finalResult.pop()
                        val b = finalResult.pop()
                        finalResult.push(b / a)
                    }

                    "*" -> {
                        finalResult.push(finalResult.pop() * finalResult.pop())
                    }
                }
            }
        }
        println(finalResult.pop())
    }
}

fun main() {
    val calc = Calculator()

    while (true) {
        val input = readln().trim()

        when {
            input == "" -> {}
            input == "/exit" -> println("Bye!").also { return }
            input == "/help" -> {
                println(
                    "The program calculates the sum or subtraction of numbers, you can also mix them" +
                            "like number + number - number. Program works if there are more operators than needed" +
                            "number +++ number -- number"
                )
            }

            input.startsWith("/") -> {
                println("Unknown command")
            }

            input.matches("""(\(+)?(\d+|[a-zA-Z]+)\s+(\++|-+|/+|\*+)\s+\(*(\d+|[a-zA-Z]+)(\)+)?.*""".toRegex()) -> {
                try {
                    val s = input.replace("""\++""".toRegex(), "+")
                        .replace("""(--)+""".toRegex(), "+")
                        .replace("""(-\+|\+-)""".toRegex(), "-")

                    calc.calculateExpression(s)
                } catch (e: Exception) {
                    println("Invalid expression")
                }
            }

            input.matches("""[a-zA-Z]+""".toRegex()) -> {
                if (calc.variable(input)) {
                    println(calc.variableToDigit(input))
                } else {
                    println("Unknown variable")
                }
            }

            input.matches(""".*=.*""".toRegex()) -> {
                val variables = input.split("""(=)""".toRegex()).map { it.trim() }
                if (!variables.first().matches("""[a-zA-Z]+""".toRegex())) {
                    println("Invalid identifier")
                }
                if (!variables.last().matches("""(-?\d+|[a-zA-Z]+)""".toRegex()) || variables.size > 2) {
                    println("Invalid assignment")
                }
                if (variables[1].isDigit()) {
                    calc.variables[variables[0]] = variables[1].toInt()
                } else if (!calc.variable(variables[1])) {
                    println("Unknown variable")
                } else {
                    calc.variables[variables[0]] = calc.variableToDigit(variables[1])
                }
            }
        }
    }
}
