package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	if (this.txtAnno.getText() == null) {
    		this.txtResult.appendText("Devi prima inserire un anno!\n");
    		return;
    	}
    	if (this.cmbBoxForma.getValue() == null) {
    		this.txtResult.appendText("Devi prima inserire una forma!\n");
    		return;
    	}
    	this.model.creaGrafo(Integer.parseInt(this.txtAnno.getText()), this.cmbBoxForma.getValue());
    	Map<State,Integer> mappaStatiPesi = new HashMap<>(this.model.getSommaPesi());
    	this.txtResult.appendText("Ecco i risultati della creazione del grafo: \n");
    	for (State s : mappaStatiPesi.keySet()) {
    		this.txtResult.appendText("Per lo stato "+s.toString()+" la somma dei pesi degli archi adiacenti e': "+mappaStatiPesi.get(s)+"\n");
    	}
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {

    	this.txtResult.clear();
    	try {
    		int year = Integer.parseInt(this.txtAnno.getText());
    		
    		if (year < 1910 || year > 2014) {
    			this.txtResult.appendText("L'anno deve essee compreso tra il 1910 e il 2014, estremi inclusi!");
    			return;
    		}
    		
    		List<String> forme = new ArrayList<>(this.model.getAllShapes(year));
    		this.cmbBoxForma.getItems().addAll(forme);
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire un anno valido!");
    		return;
    	}
    	
    }

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
