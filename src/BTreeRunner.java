public class BTreeRunner {
    public static void main(String[] args) {
        BTree<Integer, String> tree = new BTree<>(3);
        tree.insert(10, "a");
        tree.insert(20, "b");
        tree.insert(30, "c");
        tree.insert(40, "d");
        tree.insert(50, "e");
        tree.insert(60, "f");
        tree.insert(70, "g");
        tree.insert(80, "j");
//        tree.insert(90, "k");

        System.out.println(tree.search(10));
        System.out.println(tree.search(20));
        System.out.println(tree.search(30));
        System.out.println(tree.search(40));
        System.out.println(tree.search(50));
        System.out.println(tree.search(60));
        System.out.println(tree.search(70));
        System.out.println(tree.search(80));
        System.out.println(tree.search(90));

        tree.print();
        System.out.println(tree.delete(80));
        tree.print();
        System.out.println(tree.delete(20));
    }
}
