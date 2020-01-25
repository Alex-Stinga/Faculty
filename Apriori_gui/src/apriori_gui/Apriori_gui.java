/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apriori_gui;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Alex
 */
public class Apriori_gui extends Application {
    
    private Button go = new Button("Go"), file = new Button("Alege fisier");
    private Label minSup = new Label("Suport minim"), confMin = new Label("Conf minima"),  nrAssoc = new Label("Nr. associeri");
    private TextField filePath = new TextField(), inputConf = new TextField(), inputMinSup = new TextField(), inputAssoc = new TextField();
    private TextArea outputText = new TextArea();
    private VBox container = new VBox();
    private HBox topContainer = new HBox(filePath, file);
    
    private File tmp = null;
    private String  min, conf, nr;
    private boolean hasFile = false;
    
    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,700,500);
        initWindow();
        root.setTop(topContainer);
        root.setLeft(container);
        root.setCenter(outputText);
        
        actions();
        primaryStage.setTitle("Apriori");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void initWindow(){

        HBox sup = new HBox(minSup, inputMinSup);
        HBox conf = new HBox(confMin, inputConf);
        HBox assoc = new HBox(nrAssoc, inputAssoc);     
        container.getChildren().addAll(sup,conf,assoc,go);
        
        topContainer.setPadding(new Insets(10, 10, 10, 10));
        topContainer.setSpacing(10);
        filePath.setEditable(false);
     
        sup.setSpacing(10);
        conf.setSpacing(10);
        assoc.setSpacing(10);
        container.setSpacing(10);
        container.setPadding(new Insets(10, 10,10, 10));
        container.setAlignment(Pos.TOP_LEFT);
        
        outputText.setPrefWidth(100);
        outputText.setPrefHeight(300);
        outputText.setEditable(false);
    }
    
    private void actions(){
        go.setOnAction((event) -> {
            min= inputMinSup.getText();
            conf =inputConf.getText();
            nr =inputAssoc.getText();
            
            if(validateInput() && hasFile){
                if( (Integer.valueOf(nr) <=20) && (Double.valueOf(conf)/100 > Double.valueOf(min)/100) )
                    apriori_run();
                else{
                    errorWindow("Confidenta trebuie sa fie mai mare decat suportul minim si nr de reguli < 20.");
                }
            }
            else
                errorWindow("Valorile din input trebuie sa fie de tip int.");
                
        //System.out.println( inputMinSup.getText()+ " "+ conf+" "+ nr +" "+ Integer.valueOf(min));
        });

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arff file", "*.arff");
        fileChooser.getExtensionFilters().add(extFilter);
        
        file.setOnAction((event) -> {
            tmp = fileChooser.showOpenDialog(null);
            //System.out.println(tmp.getAbsolutePath());
            if(tmp != null){
                filePath.setText(tmp.getAbsolutePath());
                hasFile = true;
            }
        });
        
    }
    
    private void apriori_run(){
        
        Apriori apriori = new Apriori();		
        apriori.setNumRules(Integer.valueOf(nr));
        apriori.setMinMetric(Double.valueOf(conf)/100);
        apriori.setLowerBoundMinSupport(Double.valueOf(min)/100);
        
        Instances records = readfile();
        String tmp = null;
        try {
            apriori.buildAssociations(records);
            tmp = apriori.toString();
        } catch (Exception ex) {
            Logger.getLogger(Apriori_gui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        tmp += "Nr inistante: " +  String.valueOf(records.numInstances()) +'\n';
//        List<AssociationRule> rules =  apriori.getAssociationRules().getRules();
//        for(AssociationRule rule: rules) {  
//            tmp += rule;
//            tmp += '\n';
//        }
        outputText.setText(tmp);
    }
    
    private Boolean validateInput(){
        return min.matches("^[0-9]+$") && conf.matches("^[0-9]+$") && nr.matches("^[0-9]+$");
    }
    
     private void errorWindow(String text){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private Instances readfile(){

        ConverterUtils.DataSource source = null;
        try {
            source = new ConverterUtils.DataSource(tmp.getAbsolutePath());
        } catch (Exception ex) {
            Logger.getLogger(Apriori_gui.class.getName()).log(Level.SEVERE, null, ex);
        }
        Instances records = null;
        
        try {
            records = source.getDataSet();
        } catch (Exception ex) {
            Logger.getLogger(Apriori_gui.class.getName()).log(Level.SEVERE, null, ex);
        }
	return records;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
