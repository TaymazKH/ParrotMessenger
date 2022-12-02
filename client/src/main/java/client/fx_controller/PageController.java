package client.fx_controller;

import client.agent.GraphicalAgent;
import client.offline.event.Event;
import client.page_data.PageData;
import client.util.Dialogs;
import client.util.PageDataType;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import shared.request.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

public abstract class PageController implements Initializable {
    private static GraphicalAgent graphicalAgent;
    private PageDataType pageDataType;
    private static boolean isDialogOpen = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static GraphicalAgent getGraphicalAgent() {
        return graphicalAgent;
    }
    public PageDataType getPageDataType() {
        return pageDataType;
    }

    public static void setGraphicalAgent(GraphicalAgent graphicalAgent) {
        PageController.graphicalAgent = graphicalAgent;
    }
    public void setPageDataType(PageDataType pageDataType) {
        this.pageDataType = pageDataType;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void passSelf(){
        graphicalAgent.setCurrentPageController(this);
    }
    public byte[] selectImage(){
        block();
        byte[] bytes = null;
        try{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files", "*.png");
            fileChooser.getExtensionFilters().addAll(extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            if(file!=null) bytes = Files.readAllBytes(file.toPath());
        } catch(Exception ignored){}
        allow();
        return bytes;
    }
    public Image convertImage(byte[] imageData){
        if(imageData==null) return null;
        ImageIcon imageIcon = new ImageIcon(imageData);
        BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(),imageIcon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        imageIcon.paintIcon(null,g,0,0);
        g.dispose();
        return SwingFXUtils.toFXImage(bufferedImage,null);
    }
    public String showInputDialog(String dialog){
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(Dialogs.get(dialog));
        inputDialog.showAndWait();
        return inputDialog.getEditor().getText();
    }

    public void sendRequest(Request request){
        if(!isDialogOpen) graphicalAgent.sendRequest(request);
    }
    public void runEvent(Event event){
        if(!isDialogOpen) graphicalAgent.runEvent(event);
    }
    public abstract void update(PageData pageData);

    public void allow(){
        isDialogOpen = false;
    }
    public void block(){
        isDialogOpen = true;
    }
}
