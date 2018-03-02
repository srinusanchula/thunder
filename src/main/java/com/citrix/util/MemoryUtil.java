package com.citrix.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryType;

public class MemoryUtil {
    private static Memory memory = new Memory();

    public static Memory getTotalMemory() {
        memory.reset();
        getMemory(memory, memory);
        return memory;
    }

    private static void getMemory(Memory heapMemory, Memory nonHeapMemory) {
        ManagementFactory.getMemoryPoolMXBeans().forEach(mxBean -> {
            if(mxBean.getType() == MemoryType.HEAP) {
                heapMemory.addInit(mxBean.getUsage().getInit());
                heapMemory.addUsed(mxBean.getUsage().getUsed());
                heapMemory.addCommitted(mxBean.getUsage().getCommitted());
                heapMemory.addMax(mxBean.getUsage().getMax());
            } else if(mxBean.getType() == MemoryType.NON_HEAP) {
                nonHeapMemory.addInit(mxBean.getUsage().getInit());
                nonHeapMemory.addUsed(mxBean.getUsage().getUsed());
                nonHeapMemory.addCommitted(mxBean.getUsage().getCommitted());
                nonHeapMemory.addMax(mxBean.getUsage().getMax());
            }
        });
    }

}
