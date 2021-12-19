package pageTable

import AccessType

interface MmuPageTable {
    fun destroyPage(vpn: Int)
    fun accessToPage(vpn:Int, accessType: AccessType)

}