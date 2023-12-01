fun main() {
    val input = getResourceAsText("adventofcode.com_2023_day_1_input.txt")!!
    println(getSumOfCalibrationValues(input))
}

/**
 * Loads input text-file from src/main/resources
 */
fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

/**
 * Splits String into elements, is put into a List
 */
fun convertStringToList(input: String) : List<String> =
    input.split(regex = "[\r\n]+".toRegex()).toList()

fun getSumOfCalibrationValues(input: String): Int {
    val inputList = convertStringToList(input)
    var output = 0
    inputList.forEach {
        if (it.isNotEmpty()) {
            val numbersFromLine: Array<Int> = arrayOf(
                findCalibrationNumbers(it, true),
                findCalibrationNumbers(it, false)
            )
            output += "${numbersFromLine[0]}${numbersFromLine[1]}".toInt()
        }
    }
    return output
}

/**
 * Finds calibration values by searching for first & last numerical (0-9),
 * and then searching for first & last number letters (one-nine).
 *
 * If there's only one number, it's both first & last.
 */
fun findCalibrationNumbers(input: String, first: Boolean): Int {
    // ugh...
    val numericalList = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9')
    val numberLetterList = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    var numberIndex = if (first) input.length else -1
    var output = 0
    if (first) {
        for (number in numericalList) {
            val currentIndex = input.indexOf(number)
            if (currentIndex < numberIndex && currentIndex != -1) {
                numberIndex = currentIndex
                output = number.digitToInt()
            }
        }
        for (number in numberLetterList) {
            val currentIndex = input.indexOf(number, ignoreCase = true)
            if (currentIndex < numberIndex && currentIndex != -1) {
                numberIndex = currentIndex
                output = numericalList[numberLetterList.indexOf(number)].digitToInt()
            }
        }
    } else {
        for (number in numericalList) {
            val currentIndex = input.lastIndexOf(number)
            if (currentIndex > numberIndex) {
                numberIndex = currentIndex
                output = number.digitToInt()
            }
        }
        for (currentIndex in numberLetterList) {
            val index = input.lastIndexOf(currentIndex)
            if (index > numberIndex) {
                numberIndex = index
                output = numericalList[numberLetterList.indexOf(currentIndex)].digitToInt()
            }
        }
    }

    return output
}