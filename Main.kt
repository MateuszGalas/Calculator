package calculator

class Calculator {
    val variables = mutableMapOf<String, Int>()

    fun operation(operator: String): String {
        return when (operator.first()) {
            '+' -> "+"
            else -> {
                if (operator.length % 2 == 0) "+"
                else "-"
            }
        }
    }

    fun add(a: Int, b: Int): Int {
        return a + b
    }

    fun subtract(a: Int, b: Int): Int {
        return a - b
    }

    fun variable(variable: String): Boolean {
        return variables.containsKey(variable)
    }

    fun variableToDigit(variable: String): Int {
        return variables[variable]!!
    }

    fun makeOperations(input: String) {
        val res = input.split(" ")
        var result = if (variable(res[0])) variableToDigit(res[0]) else res[0].toInt()

        for (i in 1..res.lastIndex step 2) {
            when (operation(res[i])) {
                "+" -> {
                    result = if (variable(res[i + 1])) {
                        add(result, variableToDigit(res[i + 1]))
                    } else {
                        add(result, res[i + 1].toInt())
                    }
                }

                "-" -> {
                    result = if (variable(res[i + 1])) {
                        subtract(result, variableToDigit(res[i + 1]))
                    } else {
                        subtract(result, res[i + 1].toInt())
                    }
                }
            }
        }
        println(result)
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

            input.matches("""(\d+|[a-zA-Z]+)\s+(\++|-+)\s+(\d+|[a-zA-Z]+).*""".toRegex()) -> {
                try {
                    calc.makeOperations(input)
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
                if (!variables.last().matches("""(\d+|[a-zA-Z]+)""".toRegex()) || variables.size > 2) {
                    println("Invalid assignment")
                }
                if (variables[1].matches("""\d+""".toRegex())) {
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