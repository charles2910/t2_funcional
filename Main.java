import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        String[] numbers = input.split(" ");

        int n1 = 133, n2 = 7, n3 = 0, n4 = 2;

        try {
            n1 = Integer.parseInt(numbers[0]);
            n2 = Integer.parseInt(numbers[1]);
            n3 = Integer.parseInt(numbers[2]);
            n4 = Integer.parseInt(numbers[3]);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }

        String[][] data = csvParser("./dados.csv", ",");

        if (n1 != 0) {
            sumActiveIfConfirmedBiggerThanN1(data, n1);
        }

        if (n2 != 0 && n3 != 0) {
            n2BiggestActiveDeathsOfN3SmallerConfirms(data, n2, n3);
        }

        if (n4 != 0) {
            n4BiggestConfirmsWithOrdNames(data, n4);
        }

    }
    public static String[][] csvParser(String csvFile, String csvSplitBy) {
        String line;
        String[][] data = new String[0][];

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            List<String[]> dataList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                dataList.add(row);
            }

            data = dataList.toArray(new String[dataList.size()][]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // A soma de "Active" de todos os países em que "Confirmed" é maior o igual que n1.
    public static void sumActiveIfConfirmedBiggerThanN1(String[][] data, int n1) {
        int sumActive = 0;

        for (int i = 1; i < data.length; i++) {
            if (Integer.parseInt(data[i][1]) > n1) {
                sumActive += Integer.parseInt(data[i][4]);
            }
        }
        System.out.println(sumActive);
    }

    // Dentre os n2 países com maiores valores de "Active", o "Deaths" dos n3 países com menores valores de "Confirmed".
    public static void n2BiggestActiveDeathsOfN3SmallerConfirms(String[][] data, int n2, int n3) {

        DataSubset dataSubset;

        int[] active = new int[data.length];

        for (int i = 1; i < data.length; i++) {
            active[i] = Integer.parseInt(data[i][4]);
        }

        // Sort the active array in descending order
        sortDesc(active);

        // Get the top n2 active values
        int[] topN2Active = Arrays.copyOfRange(active, 0, n2);

        // Top n2 active data
        dataSubset = dataSubsetter(data, topN2Active,4, 1, n2);

        // From the n2 active data, get the confirmed values
        int[] n2Confirmed = dataSubset.getDataSubset();

        // Sort the confirmed array in ascending order
        sortAsc(n2Confirmed);

        // Get the bottom n3 confirmed values
        int[] bottomN3Confirmed = Arrays.copyOfRange(n2Confirmed, 0, n3);

        dataSubset = dataSubsetter(data, bottomN3Confirmed, 1, 2, n3);
        int[] bottomN3ConfirmedData = dataSubset.getDataSubset();

        for (int i = 0; i < n3; i++) {
            if (bottomN3ConfirmedData[i] != -1)
                System.out.println(bottomN3ConfirmedData[i]);
        }

    }

    //Os n4 países com os maiores valores de "Confirmed". Os nomes devem estar em ordem alfabética.
    public static void n4BiggestConfirmsWithOrdNames(String[][] data, int n4) {

        DataSubset dataSubset;

        int[] confirmed = new int[data.length];

        for (int i = 1; i < data.length; i++) {
            confirmed[i] = Integer.parseInt(data[i][1]);
        }

        // Sort the confirmed array in descending order
        sortDesc(confirmed);

        // Get the top n4 confirmed values
        int[] topN4Confirmed = Arrays.copyOfRange(confirmed, 0, n4);
        dataSubset = dataSubsetter(data, topN4Confirmed, 1, 0, n4);

        // Get the country name of the top n4 confirmed values
        String[] topN2ActiveNames = dataSubset.getName();

        // Sort the country name alphabetically
        Arrays.sort(topN2ActiveNames);

        for (String name: topN2ActiveNames) {
            System.out.println(name);
        }

    }


    public static void sortDesc(int[] array) {
        sort(array, 0, array.length - 1, true);
    }

    public static void sortAsc(int[] array) {
        sort(array, 0, array.length - 1, false);
    }

    private static void sort(int[] array, int low, int high, boolean descending) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, descending);
            sort(array, low, pivotIndex - 1, descending);
            sort(array, pivotIndex + 1, high, descending);
        }
    }

    private static int partition(int[] array, int low, int high, boolean descending) {
        int pivot = array[high];
        int i = low - 1;

        if (descending) {
            for (int j = low; j < high; j++) {
                if (array[j] >= pivot) {
                    i++;
                    swap(array, i, j);
                }
            }
        } else {
            for (int j = low; j < high; j++) {
                if (array[j] <= pivot) {
                    i++;
                    swap(array, i, j);
                }
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * The dataSubsetter method extracts a subset (row) of data based on source row and destine row
     * @param data The original data.
     * @param subset An array of values to match against the source parameter.
     * @param srcParam The index of the source parameter in each data row.
     * @param desParam The index of the destination parameter in each data row.
     * @param n The size of the subset to be extracted.
     * @return A DataSubset object containing the extracted subset of data.
     */
    static DataSubset dataSubsetter(String[][] data, int[] subset, int srcParam, int desParam, int n) {

        DataSubset dataSubset = new DataSubset();

        int j = 0;

        int[] tmpIntSubset = new int[n];
        Arrays.fill(tmpIntSubset, -1);
        String[] tmpStrSubset = new String[n];

        for (int k = 0; k < n; k++) {
            for (int i = 1; i < data.length; i++) {

                if (j == n) {
                    break;
                }

                if (data[i][srcParam].equals(String.valueOf(subset[k]))) {

                    if (desParam == 0) {
                       tmpStrSubset[j++] = data[i][desParam];
                    }

                    else {
                        tmpIntSubset[j++] = Integer.parseInt(data[i][desParam]);
                        };
                    }
                }
            }

        dataSubset.setName(tmpStrSubset);
        dataSubset.setDataSubset(tmpIntSubset);

        return dataSubset;
    }


    public static class DataSubset {
        private String[] name;

        public void setName(String[] name) {
            this.name = name;
        }

        public void setDataSubset(int[] dataSubset) {
            this.dataSubset = dataSubset;
        }

        private int[] dataSubset;

        public String[] getName() {
            return name;
        }

        public int[] getDataSubset() {
            return dataSubset;
        }
    }
}
