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
		// root.setStyle("  -fx-background-image: url(\"file:///home/fil/Images/rush/appnwd9_460s.jpg\");");
		root.setOnKeyPressed(e->{
			System.out.println(e.getCode());
			e.consume();
		});
		/*
		text.textProperty().addListener((obs,old,value)->{
			if("exit".equals(value)) {
				System.exit(0);
			}
		});
		*/
	}

	public void go() {
		String value = text.getText();
		
		if(LockManager.instance.check(LockManager.instance.currentUser(), value) ) {
			System.exit(0);
		}
	}

}
