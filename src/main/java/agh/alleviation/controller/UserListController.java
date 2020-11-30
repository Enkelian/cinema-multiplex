package agh.alleviation.controller;

import agh.alleviation.model.user.User;
import agh.alleviation.service.UserService;
import agh.alleviation.util.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/views/UserList.fxml")
public class UserListController extends GenericListController {
    UserService userService;

    ObservableList<User> userObservableList;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, UserType> typeColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> loginColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private void initialize() {
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        typeColumn.setCellValueFactory(dataValue -> dataValue.getValue().userTypeProperty());
        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        loginColumn.setCellValueFactory(dataValue -> dataValue.getValue().loginProperty());
        emailColumn.setCellValueFactory(dataValue -> dataValue.getValue().emailProperty());

        this.userObservableList = FXCollections.observableArrayList(userService.getAllUsers());

        userTable.setItems(userObservableList);
    }

    @FXML
    public void handleAddAction(ActionEvent event) {
        User user = this.viewControllerSetup.showAddUserDialog();
        if (user != null) {
            this.userObservableList.add(user);
        }
    }

    @Autowired
    public UserListController(UserService userService) {
        this.userService = userService;
    }
}
