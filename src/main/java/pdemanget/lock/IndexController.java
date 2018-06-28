package pdemanget.lock;


import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

public class IndexController {

	@FXML
	private VBox root;
	@FXML
	private TextField text;
	
	public void initialize() {
		/*
		root.setOnKeyPressed(e->{
			System.out.println(e.getCode());
			e.consume();
		});
		
		text.textProperty().addListener((obs,old,value)->{
			if("exit".equals(value)) {
				System.exit(0);
			}
		});
		*/
	}

	public void go() {
		String value = text.getText();
		
		if("exit".equals(value)) {
			System.exit(0);
		}
	}

}
