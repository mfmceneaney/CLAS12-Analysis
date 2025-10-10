
package org.jlab.analysis;

// Java Imports
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.io.*

// Groovy Imports
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic;

// CLAS Physics Imports
import org.jlab.jnp.hipo4.data.*;

/**
* Connects to a remote machine learning model server to classify input data.
*
* @version 1.0
* @author  Matthew McEneaney
*/


@CompileStatic
class MLModelClient {

    private final String _host
    private final int _port
    private final String _endpoint = "/predict"

    protected int _nscores = 2; // Number of expected output scores from model
    protected ArrayList<Bank> _banks = new ArrayList<Bank>();
    protected ArrayList<String> _bankNames = new ArrayList<String>();

    MLModelClient(String host, int port) {
        this._host = host;
        this.port = port;
    }

    /**
    * Get the number of expected output scores from the model.
    * @return The number of output scores.
    */
    int getNScores() {
        return this._nscores;
    }

    /**
    * Set the number of expected output scores from the model.
    * @param nScores The number of output scores.
    */
    void setNScores(int nScores) {
        this._nscores = nScores;
    }
    
    /**
    * Set the list of input bank names to read from each event.
    * @param bankNames The list of bank names to read.
    */
    void setInputBankNames(ArrayList<String> bankNames) {
        this._bankNames = bankNames;
    }

    /**
    * Initialize the input banks to read from each event.
    * @param reader The HIPO reader to get schemas from.
    */
    void createInputBanks(Reader reader) {
        this._banks.clear();

        for (String bankName : this._bankNames) {
            Schema schema = reader.getSchemaFactory().getSchema(bankName);
            if (schema == null) {
                throw new RuntimeException("Schema not found for bank: ${bankName}")
            }
            Bank bank = new Bank(schema);
            this._banks.add(bank);
        }
    }

    /**
    * Create JSON input from the event and configured banks.
    * @param event The event to convert to JSON.
    * @return A JSON string representing the event data.
    */
    String createJsonInput(Event event) {
        Map<String, Object> inputMap = new HashMap<>()
        for (Bank bank : this._banks) {
            event.read(bank);
            List<Map<String, Object>> rows = new ArrayList<>()
            int rowCount = bank.getRows();
            int colCount = bank.getColumns();
            for (int i = 0; i < rowCount; i++) {
                Map<String, Object> rowMap = new HashMap<>()
                for (int j = 0; j < colCount; j++) {
                    String colName = bank.getColumnName(j);
                    Object value = bank.getValue(i, j);
                    rowMap.put(colName, value);
                }
                rows.add(rowMap);
            }
            inputMap.put(bank.getName(), rows);
        }

        JsonBuilder jsonBuilder = new JsonBuilder(inputMap);
        return jsonBuilder.toString();
    }

    /**
    * Classify an event given raw JSON input.
    * @param jsonInput The JSON string representing the input data.
    * @return A list of classification scores.
    */
    List<Double> classify(String jsonInput) {
        String urlString = "http://${this._host}:${this._port}${this._endpoint}"
        URL url = new URL(urlString)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection()

        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        // Write raw JSON input
        OutputStreamWriter writer = new OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8)
        writer.write(jsonInput)
        writer.flush()
        writer.close()

        int responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : ${responseCode}")
        }

        // Read and parse the JSON response
        InputStreamReader reader = new InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)
        Object parsedResponse = new JsonSlurper().parse(reader)
        reader.close()

        if (!(parsedResponse instanceof Map)) {
            throw new RuntimeException("Unexpected response type: ${parsedResponse?.getClass()?.name}")
        }

        Map responseMap = (Map) parsedResponse
        Object predictionsObj = responseMap.get("predictions")
        if (!(predictionsObj instanceof List)) {
            throw new RuntimeException("Invalid response format: 'predictions' not found or not a list.")
        }

        List rawPredictions = (List) predictionsObj
        List<Double> predictions = new ArrayList<>()
        for (Object val : rawPredictions) {
            if (val instanceof Number) {
                predictions.add(((Number) val).doubleValue())
            } else {
                throw new RuntimeException("Invalid prediction value: ${val}")
            }
        }

        return predictions
    }

    /**
    * Classify an event using the configured input banks.
    * @param event The event to classify.
    * @return A list of classification scores.
    */
    List<Double> classify(Event event) {
        String jsonInput = createJsonInput(banks);
        return classify(jsonInput);
    }
}
