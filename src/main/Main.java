public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

    }

    public List<Employee> parseCSV(String[] columns, String filename) {

        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee. class);
        strategy.setColumnMapping(columns);

        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            // Массив считанных строк
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> list = csv.parse();
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }




    }
}
