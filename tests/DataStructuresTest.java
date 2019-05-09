import hercerm.common.ds.*;

import java.util.*;

public class DataStructuresTest {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        countComparisonsTest();
    }

    private static void countComparisonsTest() {
        BSTree<Integer, Integer> bSTree = new BSTree<>();
        AVLTree<Integer, Integer> aVLTree = new AVLTree<>();

        // TODO: 13.04.19 Add 12 and see what happens
        int[] keys = {1, 2, 3, 4};

        for(int k: keys) {
            bSTree.insert(k, null);
            aVLTree.insert(k, null);
        }

        for(int k : bSTree.keys())
            System.out.print(k + "->" + bSTree.countComparisons(k) + " ");
        System.out.println();
        for(int k : aVLTree.keys())
            System.out.print(k + "->" + aVLTree.countComparisons(k) + " ");
    }

    @SuppressWarnings("unchecked")
    private static void SMMAPITest() throws IllegalAccessException, InstantiationException {
        List<Class<SortedMultimap>> multimaps = new LinkedList<>((List<Class<SortedMultimap>>) (List<?>)
                Arrays.asList(BTree.class, AVLTree.class, BSTree.class, LList.class));

        for (Class<SortedMultimap> m : multimaps)
            sortedMultimapTest(m.newInstance());
    }

    @SuppressWarnings("unchecked")
    private static void sortedMultimapTest(SortedMultimap<Integer, Integer> multimap)
            throws IllegalAccessException, InstantiationException {

        System.out.println("MULTIMAP: " + multimap.getClass().getName());

        Integer[] keys = {10, 5, 15, null, 17, 17, 2}; // Test keys

        Random rand = new Random();

        for(Integer key : keys) {
            List<Integer> values = new LinkedList<>();

            values.add(rand.nextInt(10));
            values.add(rand.nextInt(10));

            // Insert
            System.out.printf("Insertion status for key %-5d: %s%n",
                    key, multimap.insert(key, values));
        }

        int targetKey = 90;

        // Null values in insertion
        multimap.insert(targetKey, null);
        multimap.addValue(targetKey, 56);

        // Traversal (keys)
        System.out.print("Traversal (ascending keys): "); System.out.println(multimap.keys());

        // Add value
        System.out.println("Add status upon "  + targetKey  + ": " + multimap.addValue(targetKey, 24));

        // Get values
        System.out.println("Values for key " + targetKey + ": " + multimap.get(targetKey));
        System.out.println("Return status for null get: " + multimap.get(null));

        SortedMultimap<Integer, Integer> emptySMMap = multimap.getClass().newInstance();
        System.out.print("Return status for keys and no keys exist: "); System.out.println(emptySMMap.keys());
        System.out.println("Add value to non existent key: " + emptySMMap.addValue(1, 2));

        System.out.println();
    }
}