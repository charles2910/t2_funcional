import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

//        String input = scanner.nextLine();
//        String[] numbers = input.split(" ");

        int n1 = 1000000, n2 = 10, n3 = 4, n4 = 2;

        try {
//            n1 = Integer.parseInt(numbers[0]);
//            n2 = Integer.parseInt(numbers[1]);
//            n3 = Integer.parseInt(numbers[2]);
//            n4 = Integer.parseInt(numbers[3]);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }

        String[][] data = csvParser("./dados.csv", ",");
        sumActiveIfConfirmedBiggerThanN1(data, n1);
        n2BiggestActiveDeathsOfN3SmallerConfirms(data, n2, n3);
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
        int[] active = new int[data.length];

        for (int i = 1; i < data.length; i++) {
            active[i] = Integer.parseInt(data[i][4]);
        }

        // Sort the active array in descending order
        Arrays.sort(active);
        Collections.reverse(Arrays.asList(active));

        // Get the top n2 active values
        int[] topN2Active = Arrays.copyOfRange(active, 1, n2 + 1);

        // Top n2 active data
        int[][] topN2ActiveData = dataSubsetter(data, topN2Active, n2);


        // From the n2 active data, get the confirmed values
        int[] n2Confirmed = new int[n2];

        for (int i = 0; i < n2; i++) {
            n2Confirmed[i] = topN2ActiveData[i][0];
        }

        // Sort the confirmed array in ascending order
        for (int value : n2Confirmed) {
            System.out.println(value);
        }

        Arrays.sort(n2Confirmed);

        System.out.println("Sorted");


        for (int value : n2Confirmed) {
            System.out.println(value);
        }

        // Get the bottom n3 confirmed values
        int[] bottomN3Confirmed = Arrays.copyOfRange(n2Confirmed, 0, n3);

        int[][] bottomN3ConfirmedData = dataSubsetter(data, bottomN3Confirmed, n3);


        int sumDeaths = 0;

        for (int i = 0; i < n3; i++) {
            sumDeaths += bottomN3ConfirmedData[i][1];
        }

        System.out.println(sumDeaths);
    }

    static int[][] dataSubsetter(String[][] data, int[] subset, int n) {

        int[][] dataSubset = new int[n][4];
        int j = 0;

        for (int k = 0; k < n; k++) {
            for (int i = 1; i < data.length; i++) {
                if (data[i][4].equals(String.valueOf(subset[k]))) {

                    dataSubset[j++] = new int[] {
                            Integer.parseInt(data[i][1]),
                            Integer.parseInt(data[i][2]),
                            Integer.parseInt(data[i][3]),
                            Integer.parseInt(data[i][4])
                    };

                }
            }
        }

        return dataSubset;
    }
}
