
package pageTable

import AccessType
import PageFaultError

class PageTable(physicalPagesNum: Int) :KernelPageTable, MmuPageTable {
    private val pages = ArrayList<PageTableElement>()

    init {
        for (i in 0 until physicalPagesNum){
            pages.add(PageTableElement())
        }
    }

    override fun createPage(vpn: Int, ppn: Long) {
        val page = pages[vpn]
        page.ppn = ppn
        page.presenceBit = true
    }


    override fun checkAccessToPage(vpn: Int): Boolean {
        val page = pages[vpn]
        val bit = page.referenceBit
        page.referenceBit = false
        return bit
    }

    override fun getDataToFS(vpn: Int) {
        val page = pages[vpn]
        page.modificationBit = false

    }

    override fun destroyPage(vpn: Int) {
        val page = pages[vpn]
        //checkPresenceBit(page)
        page.presenceBit = false
    }


    override fun accessToPage(vpn: Int, accessType: AccessType) {
        val page = pages[vpn]
        checkPresenceBit(page)
        page.referenceBit = true
        if (accessType == AccessType.WRITE){
            page.modificationBit = true
        }
    }

    override fun getPages(): ArrayList<PageTableElement> {
        return pages
    }

    private fun checkPresenceBit(page: PageTableElement){
        if (!page.presenceBit){
            throw PageFaultError()
        }
    }

    fun getSetOfVPN(): Set<Int> {
        return pages.indices.toSet()
    }
}