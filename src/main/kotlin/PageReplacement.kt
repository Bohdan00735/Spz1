import pageTable.KernelPageTable

class PageReplacement(val pageTable: KernelPageTable) {
    private val workingSetTime = HashMap<Int, Int>()
    private val tTicks = 10 //after that time page remove from working set

    fun addToWorkSet(vpn: Int){
        if (vpn !in workingSetTime.keys){
            workingSetTime[vpn] = 0
        }
    }

    fun getLongestUnused(): Pair<Int, Int>? {
        var page = 0
        var time = 0
        for (ppn in workingSetTime.keys){
            val unusedTime = workingSetTime[ppn]!! - System.currentTimeMillis()
            if (unusedTime > time){
                page = ppn
                time = unusedTime.toInt()
            }
        }
        if (time!=0){
            return Pair(page,time)
        }
        return null
    }

    //make each m milliseconds
    fun checkPages(currentTick: Int){
        for (vpn in workingSetTime.keys){
            if (pageTable.checkAccessToPage(vpn)){
                workingSetTime[vpn] = currentTick
            }else{
                if (workingSetTime[vpn]!! - currentTick > tTicks){
                    workingSetTime.remove(vpn)
                }
            }
        }
    }

    fun getWorkGroup(): MutableSet<Int> {
        return workingSetTime.keys
    }
}