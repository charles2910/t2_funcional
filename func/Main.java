import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n1 = 0, n2 = 0, n3 = 0, n4 = 0;

        try {
            n1 = scanner.nextInt();
            n2 = scanner.nextInt();
            n3 = scanner.nextInt();
            n4 = scanner.nextInt();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }

        String fileName = "../dados.csv";
        List<Country> countryDataList = new ArrayList<Country>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                // discard csv header
                br.readLine();

                //br returns as stream and convert it into a List
                countryDataList = br.lines()
                                    .limit(3)
                                    .map(line -> {
                                            String[] values = line.split(",");
                                            int confirmed = Integer.parseInt(values[1]);
                                            int deaths = Integer.parseInt(values[2]);
                                            int recovery = Integer.parseInt(values[3]);
                                            int active = Integer.parseInt(values[4]);
                                            Country country = new Country(values[0], confirmed, deaths, recovery, active);
                                            return country;
                                                            })
                                    .collect(Collectors.toList());

        } catch (IOException e) {
                e.printStackTrace();
        }

        int sumActiveGreaterThanN1 = 0;
        List<String> deathsGreatestActiveLowestConfirmed = new ArrayList<>();
        List<String> GreatestConfirmed = new ArrayList<>();

        countryDataList.forEach(value -> System.out.println(value.name));

        //sumActiveGreaterThanN1 = br.lines();
    }
}

class Country {
        String name;
        int confirmed = 0;
        int deaths = 0;
        int recovery = 0;
        int active = 0;

        public Country(String name, int confirmed, int deaths, int recovery, int active) {
                this.name = name;
                this.confirmed = confirmed;
                this.deaths = deaths;
                this.recovery = recovery;
                this.active = active;
        }
}
