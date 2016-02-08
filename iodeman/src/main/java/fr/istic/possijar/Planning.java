package fr.istic.possijar;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

import javafx.animation.FadeTransition;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Planning implements Initializable {

	@FXML
	MenuButton help1;
	@FXML
	MenuButton help2;
	@FXML
	MenuButton help3;
	@FXML
	Text ok1;
	@FXML
	Text nok1;
	@FXML
	Text ok2;
	@FXML
	Text nok2;
	@FXML
	Text ok3;
	@FXML
	Text nok3;
	@FXML
	Group step1;
	@FXML
	Group step2;
	@FXML
	Group step3;
	@FXML
	Group step4;
	@FXML
	Group step5;
	@FXML
	Button generateBtn;

	@FXML
	DatePicker 	dateDebut = new DatePicker(), 
				dateFin = new DatePicker();
	@FXML
	private ListView<String> listSalles;
	
	@FXML
	private ListView<String> listContraintes;

	protected ListProperty<String> listDesSalles = new SimpleListProperty<>();
	protected List<String> salles = new ArrayList<>();
	protected ListProperty<String> listDesContraintes = new SimpleListProperty<>();
	protected List<String> contraintes = new ArrayList<>();
	String contrainteForte = "Tuteurs";
	
	private String pathDonnees = "/Users/fesnault/POSSICAT/POSSICAT/data/donneesLite2.csv";
	private String pathContraintesEns = "/Users/fesnault/POSSICAT/POSSICAT/data/contraintesEnsLite2.csv";
	private String pathContraintesTut = "/Users/fesnault/POSSICAT/POSSICAT/data/contraintesTuteurLite3.csv";

	private Stage stage;
	private Desktop desktop = Desktop.getDesktop();
	
	final FileChooser fileChooser = new FileChooser();
	
	AlgoPlanningImplV3 algo = new AlgoPlanningImplV3();
	

	public Planning(Stage primaryStage) throws IOException {
		this.stage = primaryStage;
	};

	public void readCSV() throws IOException {
		
		if(pathDonnees.isEmpty() || pathContraintesEns.isEmpty() || pathContraintesTut.isEmpty()) {
			return;
		}
		
		Date debut = Date.from(dateDebut.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fin = Date.from(dateFin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		int nbJours = getWorkingDaysBetweenTwoDates(debut, fin)+1;
		int nbPeriodesParJour = 8;
		
		ObservableList<String> sallesSelectionnees = listSalles.getSelectionModel().getSelectedItems();
		ObservableList<String> contraintesSelectionnees = listContraintes.getSelectionModel().getSelectedItems();
		
		if(contraintesSelectionnees.size()==1) {
			contrainteForte = contraintesSelectionnees.get(0);
		}

		int nbPeriodesEnTout = nbPeriodesParJour*nbJours;
		
		Calendar c = Calendar.getInstance();
		c.set(dateDebut.getValue().getYear(), dateDebut.getValue().getMonthValue(), dateDebut.getValue().getDayOfMonth());
		
		algo.configure(sallesSelectionnees, c, nbPeriodesEnTout, nbPeriodesParJour, pathContraintesEns, pathContraintesTut, pathDonnees, contrainteForte);
		algo.execute();
		File f = algo.getFile();
		desktop.open(f);
		//desktop.open(new File(System.getProperty("user.home")+"/Downloads/generatedCSV.csv"));
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * Gestion des salles
		 */
		salles.add("i50");
		salles.add("i51");
		salles.add("Jersey");
		salles.add("Guernesey");
		listSalles.itemsProperty().bind(listDesSalles);
		listSalles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listDesSalles.set(FXCollections.observableArrayList(salles));
		
		/**
		 * Gestion des contraintes
		 */
		contraintes.add("Tuteurs");
		contraintes.add("Horaires");
		listContraintes.itemsProperty().bind(listDesContraintes);
		listDesContraintes.set(FXCollections.observableArrayList(contraintes));
		
		/**
		 * Gestion des dates
		 */
		dateDebut.setValue(LocalDate.now());

		/**
		 * Gestion des tooltips
		 */
		Image imgDonnees = new Image(getClass().getResource("/donnees.png").toString());
		ImageView helpDonnees = new ImageView(imgDonnees);
		Image imgContraintesEns = new Image(getClass().getResource("/contraintesEns.png").toString());
		ImageView helpContraintesEns = new ImageView(imgContraintesEns);
		Image imgContraintesTut = new Image(getClass().getResource("/contraintesTut.png").toString());
		ImageView helpContraintesTut = new ImageView(imgContraintesTut);
		final MenuItem helpPopup1 = new MenuItem();
		final MenuItem helpPopup2 = new MenuItem();
		final MenuItem helpPopup3 = new MenuItem();
		helpPopup1.setGraphic(helpDonnees);
		helpPopup2.setGraphic(helpContraintesEns);
		helpPopup3.setGraphic(helpContraintesTut);
		help1.getItems().setAll(helpPopup1);
		help2.getItems().setAll(helpPopup2);
		help3.getItems().setAll(helpPopup3);
		
		listSalles.setDisable(false);
		launchStep5();
		launchStep6();
	}
	
	public void validDate() {
		Calendar c = Calendar.getInstance();
		c.set(dateDebut.getValue().getYear(), dateDebut.getValue().getMonthValue(), dateDebut.getValue().getDayOfMonth());
		c.add(Calendar.DATE, 4);
		dateFin.setValue(LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
	}
	
	public void openJeuDonnees() {

		//System.err.println(listSalles.getItems());
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            //System.err.println(file.getAbsolutePath());
            CSVParser parser = new CSVParser();
            int checkData = parser.checkData(file.getAbsolutePath());
            if(checkData > 0) {
                pathDonnees = file.getAbsolutePath();
				FadeTransition ft = new FadeTransition(Duration.millis(1000), ok1);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				ok1.setText("Succès, "+checkData+" soutenances importées");

				// On passe à l'étape 2
				FadeTransition ft2 = new FadeTransition(Duration.millis(1000), step1);
				ft2.setFromValue(1.0);
				ft2.setToValue(0.5);
				ft2.play();
				step2.setDisable(false);
				FadeTransition ft3 = new FadeTransition(Duration.millis(1000), step2);
				ft3.setFromValue(0.5);
				ft3.setToValue(1.0);
				ft3.play();
            }
            else {
            	// If < 0, shows a mistake
				nok1.setText("Échec, fichier non valide");
				FadeTransition ft = new FadeTransition(Duration.millis(1000), nok1);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				new java.util.Timer().schedule(
						new java.util.TimerTask() {
							@Override
							public void run() {
								FadeTransition ft = new FadeTransition(Duration.millis(1000), nok1);
								ft.setFromValue(1.0);
								ft.setToValue(0.0);
								ft.play();
							}
						},
						3000
				);
            }

        }
	}
	
	public void openContraintesEns() {
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            CSVParser parser = new CSVParser();
            int checkData = parser.checkContraintes(file.getAbsolutePath());
            if(checkData < 0) {
                pathContraintesEns = file.getAbsolutePath();
				FadeTransition ft = new FadeTransition(Duration.millis(1000), ok2);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				ok2.setText("Succès");

				// On passe à l'étape 3
				FadeTransition ft3 = new FadeTransition(Duration.millis(1000), step2);
				ft3.setFromValue(1.0);
				ft3.setToValue(0.5);
				ft3.play();
				step3.setDisable(false);
				FadeTransition ft4 = new FadeTransition(Duration.millis(1000), step3);
				ft4.setFromValue(0.5);
				ft4.setToValue(1.0);
				ft4.play();

            }
            else {
            	// If > 0, it means error on this line, we add one because count starts from zero in dev
				FadeTransition ft = new FadeTransition(Duration.millis(1000), nok2);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				nok2.setText("Échec, erreur à la ligne "+checkData);
				new java.util.Timer().schedule(
						new java.util.TimerTask() {
							@Override
							public void run() {
								FadeTransition ft = new FadeTransition(Duration.millis(1000), nok2);
								ft.setFromValue(1.0);
								ft.setToValue(0.0);
								ft.play();
							}
						},
						3000
				);
            }

        }
	}
	
	public void openContraintesTut() {
		File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            CSVParser parser = new CSVParser();
            int checkData = parser.checkContraintes(file.getAbsolutePath());
            if(checkData < 0) {
                pathContraintesTut = file.getAbsolutePath();
				FadeTransition ft = new FadeTransition(Duration.millis(1000), ok3);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				ok3.setText("Succès");

				// On passe à l'étape 4
				FadeTransition ft2 = new FadeTransition(Duration.millis(2000), step3);
				ft2.setFromValue(1.0);
				ft2.setToValue(0.5);
				ft2.play();
				step4.setDisable(false);
				FadeTransition ft3 = new FadeTransition(Duration.millis(2000), step4);
				ft3.setFromValue(0.5);
				ft3.setToValue(1.0);
				ft3.play();
				launchStep5();
				launchStep6();
            }
            else {
            	// If > 0, it means error on this line, we add one because count starts from zero in dev
				FadeTransition ft = new FadeTransition(Duration.millis(1000), nok3);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				nok3.setText("Échec, erreur à la ligne "+checkData);
				new java.util.Timer().schedule(
						new java.util.TimerTask() {
							@Override
							public void run() {
								FadeTransition ft = new FadeTransition(Duration.millis(1000), nok3);
								ft.setFromValue(1.0);
								ft.setToValue(0.0);
								ft.play();
							}
						},
						3000
				);
            }
        }
	}
	
	public int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
	    Calendar startCal;
	    Calendar endCal;
	    startCal = Calendar.getInstance();
	    startCal.setTime(startDate);
	    endCal = Calendar.getInstance();
	    endCal.setTime(endDate);
	    int workDays = 0;
	 
	    //Return 0 if start and end are the same
	    if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
	        return 0;
	    }
	 
	    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
	        startCal.setTime(endDate);
	        endCal.setTime(startDate);
	    }
	 
	    do {
	        startCal.add(Calendar.DAY_OF_MONTH, 1);
	        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY 
	       && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	            ++workDays;
	        }
	    } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
	 
	    return workDays;
	}

	public void launchStep5() {
		step5.setDisable(false);
		step5.setOpacity(1.0);
	}

	public void launchStep6() {
		generateBtn.setDisable(false);
		generateBtn.setOpacity(1.0);
	}

}
