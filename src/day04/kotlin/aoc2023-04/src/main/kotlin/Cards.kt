import kotlin.math.min

class Cards(
    private val input: String
) {
    private val inputList = convertStringToList(input)
    val cards: List<Card> = getAllCards()

    private fun getAllCards(): List<Card> {
        val tempList: MutableList<Card> = mutableListOf()
        for (i in inputList.indices) {
            tempList.add(Card(inputList[i]))
        }
        return tempList
    }

    fun getSumOfTotalPoints(): Int {
        var sum = 0
        cards.forEach { card ->
            sum += card.getSumOfPoints()
        }
        return sum
    }

    fun getSumOfNewCopies(): Int {
        val instanceAmountMap = mutableMapOf<Int, Int>()

        for (i in cards.indices)
            instanceAmountMap[i] = 1

        instanceAmountMap.forEach { instance ->
            val count1 = cards[instance.key].getAmountOfCopies()
            for (instanceCount in 0 until instance.value) {
                val minCount = (instance.key + 1) + count1
                val maxCount = cards.size
                for (i in (instance.key + 1) until min(minCount, maxCount)) {
                    instanceAmountMap[i] = instanceAmountMap[i]!! + 1
                }
            }
        }

        var count = 0
        instanceAmountMap.forEach { instance ->
            count += instance.value
        }
        return count
    }
}