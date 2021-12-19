import kotlin.random.Random

class MyProcess(val id:Long, workingSetBounds: Pair<Int, Int>, val numOfVirtualPages: Int) {
    private lateinit var mmuClient: MMUClient
    private val random = Random
    private val allVirtualAddresses = IntArray(numOfVirtualPages){it}
    private val workSet = allVirtualAddresses.slice(workingSetBounds.first .. workingSetBounds.second).toHashSet()
    private var notWorkingSet:HashSet<Int> = (allVirtualAddresses.toHashSet() - workSet) as HashSet<Int>
    var currentAccessSetTo = true //where we take virtual page on tick
    fun setMMU(mmu: MMUClient) {
        mmuClient = mmu
    }

    fun stillWorking(): Boolean {
        return random.nextDouble() < 0.9//possibility to end process
    }

    fun doActionOnTick(currentTick: Int) {
        println("---------$id----------")
        val returnedPage = mmuClient.getAccessToPage(getAccessType(), getVirtualAddress(), currentTick)
        if (currentAccessSetTo){
            workSet.add(returnedPage)
            notWorkingSet.add(returnedPage)
        }else{
            workSet.remove(returnedPage)
            notWorkingSet.remove(returnedPage)
        }
    }

    private fun getVirtualAddress(): Int {
        return if (random.nextDouble() < 0.8){//choose from work set or not
            currentAccessSetTo = true
            workSet.random()
        }else{
            currentAccessSetTo = true
            notWorkingSet.random()
        }
    }

    private fun getAccessType(): AccessType {
        return if (random.nextBoolean()) AccessType.WRITE
        else AccessType.READ
    }

    fun getMMU(): MMUClient {
        return mmuClient
    }


}