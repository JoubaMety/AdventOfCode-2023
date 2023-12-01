fun main() {
    val input = getResourceAsText("adventofcode.com_2023_day_1_input.txt")!!
    println(getSumOfRequiredNumbers(input))
}

/*
    Loads day 1 input from src/main/resources
 */
fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

fun getSumOfRequiredNumbers(input: String): Int {

    val inputLines = input.split(regex = "[\r\n]+".toRegex()).toList()
    var output = 0
    inputLines.forEach {
        if (it.isNotEmpty()) {
            val numbersFromLine: Array<Int> = arrayOf(
                findNumberInOneLine(it, true),
                findNumberInOneLine(it, false)
            )
            output += "${numbersFromLine[0]}${numbersFromLine[1]}".toInt()
        }
    }
    return output
}

fun findNumberInOneLine(input: String, first: Boolean): Int {
    val numberArray = charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
    val numberLetterList = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    var numberIndex = if (first) input.length else -1
    var output = 0
    if (first) {
        for (number in numberArray) {
            val index = input.indexOf(number)
            if (index < numberIndex && index != -1) {
                numberIndex = index
                output = number.digitToInt()
            }
        }
        for (number in numberLetterList) {
            val index = input.indexOf(number, ignoreCase = true)
            if (index < numberIndex && index != -1) {
                numberIndex = index
                output = numberArray[numberLetterList.indexOf(number)].digitToInt()
            }
        }
    } else {
        for (number in numberArray) {
            val index = input.lastIndexOf(number)
            if (index > numberIndex) {
                numberIndex = index
                output = number.digitToInt()
            }
        }
        for (number in numberLetterList) {
            val index = input.lastIndexOf(number)
            if (index > numberIndex) {
                numberIndex = index
                output = numberArray[numberLetterList.indexOf(number)].digitToInt()
            }
        }
    }

    return output
}