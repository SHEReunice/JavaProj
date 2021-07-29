package os;

public class PCB {
    String name;//进程名
    int time;//执行时间
    int status=5;//1 就绪，2运行，3阻塞，4挂起，5后备
    int priority;//进程的优先级
    int memory_size;//进程所占的优先级大小
    int memory_point;//进程第一个块指向的内存位置
    boolean cooperation;//若为true，则为同步进程，反之，为false，为独立进程


}
