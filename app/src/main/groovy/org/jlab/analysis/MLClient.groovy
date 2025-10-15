
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
import org.jlab.jnp.hipo4.io.HipoReader;

/**
* Connects to a remote machine learning model server to classify input data.
*
* @version 1.0
* @author  Matthew McEneaney
*/


@CompileStatic
public class MLClient {

    private final String _host
    private final int _port
    private final String _endpoint = "/predict"

    protected int _nscores = 2; // Number of expected output scores from model
    protected ArrayList<Bank> _banks = new ArrayList<Bank>();
    protected ArrayList<String> _bankNames = new ArrayList<String>();

    MLClient(String host, int port) {
        this._host = host;
        this._port = port;
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
    void createInputBanks(HipoReader reader) {
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
        Map<String, Object> inputMap = new HashMap<>();

        for (Bank bank : this._banks) {
            event.read(bank);
            ArrayList<ArrayList<Object>> rows = new ArrayList<>();
            int rowCount = bank.getRows();
            Schema schema = bank.getSchema();
            ArrayList<String> entryArray = (ArrayList<String>)schema.getEntryList();
            // System.out.println("Processing bank: ${schema.getName()} with ${rowCount} rows and entries: ${entryArray}");
            for (String entry : entryArray) {
                ArrayList<Object> row_array = new ArrayList<>();
                Integer entry_type = schema.getType(entry);
                for (int i = 0; i < rowCount; i++) {
                    Object value;
                    switch (entry_type) {
                        case 1: // byte
                            value = bank.getByte(entry, i);
                            break;
                        case 2: // short
                            value = bank.getShort(entry, i);
                            break;
                        case 3: // int
                            value = bank.getInt(entry, i);
                            break;
                        case 4: // float
                            value = bank.getFloat(entry, i);
                            break;
                        case 5: // double
                            value = bank.getDouble(entry, i);
                            break;
                        case 8: // long
                            value = bank.getLong(entry, i);
                            break;
                        default:
                            throw new RuntimeException("Unsupported entry type: ${entry_type} for entry: ${entry}");
                            break;
                    }
                    row_array.add(value);
                }
                rows.add(row_array);
            }
            inputMap.put(schema.getName(), rows);
        }

        JsonBuilder jsonBuilder = new JsonBuilder(inputMap);
        return jsonBuilder.toString();
    }

    /**
    * Classify an event given raw JSON input.
    * @param jsonInput The JSON string representing the input data.
    * @return A list of classification scores.
    */
    ArrayList<Double> classify(String jsonInput) {
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

        ArrayList<Object> rawPredictions = (ArrayList) predictionsObj
        ArrayList<Double> predictions = new ArrayList<>()
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
    ArrayList<Double> classify(Event event) {
        String jsonInput = this.createJsonInput(event);
        return this.classify(jsonInput);
    }
} // public class MLClient {
