package pdemanget.lock;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * http://stackoverflow.com/questions/17522343/custom-javafx-webview-protocol-handler
 * http://stackoverflow.com/questions/16215844/javafx-webview-disable-same-origin-policy-allow-cross-domain-requests
 *
 *
 * -Dhttp.proxyHost=10.0.0.100 -Dhttp.proxyPort=8800
 * -Dhttp.nonProxyHosts="localhost|127.0.0.1|10.*.*.*|*.foo.com|etc"
 *
 * TODO: Actions non fournies par webkit: base: - back/next button bind sur
 * alt<- alt-> - refresh bind sur F5 - bookmark bind sur ^D - menu des bookmarks
 * utile: - tabs : ^T - menus : Classement bookmarks, Histo arborescent,
 * Parametres - customisation : gestion de plugin avec une API sur le
 * chargement, l'histo le contenu (filtrage..)
 *
 * @author pdemanget
 *
 */
public class Lock extends Application {

	// private static final String LOGO = "/style/logo.png";
	private ResourceBundle bundle = ResourceBundle.getBundle("i18n/lock", Locale.getDefault());

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws IOException {
		System.out.println("Lock screen");
		// Injector.registerExistingAndInject (this);
		// Injector.setModelOrService(this.getClass (),this);
		stage.setTitle("Lock screen");

		// stage.getIcons().add(new Image(LOGO));
		Parent root = FXMLLoader.load(getClass().getResource("Index.fxml"), bundle);
		Scene scene = new Scene(root);
		//stage.setFullScreen(true);
		stage.setAlwaysOnTop(true);
		  stage.initStyle(StageStyle.UNDECORATED);
		stage.fullScreenExitKeyProperty().set(KeyCombination.NO_MATCH);
		stage.setScene(scene);
		Rectangle2D bounds = allScreens();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		
		stage.setOnCloseRequest(e->e.consume());
		
		stage.show();
		//hideScreens();
		
		
	}
	
	private Rectangle2D allScreens() {
		double minX=0;
		double minY=0;
		double maxX=0;
		double maxY=0;
		
		for( Screen screen:Screen.getScreens()) {
			minX=Math.min(minX,screen.getBounds().getMinX());
			maxX=Math.max(maxX,screen.getBounds().getMaxX());
			minY=Math.min(minY,screen.getBounds().getMinY());
			maxY=Math.max(maxY,screen.getBounds().getMaxY());
		}
		return new Rectangle2D(minX,minY,maxX-minX,maxY-minY);
	}

	private void hideScreens() {
		Screen primary = Screen.getPrimary();
		
		for( Screen screen:Screen.getScreens()) {
			if(!screen.getBounds().equals( primary.getBounds())) {
				hideScreen(screen);
			}
		}
	}

	private void hideScreen(Screen screen) {
		Stage popup = new Stage();
		popup.setAlwaysOnTop(true);
		Rectangle2D bounds = screen.getBounds();
		popup.setFullScreen(true);
		popup.setX(bounds.getMinX());
		popup.setY(bounds.getMinY());
		popup.setWidth(bounds.getWidth());
		popup.setHeight(bounds.getHeight());
		popup.setScene(new Scene(new VBox()));
		popup.setOnCloseRequest(e->{});
		popup.setResizable(false);
		popup.fullScreenExitKeyProperty().set(KeyCombination.NO_MATCH);
		popup.show();
	}

	@Override
	public void stop() {
		System.out.println("exiting");
	}
}
