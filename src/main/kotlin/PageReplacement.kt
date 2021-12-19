import pageTable.KernelPageTable

class PageReplacement(val pageTable: KernelPageTable) {
    private val workingSetTime = HashMap<Int, Int>()
    private val tTicks = 1000 //after that time page remove from working set

    fun addToWorkSet(vpn: Int, currentTick: Int){
        if (vpn !in workingSetTime.keys){
            workingSetTime[vpn] = currentTick
        }
    }

    fun getLongestUnused(currentTick: Int): Pair<Int, Int> {
        var page = 0
        var time = 0
        for (vpn in workingSetTime.keys){
            val unusedTime = currentTick - workingSetTime[vpn]!!
            if (unusedTime > time){
                page = vpn
                time = unusedTime
            }
        }
        return Pair(page,time)
    }

    //make each m milliseconds
    fun checkPages(currentTick: Int){
        for (vpn in workingSetTime.keys){
            if (pageTable.checkAccessToPage(vpn)){
                workingSetTime[vpn] = currentTick
            }else{
                if (currentTick - workingSetTime[vpn]!!  > tTicks){
                    workingSetTime.remove(vpn)
                }
            }
        }
    }

    fun getWorkGroup(): MutableSet<Int> {
        return workingSetTime.keys
    }

    fun removePage(victimPage: Int) {
        workingSetTime.remove(victimPage)
    }
}