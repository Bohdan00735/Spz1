import kotlin.random.Random

class MyProcess(val id:Long, private val workingSetBounds: Pair<Int, Int>, val numOfVirtualPages: Int) {
    private lateinit var mmuClient: MMUClient
    private val random = Random

    fun setMMU(mmu: MMUClient) {
        mmuClient = mmu
    }

    fun stillWorking(): Boolean {
        return random.nextDouble() < 0.9//possibility to end process
    }

    fun doActionOnTick() {
        println("---------$id----------")
        mmuClient.getAccessToPage(getAccessType(), getVirtualAddress())
    }

    private fun getVirtualAddress(): Int {
        return if (random.nextDouble() < 0.8){//choose from work set or not
             random.nextInt(workingSetBounds.first, workingSetBounds.second)
        }else random.nextInt(numOfVirtualPages)
    }

    private fun getAccessType(): AccessType {
        return if (random.nextBoolean()) AccessType.WRITE
        else AccessType.READ
    }

    fun getMMU(): MMUClient {
        return mmuClient
    }


}