package os;

import java.util.*;

public class dispatch {
    ArrayList<node> ready_array = new ArrayList<node>();//结点数据为node，来存储就绪进程
    ArrayList<node> pend_array = new ArrayList<node>();//结点数据为node，来存储挂起进程
    ArrayList<node> reserve_array = new ArrayList<node>();//结点数据为node，来存储后备进程
    ArrayList<node> wait_array = new ArrayList<node>();//结点数据为node，来存阻塞进程
    Memory memory;
    int segment = 6;



    public dispatch(Memory memory,int segment)
    {
        this.memory = memory;
        this.segment =segment;
    }


    boolean malloc(node node)
    {

        int a = Integer.parseInt(node.data.name);
        int size =node.data.memory_size;
        for(int i=0;i<memory.me.length-size;i++)
        {
            boolean flag=true;
            for(int j=0;j<size;j++)
            {
                if(memory.me[i+j] != 0)
                    flag=false;
            }
            if(flag)
            {
                for(int j=0;j<size;j++)
                {
                    memory.me[i+j] = a;

                }
                node.data.memory_point =i;
                memory.num++;
                return true;
            }
        }

        return false;

    }

    void free(node node)
    {
        for(int i =node.data.memory_point;i<node.data.memory_point+node.data.memory_size;i++)
        {
            memory.me[i]=0;
        }
        memory.num--;
    }

    boolean addnode2ready()
    {

        node node = reserve_array.get(0);
        if(malloc(node)){ //按进程所需的内存大小，向内存申请一块内存，若申请条件符合，则为申请成功。
        	//从后备队列取出最优先的进程
            node.data.status=1;
            ready_array.add(node);
            reserve_array.remove(0);
            return true;
        }
        else
            return false;
    }

    void sortqueue(ArrayList<node> array)//调用Java中的Collections.sort（）函数，依据优先级，将进程排列
    {
        Collections.sort(array, new Comparator<node>()
        {

            @Override
            public int compare(node o1, node o2) {
                if(o1.data.priority<=o2.data.priority)
                    return 1;
                else
                    return -1;

            }
        });
    }


    String run() //从就绪队列队头中取出就绪进程，执行该进程
    {
        node head = ready_array.get(0); 
        if(!findnode(pend_array,head.pre).data.name.equals("0"))
        {
            block(head);  //如果它的前趋进程被挂起，则该进程将会被阻塞
        }
        else{
        head.data.time -= 1;
        head.data.priority -= 1; //被执行的进程优先权-1，要求运行时间-1


        if (head.data.time <= 0) {  //要求运行时间为0时，撤销该进程
            ready_array.remove(head);
            free(head);
            // printMemory();
            return head.data.name;
        }
    }
        return "";
    }

    void operate()
    {

        run();
        sortqueue(ready_array); //一个时间片结束后，重新排序，进行下轮调度
    }

    void printList()
    {
        for(node eachnode: ready_array)
        {
           System.out.println(eachnode.data.name+" "
                  +eachnode.data.time+" "+eachnode.data.priority);
        }
    }

    void printnode()
    {
        node head = ready_array.get(0);
        System.out.println(head.data.name+" "
                +head.data.time+" "+head.data.priority);
    }


    void printMemory()
    {
        System.out.println("-----------------------------");
        for(int i=0;i<memory.me.length;i++)
        {
            System.out.print(memory.me[i]);
        }
        System.out.println();
        System.out.println("-----------------------------");
    }

    void block(node node)//当某进程前驱被挂起时，该进程会阻塞。当该进程的前驱被解挂的时候，该进程会自动被唤醒。
    {
        ready_array.remove(node);
        node.data.status=3;
        wait_array.add(node);

    }


    void wakeup(node node)
    {
        wait_array.remove(node);
        node.data.status=1;
        ready_array.add(node);

    }

    void wake_scan()//阻塞发生在执行前的检查时
    {
        for(node node : wait_array )
        {
            if(findnode(pend_array,node.pre).data.name.equals("0"))
                wakeup(node);
        }
        sortqueue(ready_array);
    }

    void suspend(node node)//可主动选取欲挂起进程，输入进程名，可将内存中的进程挂起，放入到挂起队列中
    {
        ready_array.remove(node);
        node.data.status=4;
        free(node);
        pend_array.add(node);
    }

    void active(node node)//可主动选取欲解挂进程，输入进程名，可将进程解挂，从挂起队列中取出，重新将此进程放入到内存中与就绪队列中
    {

        if(malloc(node)){
            pend_array.remove(node);
            node.data.status=1;

            ready_array.add(node);

            sortqueue(ready_array);
            wake_scan();
        }

    }


    public node findnode(ArrayList<node> array, String name){
            for(node eachnode : array)
            {
                if(eachnode.data.name.equals(name))
                {
                    return eachnode;
                }
            }
            node n = new node("0",0,0,0,0,"0");
            return n;
    }

    void addnode2reserve(node node)//输入进程信息
    {
        reserve_array.add(node);//将进程加入后备队列中
        sortqueue(reserve_array); //添加好的进程在后备队列，按优先级排列
    }

    void loadnode()
    {
        while(ready_array.size()<segment&&reserve_array.size()>0
                        && addnode2ready())
        { }
    }

}
