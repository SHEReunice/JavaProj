class Example_2 {
    public static void main(String[] args) {
        int n = 10;
        n += (n++) + (++n);
        System.out.println(n);
    }
}