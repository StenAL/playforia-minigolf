package agolf;

public class SynchronizedInteger {

    private long n;

    public SynchronizedInteger() {
        this(0);
    }

    protected SynchronizedInteger(int n) {
        this.set(n);
    }

    public synchronized int set(int n) {
        this.priv_set(n);
        return n;
    }

    public synchronized void add(int i) {
        this.set((this.n > 0L ? (int) ((2269700342778490L - this.n) / 31L) : (int) ((this.n + 110157223978885L) / 7L))
                + i);
    }

    public synchronized int get() {
        return this.n > 0L ? (int) ((2269700342778490L - this.n) / 31L) : (int) ((this.n + 110157223978885L) / 7L);
    }

    private void priv_set(int n) {
        if (n % 2 == 0) {
            this.n = 2269700342778490L - (long) n * 31L;
        } else {
            this.n = -110157223978885L + (long) n * 7L;
        }
    }
}
