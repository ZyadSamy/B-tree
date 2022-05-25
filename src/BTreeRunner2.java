public class BTreeRunner2 {
    public static void main(String[] args) {
        BTree<Integer, String> tree = new BTree<>(3);
        tree.insert(10, "a");
        tree.insert(40, "d");
        tree.insert(10, null);
        tree.insert(30, null);
        tree.insert(33, null);
        tree.insert(50, null);
        tree.insert(204, null);
        tree.insert(4, null);
        tree.insert(60, null);
        tree.insert(44, null);
        tree.insert(401, null);
        tree.insert(123, null);
//        tree.insert(38, null);
//        tree.insert(39, null);
//        tree.insert(42, null);
        tree.print();

        tree.delete(44);
        tree.print();
    }
}
