class MySystem() {
    //local memory model, all process have similar weight
    private val mmu = MMU()
    private val numOfPhysicalPages = 100
    private val numOfTicks = 10000
    private val numOfProcess = 10
    private val percentOfPagesInWorkSet = 0.8//increase to check work, num of faults should increase
    private val processesQueue = ArrayList<MyProcess>()
    private val ticksForOneProcess = 5 //all processes needs time multiple to this parameter
    private val processesGenerator = ProcessesGenerator(percentOfPagesInWorkSet, numOfPhysicalPages)

    init {
        for (i in 0 until numOfProcess){
            addProcess()
        }
    }


    fun start(){
        var tickOnProcessCounter = 0
        var process = processesQueue.first()
        for (i in 0..numOfTicks){
            tickOnProcessCounter++
            if (tickOnProcessCounter > ticksForOneProcess){
                processesQueue.remove(process)
                if (process.stillWorking()){
                    processesQueue.add(process)
                }else{
                    mmu.removeClient(process.getMMU())
                    addProcess()

                }
                process = processesQueue.first()
                tickOnProcessCounter = 1
            }
            process.doActionOnTick(i)
            mmu.doActionOnTick(i)
        }
        println("System works end, num of faults:" + mmu.pageFaultCounter)
    }

    private fun addProcess() {
        val process = processesGenerator.makeProcess()
        process.setMMU(mmu.addProcess(numOfPhysicalPages))
        processesQueue.add(process)
    }
}