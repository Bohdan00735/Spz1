import kotlin.random.Random

class ProcessesGenerator(workSetPercent:Double, private val numOfAddresses: Int) {
    private val ids = ArrayList<Long>()
    private val workSetSize = (numOfAddresses* workSetPercent).toInt()

    fun makeProcess(): MyProcess {
        return MyProcess(generateId(), generateWorkSetBounds(), numOfAddresses)
    }

    private fun generateWorkSetBounds(): Pair<Int, Int> {
        val lowBound = Random.nextInt(0, numOfAddresses - workSetSize)
        return Pair(lowBound, lowBound+workSetSize)
    }

    private fun generateId(): Long {
        while (true){
            val id = Random.nextLong()
            if (id !in ids){
                return id
            }
        }
    }


}