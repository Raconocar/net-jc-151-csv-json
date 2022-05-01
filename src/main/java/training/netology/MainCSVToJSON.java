package training.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainCSVToJSON {
    private static FileWriter file;

    public static void main(String[] args) {


        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        try {
            List<Employee> listCSV = parseCSV(columnMapping, fileName);
            writeToJSONFile("dataCSV.json", listCSV);
        } finally {

        }



        try {
            List<Employee> listXML = parseXML("data.xml");
            writeToJSONFile("dataXML.json", listXML);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }

    public static List<Employee> parseCSV(String[] columns, String filename) {

        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columns);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> date = csv.parse();
            return date;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Employee> parseXML(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse("data.xml");
        Node root = document.getDocumentElement();

        return read(root);
    }

    public static List<Employee> read(Node root) {
        List<Employee> list = null;
        list = new ArrayList<>();
        NodeList staff = root.getChildNodes();
        for (int i = 0; i < staff.getLength(); i++) {
            Node employee = staff.item(i);

            long id = 0;
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;

            if (employee.getNodeType() != Node.TEXT_NODE) {
                NodeList employeer = employee.getChildNodes();
                for (int j = 0; j < employeer.getLength(); j++) {
                    Node employeersDetails = employeer.item(j);
                    if (employeersDetails.getNodeType() != Node.TEXT_NODE) {
                        switch (employeersDetails.getNodeName()) {
                            case "id":
                                id = Integer.parseInt(employeersDetails.getChildNodes().item(0).getTextContent()); break;
                            case "firstName":
                                firstName = employeersDetails.getChildNodes().item(0).getTextContent();break;
                            case "lastName":
                                lastName = employeersDetails.getChildNodes().item(0).getTextContent();break;
                            case "country":
                                country = employeersDetails.getChildNodes().item(0).getTextContent();break;
                            case "age":
                                age = Integer.parseInt(employeersDetails.getChildNodes().item(0).getTextContent());break;
                        }
                    }
                }
                list.add(new Employee(id, firstName, lastName, country, age));
            }
        }

        return list;
    }

    public static void writeToJSONFile(String filename, List list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            file = new FileWriter(filename);
            file.write(gson.toJson(list));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}