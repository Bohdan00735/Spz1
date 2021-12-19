class MMU() {
    private val clients = ArrayList<MMUClient>()
    var pageFaultCounter = 0

    fun addProcess( numOfPhysicalPages: Int): MMUClient {
        val client = MMUClient(this, numOfPhysicalPages)
        clients.add(client)
        return client
    }

    fun getNewPage(currentTick: Int): Int {

        val victimPages = lookForFree()
        val victimPage = if (victimPages.isEmpty()){
            findMostWaiting(currentTick).first
        }else{
            victimPages.first()
        }
        println("victim page found $victimPage")
        saveToFS(victimPage)
        clearInTables(victimPage)
        loadFromFs()
        return victimPage
    }

    private fun loadFromFs() {
        //get data from file System
    }

    private fun clearInTables(victimPage: Int) {
        for (clientMMU in clients){
            clientMMU.freeVPN(victimPage)
        }
    }

    private fun saveToFS(victimPage: Int) {
        println("save to FS $victimPage")
        //get data from physical page and write it to file system
    }

    private fun findMostWaiting(currentTick: Int): Pair<Int, Int> {
        var page = Pair(0,0)
        for (clientMMU in clients){
            val clientPage = clientMMU.getVictimPage(currentTick)
            if (page.second < clientPage.second){
                page = clientPage
            }
        }
        return page
    }

    private fun lookForFree(): Set<Int> {
        var freeSet = clients.first().getFreeAddresses()
        for (clientMMU in clients){

            freeSet = freeSet.intersect(clientMMU.getFreeAddresses())

        }
        return freeSet
    }

    fun doActionOnTick(tick: Int) {
        clients.forEach { mmuClient -> mmuClient.onTick(tick) }
    }

    fun removeClient(mmu: MMUClient) {
        clients.remove(mmu)
    }
}