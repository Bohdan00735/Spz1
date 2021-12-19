import pageTable.PageTable

class MMUClient(val mmu: MMU,physicalPagesNum: Int) {
    private  val mmuPageTable = PageTable(physicalPagesNum)
    private val pageReplacement = PageReplacement(mmuPageTable)

    fun getAccessToPage(actionType: AccessType, virtualAddress: Int): Int {

        println("get access to $virtualAddress")
        return try {
            mmuPageTable.accessToPage(virtualAddress, actionType)
            pageReplacement.addToWorkSet(virtualAddress)
            println(actionType.name + " in $virtualAddress")
            virtualAddress
        }catch (pageFault: PageFaultError){
            println("Page fault $virtualAddress")
            mmu.pageFaultCounter++
            val newVPN = mmu.getNewPage()
            mmuPageTable.createPage(newVPN, newVPN.toLong())//ppn should come from kernel
            getAccessToPage(actionType, newVPN)
        }
    }

    fun getFreeAddresses(): Set<Int> {
        return  mmuPageTable.getSetOfVPN() - pageReplacement.getWorkGroup()
    }

    fun getVictimPage(): Pair<Int, Int>? {
        return pageReplacement.getLongestUnused()
    }

    fun freeVPN(victimPage: Int) {
        mmuPageTable.destroyPage(victimPage)
    }

    fun onTick(tick: Int) {
        pageReplacement.checkPages(tick)
    }
}