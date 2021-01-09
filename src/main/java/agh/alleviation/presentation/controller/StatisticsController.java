package agh.alleviation.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

/**
 * Controller responsible for displaying a screen where user can choose a type of statistics to see.
 *
 * @author Ksenia Fiodarava
 */
@Component
@FxmlView("/views/Statistics.fxml")
public class StatisticsController extends GenericController{

    @FXML
    public Button movies;

    @FXML
    public Button genres;

    /**
     * Shows movie stats.
     * @param event the event
     */
    @FXML
    public void showMovieStats(ActionEvent event){
        viewControllerManager.showMovieStats();
    }

    @FXML
    public void showGenreStats(ActionEvent event){

    }
}
