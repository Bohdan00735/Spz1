class FileSystemSimulator {
    //map key = process id, value - map with key - last virtual address, last physical address(address - number of the page)
    private val savedData = HashMap<Long, HashMap<Long, Long>>()

    fun saveData(processId: Long, vpn: Long ,ppn: Long){
        var processData = savedData[processId]
        if (processData == null){
            processData = HashMap()
            savedData[processId] = processData
        }
        processData[vpn] = ppn
    }

    fun getData(processId: Long, vpn: Long): Long {
        return savedData[processId]?.get(vpn)!!
    }
}