package calculator

class Calculator {

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
}

fun main() {
    val calc = Calculator()

    while (true) {
        val input = readln().split(" ")

        if (input.first() == "") continue
        if (input.first().equals("/exit", true)) println("Bye!").also { return }
        if (input.first().equals("/help", true)) {
            println("The program calculates the sum or subtraction of numbers, you can also mix them" +
                    "like number + number - number. Program works if there are more operators than needed" +
                    "number +++ number -- number")
            continue
        }
        if (input.first().matches("""/\w+""".toRegex())) {
            println("Unknown command")
            continue
        }

        try {
            var result = input[0].toInt()
            for (i in 1..input.lastIndex step 2) {
                when(calc.operation(input[i])) {
                    "+" -> result = calc.add(result, input[i + 1].toInt())
                    "-" -> result = calc.subtract(result, input[i + 1].toInt())
                }
            }
            println(result)
        } catch (e: Exception) {
            println("Invalid expression")
            continue
        }
    }
}
