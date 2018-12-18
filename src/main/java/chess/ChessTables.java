package chess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChessTables {

   public ChessTables() {
   }

   public static void convertCsvToXml(String file) throws Exception {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder parser = factory.newDocumentBuilder();
      Document doc = parser.newDocument();

      Element table = doc.createElement("table");
      table.setAttribute("class", "tablecev");
      doc.appendChild(table);

      String csvfile = file + ".csv";
      String line = "";
      BufferedReader bufferedReader = new BufferedReader(new FileReader(csvfile));

      int counter = 0;

      for (Element tbody = null; (line = bufferedReader.readLine()) != null; ++counter) {
         String[] ligne = line.split(";");
         int i;
         if (counter != 0) {
            Element row = doc.createElement("tr");
            tbody.appendChild(row);

            for (i = 0; i < ligne.length; ++i) {
               Element td = doc.createElement("td");
               td.setTextContent(ligne[i]);
               row.appendChild(td);
            }
         } else {
            Element thead = doc.createElement("thead");
            table.appendChild(thead);

            for (i = 0; i < ligne.length; ++i) {
               Element th = doc.createElement("th");
               th.setTextContent(ligne[i]);
               thead.appendChild(th);
            }

            tbody = doc.createElement("tbody");
            table.appendChild(tbody);
         }
      }

      bufferedReader.close();
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty("encoding", "ISO-8859-1");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty("indent", "yes");
      DOMSource source = new DOMSource(doc);
      LocalDate date = LocalDate.now();

      String nouveau = file + "-" + date + ".xml";

      FileWriter fileWriter = new FileWriter(nouveau);
      StreamResult result = new StreamResult(fileWriter);
      transformer.transform(source, result);
      fileWriter.close();
   }


}
