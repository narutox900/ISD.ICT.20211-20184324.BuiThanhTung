package views.screen;

import java.io.IOException;
import java.util.Hashtable;

import controller.BaseController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.screen.home.HomeScreenHandler;

public class BaseScreenHandler extends FXMLScreenHandler {

  private Scene scene;
  private BaseScreenHandler prev;
  protected final Stage stage;
  protected HomeScreenHandler homeScreenHandler;
  protected Hashtable<String, String> messages;
  private BaseController baseController;

  private BaseScreenHandler(String screenPath) throws IOException {
    super(screenPath);
    this.stage = new Stage();
  }

  public void setPreviousScreen(BaseScreenHandler prev) {
    this.prev = prev;
  }

  public BaseScreenHandler getPreviousScreen() {
    return this.prev;
  }

  public BaseScreenHandler(Stage stage, String screenPath) throws IOException {
    super(screenPath);
    this.stage = stage;
  }

  public void show() {
    if (this.scene == null) {
      this.scene = new Scene(this.content);
    }
    this.stage.setScene(this.scene);
    this.stage.show();
  }

  public void setScreenTitle(String string) {
    this.stage.setTitle(string);
  }

  public void setBController(BaseController bController) {
    this.baseController = bController;
  }

  public BaseController getBaseController() {
    return this.baseController;
  }

  public void forward(Hashtable messages) {
    this.messages = messages;
  }

  public void setHomeScreenHandler(HomeScreenHandler HomeScreenHandler) {
    this.homeScreenHandler = HomeScreenHandler;
  }

//	public void createInvoiceScreen (Order order, RushOrder rushOrder) throws IOException {
//
//		// create invoice screen
//		Invoice invoice = getBController().createInvoice(order, rushOrder);
//
//		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
//		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
//		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
//		InvoiceScreenHandler.setBController(getBController());
//		InvoiceScreenHandler.show();
//	}
}
