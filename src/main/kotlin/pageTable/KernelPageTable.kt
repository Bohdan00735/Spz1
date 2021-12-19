package pageTable

interface KernelPageTable {
    fun createPage(vpn: Int, ppn:Long)
    fun checkAccessToPage(vpn: Int): Boolean
    fun getDataToFS(vpn: Int)
    fun getPages(): ArrayList<PageTableElement>
}