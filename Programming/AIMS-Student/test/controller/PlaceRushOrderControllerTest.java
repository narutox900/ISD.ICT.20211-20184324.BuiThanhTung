package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;

/**
 * @author tungbt
 */

public class PlaceRushOrderControllerTest {

  private PlaceRushOrderController placeRushOrderController;
  private Media media;

  @BeforeEach
  void setUp() throws Exception {
    placeRushOrderController = new PlaceRushOrderController();
    media = new Media();
  }


  @ParameterizedTest
  @CsvSource({
      "2002/12/03 19:15,false",
      "2021/12/03 10:15,true",
      "abc,false",
      ",true"
  })
  public void testRushTime(String time, boolean expected) {
    boolean isValid = placeRushOrderController.validateRushTime(time);
    assertEquals(expected, isValid);
  }

  @Test
  public void testRushCartMedia1() {
    boolean isValid = placeRushOrderController.validateRushCartMedia();
    assertEquals(false, isValid);
  }

  @Test
  public void testRushCartMedia2() throws SQLException {
    CartMedia cm1 = new CartMedia(media.getMediaById(1), Cart.getCart(), 2, 4000);
    Cart.getCart().addCartMedia(cm1);
    boolean isValid = placeRushOrderController.validateRushCartMedia();
    assertEquals(false, isValid);
  }

  @Test
  public void testRushCartMedia3() throws SQLException {
    CartMedia cm1 = new CartMedia(media.getMediaById(1), Cart.getCart(), 2, 5000);
    CartMedia cm2 = new CartMedia(media.getMediaById(2), Cart.getCart(), 2, 5000);

    cm1.getMedia().setSupportRushOrder(true);
    Cart.getCart().addCartMedia(cm1);
    Cart.getCart().addCartMedia(cm2);

    boolean isValid = placeRushOrderController.validateRushCartMedia();
    assertEquals(true, isValid);
  }

  @ParameterizedTest
  @CsvSource({
      "Hanoi,true",
      "so 213 hoang hoa tham hanoi,true",
      "so 213 hoang hoa tham hai phong,false",
      ",false"
  })
  public void testProvince(String address, boolean expected) {
    boolean isValid = placeRushOrderController.validateProvince(address);
    assertEquals(expected, isValid);
  }


  @Test
  public void testProvince2() {
    boolean isValid = placeRushOrderController.validateProvince("so 213, hoang hoa tham");
    assertEquals(false, isValid);
  }
}
