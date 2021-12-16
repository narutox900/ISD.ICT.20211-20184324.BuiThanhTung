package views.screen.shipping;

import common.exception.InvalidDeliveryInfoException;
import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.RushOrder;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ShippingRushOrderScreenHandler extends ShippingScreenHandler {

  @FXML
  private TextField deliveryTime;

  private RushOrder rushOrder;

  public ShippingRushOrderScreenHandler(Stage stage, String screenPath, RushOrder rushOrder)
      throws IOException {
    super(stage, screenPath, rushOrder);
    this.rushOrder = rushOrder;
  }

  public PlaceRushOrderController getBaseController() {
    return (PlaceRushOrderController) super.getBaseController();
  }


  @FXML
  void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

    HashMap messages = addInfoToMessage();
    messages.put("deliveryTime", deliveryTime.getText());
    Boolean validateInfoResult;
    errorMessage.setText("");

    try {
      // process and validate delivery info
      validateInfoResult = getBaseController().processDeliveryInfo(messages);
    } catch (InvalidDeliveryInfoException e) {
      throw new InvalidDeliveryInfoException(e.getMessage());
    }

    if (validateInfoResult.isEmpty()) {
      calculateShippingFee(messages);
      rushOrder.setDeliveryInfo(messages);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

      String time = deliveryTime.getText();
      if (!time.isEmpty()) {
        rushOrder.setDeliveryTime(LocalDateTime.parse(deliveryTime.getText(), formatter));
      }
      // if there are some normal items left
      Order order = getBaseController().createOrder();
      if (order.getLstOrderMedia().size() > 0) {
        ShippingScreenHandler ShippingScreenHandler = new ShippingScreenHandler(this.stage,
            Configs.SHIPPING_SCREEN_PATH, order);
        ShippingScreenHandler.setPreviousScreen(this);
        ShippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
        ShippingScreenHandler.setScreenTitle("Shipping Screen");
        ShippingScreenHandler.setBController(new PlaceOrderController());
        ShippingScreenHandler.show();
      } else {
        // create invoice screen
        Invoice invoice = getBaseController().createInvoice(rushOrder);

        BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage,
            Configs.INVOICE_SCREEN_PATH, invoice);
        InvoiceScreenHandler.setPreviousScreen(this);
        InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
        InvoiceScreenHandler.setScreenTitle("Invoice Screen");
        InvoiceScreenHandler.setBController(getBaseController());
        InvoiceScreenHandler.show();
      }

    } else {
      errorMessage.setText(validateInfoResult);
    }
  }


  public void calculateShippingFee(HashMap messages) {
    // calculate shipping fees
    int shippingFees = getBaseController().calculateShippingFee(rushOrder);
    rushOrder.setShippingFees(shippingFees);
    rushOrder.setDeliveryInfo(messages);
  }


  public void createInvoiceScreen() throws IOException {

    // create invoice screen
    Invoice invoice = getBaseController().createInvoice(rushOrder);
    BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage,
        Configs.INVOICE_SCREEN_PATH, invoice);
    InvoiceScreenHandler.setPreviousScreen(this);
    InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
    InvoiceScreenHandler.setScreenTitle("Invoice Screen");
    InvoiceScreenHandler.setBController(getBaseController());
    InvoiceScreenHandler.show();
  }
}
