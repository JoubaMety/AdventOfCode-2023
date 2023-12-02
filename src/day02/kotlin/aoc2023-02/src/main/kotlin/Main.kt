/**
 * Loads input text-file from src/main/resources
 */
fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

fun main() {

    /*
    val input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n" +
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n" +
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n" +
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n" +
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
     */
    val input = getResourceAsText("adventofcode.com_2023_day_2_input.txt")!!
    val pattern = arrayOf(12, 13, 14)
    println(getSumOfIDsFromGames(input, pattern))
    println(getSumOfPowersFromGames(input))
}

/**
 * Splits String into elements, is put into a List
 */
fun convertStringToList(input: String): List<String> =
    input.split(regex = "[\r\n]+".toRegex()).toList()

//                                           do you see what I see? lmfao
fun convertGamesStringToList(input: String): List<List<String>> {
    val inputLines = convertStringToList(input)
    val mutableOutput = mutableListOf("")

    val outputMutableList: MutableList<List<String>> = mutableListOf()
    inputLines.forEach { inputLine ->
        val splitLine = inputLine
            .replace("^Game (0|[1-9][0-9]?|100): ".toRegex(), "")
            .split("; ".toRegex())
        outputMutableList.add(splitLine)
    }

    return outputMutableList
}

/**
 * Finds index of color for the getSum functions
 * Red - 0; Green - 1; Blue - 2
 */
fun findColorIndexFromString(input: String): Int {
    val colorList = listOf("red", "green", "blue")
    for (i in colorList.indices)
        if (input.contains(colorList[i]))
            return i
    return -1
}

/**
 * Gets sum of IDs from Games, that are valid
 * a.k.a. if the number of cubes in pattern match the number of cubes that could be played in each game/set
 */
fun getSumOfIDsFromGames(input: String, pattern: Array<Int>): Int {
    val gameList = convertGamesStringToList(input)

    var i = 1
    var sum = 0
    gameList.forEach { setList ->
        var valid = true
        setList.forEach { set ->
            val colorList = set.split(", ".toRegex())
            colorList.forEach { color ->
                val colorIndex = findColorIndexFromString(color)
                val count = color.replace(" ((red)|(green)|(blue))".toRegex(), "").toInt()
                if (count > pattern[colorIndex])
                    valid = false
            }
        }
        if (valid) sum += i
        i++
    }

    return sum
}

/**
 * Gets sum of powers of the least amount of colored cubes required for each game.
 */
fun getSumOfPowersFromGames(input: String): Int {
    val gameList = convertGamesStringToList(input)

    var sum = 0
    gameList.forEach { setList ->
        val minimumRequiredCubes : MutableList<Int> = mutableListOf(0,0,0)
        setList.forEach { set ->
            val colorList = set.split(", ".toRegex())
            colorList.forEach { color ->
                val colorIndex = findColorIndexFromString(color)
                val count = color.replace(" ((red)|(green)|(blue))".toRegex(), "").toInt()
                if (count > minimumRequiredCubes[colorIndex])
                    minimumRequiredCubes[colorIndex] = count
            }
        }
        sum += (minimumRequiredCubes[0] *
                minimumRequiredCubes[1] *
                minimumRequiredCubes[2])
    }

    return sum
}