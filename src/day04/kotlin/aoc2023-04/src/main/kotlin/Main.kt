fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

/**
 * Splits String into elements, is put into a List
 */
fun convertStringToList(input: String): List<String> =
    input.split(regex = "[\r\n]+".toRegex()).toList()

fun main() {
    val input = getResourceAsText("adventofcode.com_2023_day_4_input.txt")!!

    val cards = Cards(input)
    val cardsPoints = cards.getSumOfTotalPoints()
    val cardsCopies = cards.getSumOfNewCopies()

    println("AOC 2023 | Day 04")
    println("Scratchcards")
    println(" - Part 1: Sum of Total Points: $cardsPoints")
    println(" - Part 2: Sum of New Copies:   $cardsCopies")
}