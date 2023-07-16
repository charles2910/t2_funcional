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

        n1 = scanner.nextInt();
        n2 = scanner.nextInt();
        n3 = scanner.nextInt();
        n4 = scanner.nextInt();

        final int N1 = n1, N2 = n2, N3 = n3, N4 = n4;

        String fileName = "./dados.csv";
        List<Country> countryDataList = new ArrayList<Country>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                // discard csv header
                br.readLine();

                //br returns as stream and convert it into a List
                countryDataList = br.lines()
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


        sumActiveGreaterThanN1 = countryDataList.stream()
                                                .filter(country -> country.confirmed >= N1)
                                                .map(country -> country.active)
                                                .reduce(0, (partialSum, active) -> partialSum + active);
        System.out.println(sumActiveGreaterThanN1);

        countryDataList.stream()
                       .sorted(Comparator.comparing(Country::getActive, Comparator.reverseOrder()))
                       .limit(N2)
                       .sorted(Comparator.comparing(Country::getConfirmed))
                       .limit(N3)
                       .map(country -> country.deaths)
                       .forEach(deaths -> System.out.println(deaths));

        countryDataList.stream()
                       .sorted(Comparator.comparing(Country::getConfirmed, Comparator.reverseOrder()))
                       .limit(N4)
                       .sorted(Comparator.comparing(Country::getName))
                       .map(country -> country.name)
                       .forEach(name -> System.out.println(name));
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

        @Override
        public String toString() {
                return this.name + ", " + this.confirmed + ", " + this.deaths + ", " + this.recovery + ", " + this.active;
        }

        public String getName() {
                return this.name;
        }

        public Integer getConfirmed() {
                return Integer.valueOf(this.confirmed);
        }

        public Integer getActive() {
                return Integer.valueOf(this.active);
        }
}
