import kotlin.math.pow

class Card(
    private val input: String
) {
    private val numbers = getNumbers()

    val winningNumbers: List<Int> = numbers[0]
    val ownNumbers: List<Int> = numbers[1]

    //                        again? wtf dude. AND WHAT IS THAT BELOW AS WELL?! DUUUUUUUUDE
    private fun getNumbers(): List<List<Int>> {
        val tempString = input.replace("^Card ((\\s|[1-9])(\\s|[0-9])[0-9]?|100): ".toRegex(), "")
        val tempList = tempString.split("( \\| )".toRegex()).toList()
        val tempWinningNumbers: List<Int> = tempList[0].trim().split("(\\s\\s)|\\s".toRegex()).map { it.trim().toInt() }
        val tempOwnNumbers: List<Int> = tempList[1].trim().split("(\\s\\s)|\\s".toRegex()).map { it.trim().toInt() }
        return listOf(tempWinningNumbers, tempOwnNumbers)
    }

    fun getSumOfPoints(): Int {
        var count = 0.0

        winningNumbers.forEach { winningNumber ->
            ownNumbers.forEach { ownNumber ->
                if (ownNumber == winningNumber)
                    count++
            }
        }

        return 2.0.pow(count - 1).toInt()
    }

    fun getAmountOfCopies(): Int {
        var count = 0

        winningNumbers.forEach { winningNumber ->
            ownNumbers.forEach { ownNumber ->
                if (ownNumber == winningNumber)
                    count++
            }
        }

        return count
    }
}