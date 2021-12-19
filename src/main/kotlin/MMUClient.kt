import pageTable.PageTable

class MMUClient(val mmu: MMU,physicalPagesNum: Int) {
    private  val mmuPageTable = PageTable(physicalPagesNum)
    private val pageReplacement = PageReplacement(mmuPageTable)

    fun getAccessToPage(actionType: AccessType, virtualAddress: Int, current_tik: Int): Int {

        println("get access to $virtualAddress")
        return try {
            mmuPageTable.accessToPage(virtualAddress, actionType)
            pageReplacement.addToWorkSet(virtualAddress, current_tik)
            println(actionType.name + " in $virtualAddress")
            virtualAddress
        }catch (pageFault: PageFaultError){
            println("Page fault $virtualAddress")
            mmu.pageFaultCounter++
            val newVPN = mmu.getNewPage(current_tik)

            mmuPageTable.createPage(newVPN, newVPN.toLong())//ppn should come from kernel
            getAccessToPage(actionType, newVPN, current_tik)
        }
    }

    fun getFreeAddresses(): Set<Int> {
        return (mmuPageTable.getSetOfVPN() - pageReplacement.getWorkGroup())
    }

    fun getVictimPage(current_tik: Int): Pair<Int, Int> {
        return pageReplacement.getLongestUnused(current_tik)
    }

    fun freeVPN(victimPage: Int) {
        mmuPageTable.destroyPage(victimPage)
        pageReplacement.removePage(victimPage)
    }

    fun onTick(tick: Int) {
        pageReplacement.checkPages(tick)
    }
}