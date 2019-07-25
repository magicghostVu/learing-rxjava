package testfx;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import logging.LoggingService;

import java.net.URL;
import java.util.ResourceBundle;

public class MController implements Initializable {


    @FXML
    private CheckBox checkBox;

    @FXML
    private Label lableStatusCheck;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //LoggingService.getInstance().getLogger().info("checkbox is {}", checkBox);


        //JavaFxObservable.


        var booleanProperties = checkBox.selectedProperty();

        var o1 = Observable.<Boolean>create(observableEmitter -> {
            //observableEmitter.onNext(booleanProperties.getValue());
            ChangeListener<Boolean> l = (ov, pre, cr) -> {
                observableEmitter.onNext(cr);
            };
            booleanProperties.addListener(l);
        });

        o1.subscribe(val -> {
            LoggingService.getInstance().getLogger().info("val is {}", val);
        });

        var observable = JavaFxObservable.actionEventsOf(checkBox).map(e -> {
            CheckBox c = (CheckBox) e.getSource();


            return c.isSelected();
        });/*.subscribe(t -> {
            LoggingService.getInstance().getLogger().info("i is {}", t);
            if (t) {
                lableStatusCheck.setText("Checkbox is checked");
            } else {
                lableStatusCheck.setText("checkbox is unchecked");
            }
        });*/

        LoggingService.getInstance().getLogger().info("o1 is {}, o2 is {}", o1, observable);

    }
}
