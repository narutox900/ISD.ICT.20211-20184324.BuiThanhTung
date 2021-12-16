package entity.order;

import utils.Configs;

import java.time.LocalDateTime;

/**
 * This class represents the RushOrder entity.
 *
 * @author tungbt
 */
public class RushOrder extends Order {

  private LocalDateTime deliveryTime;

  public LocalDateTime getDeliveryTime() {
    return deliveryTime;
  }

  public void setDeliveryTime(LocalDateTime deliveryTime) {
    this.deliveryTime = deliveryTime;
  }
}
