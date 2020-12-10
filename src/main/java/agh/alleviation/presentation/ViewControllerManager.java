package agh.alleviation.presentation;

import agh.alleviation.controller.*;
//import agh.alleviation.controller.edit_dialog.EditDialogController;
//import agh.alleviation.controller.edit_dialog.EditHallDialogController;
//import agh.alleviation.controller.edit_dialog.EditUserDialogController;
import agh.alleviation.model.Hall;
import agh.alleviation.model.user.User;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewControllerManager is responsible for setting up the controllers of the application.
 * It heavily relies on FxWeaver in order to maintain cooperation between JavaFX and Spring Boot.
 * It sets up modal views for adding user and hall.
 *
 * @author Kamil Krzempek
 * @see ScreenSwitcher
 */
public class ViewControllerManager {
    private FxWeaver fxWeaver;
    private Stage primaryStage;
    private ScreenSwitcher screenSwitcher;
    private List<FxControllerAndView<? extends GenericController, Node>> controllersAndViews;

    /**
     * Instantiates a new View controller manager.
     *
     * @param fxWeaver     the fx weaver
     * @param primaryStage the primary stage
     */
    public ViewControllerManager(FxWeaver fxWeaver, Stage primaryStage) {
        this.fxWeaver = fxWeaver;
        this.primaryStage = primaryStage;
    }

    /**
     * Init root layout.
     */
    public void initRootLayout() {
        this.setViewsAndControllers();

        primaryStage.show();
    }

    /**
     * Loads controllers into the FxWeaver and sets them up with corresponding views.
     */

    private FxControllerAndView<? extends GenericController, Node> addToControllersAndViews(Class<? extends GenericController> controller){
        var controllerAndView = fxWeaver.load(controller);
        controllersAndViews.add(controllerAndView);
        return controllerAndView;
    }

    public void setViewsAndControllers() {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        this.screenSwitcher = new ScreenSwitcher(borderPane);

        borderPane.setPrefHeight(400);

        controllersAndViews = new ArrayList<>();

        var menuBar= addToControllersAndViews(MenuController.class);
        var main  = addToControllersAndViews(MainController.class);
        var userList = addToControllersAndViews(UserListController.class);
        var hallList = addToControllersAndViews(HallListController.class);
        var movieList = addToControllersAndViews(MovieListController.class);

        controllersAndViews.forEach(cv -> cv.getController().setAppController(this));

        borderPane.setTop(menuBar.getView().get());

        Pane mainRoot = (Pane) main.getView().get();
        screenSwitcher.addScreen(Screen.MAIN, mainRoot);
        Pane userListRoot = (Pane) userList.getView().get();
        screenSwitcher.addScreen(Screen.USER_LIST, userListRoot);
        Pane hallListRoot = (Pane) hallList.getView().get();
        screenSwitcher.addScreen(Screen.HALL_LIST, hallListRoot);
        Pane movieListRoot = (Pane) movieList.getView().get();
        screenSwitcher.addScreen(Screen.MOVIE_LIST, movieListRoot);
        borderPane.setCenter(mainRoot);

        primaryStage.setScene(scene);
    }

    /**
     * Switch view.
     *
     * @param screen the screen
     */
    public void switchView(Screen screen) {
        this.screenSwitcher.activate(screen);
    }

    /**
     * A helper function for showAddUserDialog and showAddDialog.
     *
     * @param root  root pane
     * @param title title of the window
     * @return a stage for the window
     */
    private Stage setupStageAndScene(Pane root, String title) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        return dialogStage;
    }

    private <Item, Controller extends EditDialogController<Item>> Item showEditItemDialog(Class<Controller> controllerClass, String title, Item item) {
        FxControllerAndView<Controller, Pane> controllerAndView = fxWeaver.load(controllerClass);
        Stage stage = setupStageAndScene(controllerAndView.getView().get(), title);
        Controller controller = controllerAndView.getController();
        controller.setDialogStage(stage);
        if(item != null) controller.setEditedItem(item);
        stage.showAndWait();
        return controller.getEditedItem();
    }

    public User showAddUserDialog() {
        return this.showEditItemDialog(EditUserDialogController.class, "Add user", null);
    }

    public void showEditUserDialog(User user) {
        this.showEditItemDialog(EditUserDialogController.class, "Add user", user);
    }

    public Hall showAddHallDialog() {
        return this.showEditItemDialog(EditHallDialogController.class, "Add hall", null);
    }

    public void showEditHallDialog(Hall hall) {
        this.showEditItemDialog(EditHallDialogController.class, "Edit hall", hall);
    }
}
