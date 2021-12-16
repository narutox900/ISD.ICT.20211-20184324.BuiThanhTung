package controller;

import common.exception.InvalidDeliveryInfoException;
import common.exception.UnsupportedRushException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.OrderMedia;
import entity.order.RushOrder;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class controls the flow of place rush order usecase in our AIMS project.
 *
 * @author tungbt
 */

public class PlaceRushOrderController extends PlaceOrderController {

  /**
   * Just for logging purpose.
   */
  private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

  /**
   * This method checks the avalibility of product when user click PlaceOrder button.
   *
   * @throws SQLException DB error
   */
  public void placeOrder() throws SQLException {
    Cart.getCart().checkAvailabilityOfProduct();
  }

  /**
   * This method creates the new RushOrder based on the Cart.
   *
   * @return RushOrder
   * @throws SQLException DB error
   */
  public RushOrder createRushOrder() throws SQLException {
    RushOrder order = new RushOrder();
    for (Object object : Cart.getCart().getListMedia()) {
      CartMedia cartMedia = (CartMedia) object;
      if (cartMedia.supportRush()) {
        OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
            cartMedia.getQuantity(),
            cartMedia.getPrice());
        order.getlstOrderMedia().add(orderMedia);
      }
    }
    if (order.getAmount() == 0) {
      throw new UnsupportedRushException("Cart doesn't have any items that support rush order");
    }
    return order;
  }

  /**
   * This method creates the new Invoice based on order.
   *
   * @param order RushOrder
   * @return Invoice Invoice
   */
  public Invoice createInvoice(RushOrder order) {
    return new Invoice(order);
  }

  /**
   * This method takes responsibility for processing the shipping info from user.
   *
   * @param info Hashmap
   * @return
   * @throws InterruptedException Thread errors
   * @throws IOException          IO errors
   */
  public boolean processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
    LOGGER.info("Process Delivery Info");
    LOGGER.info(info.toString());
    return validateDeliveryInfo(info);
  }

  /**
   * The method validates the delivery info.
   *
   * @param info Hashmap
   * @throws InterruptedException Thread errors
   * @throws IOException          IO errors
   */
  public boolean validateDeliveryInfo(HashMap<String, String> info)
      throws InterruptedException, IOException {
    if (!validateAddress(info.get("address"))) {
      throw new InvalidDeliveryInfoException("Invalid address!");
    }
    if (!validateName(info.get("name"))) {
      throw new InvalidDeliveryInfoException("Invalid name!");
    }
    if (!validatePhoneNumber(info.get("phoneNumber"))) {
      throw new InvalidDeliveryInfoException("Invalid phone number!");
    }
    if (!validateProvince(info.get("province"))) {
      throw new InvalidDeliveryInfoException("Invalid province");
    }
    return true;
  }


  /**
   * The method validates input phone number.
   *
   * @param phoneNumber string
   * @return boolean
   */
  public boolean validatePhoneNumber(String phoneNumber) {
    // all phone number is 10 characters long
    if (phoneNumber.length() != 10) {
      return false;
    }

    //  phone number should only contains numbers
    try {
      Integer.parseInt(phoneNumber);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * The method validates the input name.
   *
   * @param name string
   * @return boolean
   */
  public boolean validateName(String name) {
    // check if name is not null and only contains legit character for names
    return !name.equals("null") && name.matches("^[A-Za-z \\-'.]+$") && !name.isEmpty();

  }

  /**
   * The method validates the input address.
   *
   * @param address string
   * @return boolean
   */
  public boolean validateAddress(String address) {
    // check if address is not null and only contains legit character for names
    return !address.equals("null") && address.matches("^[A-Za-z0-9][A-Za-z0-9\\/\\.\\-\\, ]+$")
        && !address.isEmpty();
  }

  /**
   * The method validates the input province.
   *
   * @param province string
   * @return boolean
   */
  public boolean validateProvince(String province) {
    // rush order is only available in Hanoi
    if (province.trim() != "Hanoi") {
      return false;
    }
    return true;

  }
}