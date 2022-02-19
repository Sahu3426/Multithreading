public class SimpleCountDownLatch {
    private int count;

    public SimpleCountDownLatch(int count) {
        this.count = count;
        if (count < 0) {
            throw new IllegalArgumentException("count cannot be negative");
        }
    }

    /**
     * Causes the current thread to wait until the latch has counted down to zero.
     * If the current count is already zero then this method returns immediately.
     */
    public void await() throws InterruptedException {
        synchronized (this) {
            while (count > 0) {
                this.wait();
            }
        }
    }

    /**
     *  Decrements the count of the latch, releasing all waiting threads when the count reaches zero.
     *  If the current count already equals zero then nothing happens.
     */
    public void countDown() {
        synchronized (this) {
            if (count > 0) {
                count--;

                if (count == 0) {
                    this.notifyAll();
                }
            }
        }
    }

    /**
     * Returns the current count.
     */
    public int getCount() {
        return this.count;
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleCountDownLatch latch = new SimpleCountDownLatch(3);
    }
}



//    The CountDownLatch and its simple version the SimpleCountDownLatch are initialized with a non-negative count.
//
//        The SimpleCountDownLatch allows one or more threads to wait until a set of operations being performed in other threads, completes.
//
//        The SimpleCountDownLatch has the following main operations:
//
//        countDown() - Decrements the count of the latch, releasing all waiting threads when the count reaches zero. If the current count already equals zero then nothing happens.
//
//        await() - Causes the current thread to wait until the latch has counted down to zero. If the current count is already zero then this method returns immediately.
//
//        The await method blocks until the current count reaches zero due to invocations of the countDown() method, after which all waiting threads are released and any subsequent invocations of await return immediately.
//
