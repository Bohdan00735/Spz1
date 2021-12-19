class MMU() {
    private val clients = ArrayList<MMUClient>()
    var pageFaultCounter = 0

    fun addProcess( numOfPhysicalPages: Int): MMUClient {
        val client = MMUClient(this, numOfPhysicalPages)
        clients.add(client)
        return client
    }

    fun getNewPage(): Int {

        val victimPages = lookForFree()
        val victimPage = if (victimPages.isEmpty()){
            findMostWaiting().first
        }else{
            victimPages.first()
        }
        println("victim page found $victimPage")
        saveToFS(victimPage)
        clearInTables(victimPage)
        return victimPage
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

    private fun findMostWaiting(): Pair<Int, Int> {
        var page = Pair(0,0)
        for (clientMMU in clients){
            val clientPage = clientMMU.getVictimPage() ?: continue
            if (page.second < clientPage.second){
                page = clientPage
            }
        }
        return page
    }

    private fun lookForFree(): HashSet<Int> {
        val freeSet = HashSet<Int>()
        for (clientMMU in clients){
            freeSet.addAll(clientMMU.getFreeAddresses())
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