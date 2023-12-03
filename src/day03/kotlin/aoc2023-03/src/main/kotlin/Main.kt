data class StringInSchematic(
    val schematic: List<String>,
    val index: Index,
    val length: Int
)

data class Index(
    val column: Int,
    val row: Int
)

class PartNumberMaximumDimensions(
    partNumber: StringInSchematic
) {
    val top = getTopDimension(partNumber)
    val bottom = getBottomDimension(partNumber)
    val left = getLeftDimension(partNumber)
    val right = getRightDimension(partNumber)

    private fun getTopDimension(partNumber: StringInSchematic): Int {
        var top: Int = partNumber.index.row
        if (top - 1 >= 0)
            top--
        return top
    }

    private fun getBottomDimension(partNumber: StringInSchematic): Int {
        var bottom: Int = partNumber.index.row
        if (bottom + 1 < (partNumber.schematic.size))
            bottom++
        return bottom
    }

    private fun getLeftDimension(partNumber: StringInSchematic): Int {
        var left: Int = partNumber.index.column
        if (left - 1 >= 0)
            left--
        return left
    }

    private fun getRightDimension(partNumber: StringInSchematic): Int {
        var right: Int = partNumber.index.column + (partNumber.length - 1)
        if (right + 1 < (partNumber.schematic[partNumber.index.row].length - 1))
            right++
        return right
    }
}


fun getSumOfPartNumbers(input: String): Int {
    val inputList = convertStringToList(input)

    var sumOfPartNumbers = 0
    var partNumberIndex: Int
    var partNumberLength: Int
    var currentRow = 0
    inputList.forEach { row ->
        if (row.isNotEmpty()) {
            partNumberIndex = -1
            partNumberLength = 0
            var characterIndex = 0
            row.forEach { character ->
                if (character.isDigit() && character != '.') {
                    if (partNumberIndex == -1) partNumberIndex = characterIndex
                    partNumberLength++
                }
                if (!character.isDigit() || characterIndex+1 == row.length) {
                    if (partNumberIndex != -1) {
                        if (findSymbolForPartNumber(
                                StringInSchematic(
                                    inputList,
                                    Index(partNumberIndex, currentRow),
                                    partNumberLength
                                )
                            )
                        ) {
                            val substring = inputList[currentRow]
                                .substring(partNumberIndex, partNumberIndex + partNumberLength)
                            val sum = substring.toIntOrNull()
                            if (sum != null)
                                sumOfPartNumbers += sum
                        }
                        partNumberIndex = -1
                        partNumberLength = 0
                    }
                }
                characterIndex++
            }
        }
        currentRow++
    }

    return sumOfPartNumbers
}

fun findSymbolForPartNumber(partNumber: StringInSchematic): Boolean {

    val maxDimensions = PartNumberMaximumDimensions(partNumber)

    var foundSymbol = false
    if (partNumber.schematic[partNumber.index.row].isNotBlank()) {
        y@ for (y in maxDimensions.top..maxDimensions.bottom) {
            x@ for (x in maxDimensions.left..maxDimensions.right) {
                val character = partNumber.schematic[y][x]
                if (!character.isDigit() && character != '.') {
                    foundSymbol = true
                    break@y
                }
            }
        }
    }

    return foundSymbol
}


class GearRatio(
    private val schematic: List<String>,
    private val index: Index
) {
    private var firstNumber: Int = 0
    private var secondNumber: Int = 0

    fun getNearbyNumbers() {
        val maxDimensions = PartNumberMaximumDimensions(
            StringInSchematic(schematic, index, 1)
        )

        var numbersCount = 0
        val foundNumbersList : MutableList<StringInSchematic> = mutableListOf()
        y@ for (y in maxDimensions.top..maxDimensions.bottom) {
            x@ for (x in maxDimensions.left..maxDimensions.right) {
                if (schematic[y][x].isDigit()) {
                    var foundNumberBool = false
                    foundNumbersList.forEach { foundNumber ->
                        if(foundNumber.index.row == y) {
                            val startIndex = foundNumber.index.column
                            val endIndex = foundNumber.index.column + foundNumber.length
                            if (x in startIndex..endIndex) {
                                foundNumberBool = true
                            }
                        }
                    }
                    if (!foundNumberBool) {
                        val wholeNumber = getWholeNumber(Index(x,y))
                        //           duuuuuuudee,wtf
                        val number = wholeNumber.number
                        foundNumbersList.add(wholeNumber.stringInSchematic)
                        if (numbersCount == 0) {
                            firstNumber += number
                        } else if (numbersCount == 1) {
                            secondNumber += number
                            break@y
                        }
                        numbersCount++
                    }
                }
            }
        }
    }

    private data class WholeNumber(
        val stringInSchematic: StringInSchematic,
        val number: Int
    )

    private fun getWholeNumber(index: Index) : WholeNumber {
        var number = ""

        var numberIndex = index.column
        var numberLength = 0
        for(i in (0..index.column).reversed()) {
            val character = schematic[index.row][i]
            if (character.isDigit()) {
                number = "$character$number"
                numberIndex = i
                numberLength++
            } else {
                break
            }
        }
        for(i in index.column+1..< schematic[index.row].length) {
            val character = schematic[index.row][i]
            if (schematic[index.row][i].isDigit()) {
                number = "$number$character"
                numberLength++
            } else {
                break
            }
        }

        return WholeNumber(
            StringInSchematic(
                schematic,
                Index(numberIndex, index.row),
                numberLength
            ),
            number.toInt()
        )
    }

    fun getGearRatio() : Int {
        return firstNumber * secondNumber
    }

    fun isValid() : Boolean {
        return firstNumber > 0 && secondNumber > 0
    }
}


fun getSumOfGearRatios(input: String) : Int {
    val inputList = convertStringToList(input)

    var sumOfGearRatios = 0
    var currentRow = 0
    inputList.forEach { row ->
        var currentChar = 0
        row.forEach { character ->
            if (character == '*') {
                val gearRatio = GearRatio(inputList, Index(currentChar, currentRow))
                gearRatio.getNearbyNumbers()
                if(gearRatio.isValid()) sumOfGearRatios += gearRatio.getGearRatio()
            }
            currentChar++
        }
        currentRow += 1
    }

    return sumOfGearRatios
}

fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

/**
 * Splits String into elements, is put into a List
 */
fun convertStringToList(input: String): List<String> =
    input.split(regex = "[\r\n]+".toRegex()).toList()


fun main() {
    val input = getResourceAsText("adventofcode.com_2023_day_3_input.txt")
    if (input != null) {
        val sumOfPartNumbers = getSumOfPartNumbers(input)
        val sumOfGearRatios = getSumOfGearRatios(input)

        println("AOC 2023 | Day 03")
        println("- Part 01: Sum of Part Numbers: $sumOfPartNumbers")
        println("- Part 01: Sum of Gear Ratios: $sumOfGearRatios")
    } else {
        error("ERROR: Input text file couldn't be read.")
    }
}
