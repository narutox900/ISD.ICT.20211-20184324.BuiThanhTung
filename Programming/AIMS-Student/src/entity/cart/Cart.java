package entity.cart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.exception.MediaNotAvailableException;
import entity.media.Media;

public class Cart {

  private List<CartMedia> lstCartMedia;
  private static Cart cartInstance;
//    private List<CartMedia> listSupportCartMedia;

  public static Cart getCart() {
    if (cartInstance == null) {
      cartInstance = new Cart();
    }
    return cartInstance;
  }

  public Cart(List<CartMedia> lstCartMedia) {
    this.lstCartMedia = lstCartMedia;
  }


  public Cart() {
    lstCartMedia = new ArrayList<>();
  }

  public void addCartMedia(CartMedia cm) {
    lstCartMedia.add(cm);
  }

  public void removeCartMedia(CartMedia cm) {
    lstCartMedia.remove(cm);
  }

  public List getListMedia() {
    return lstCartMedia;
  }

  public void emptyCart() {
    lstCartMedia.clear();
  }

  public int getTotalMedia() {
    int total = 0;
    for (Object obj : lstCartMedia) {
      CartMedia cm = (CartMedia) obj;
      total += cm.getQuantity();
    }
    return total;
  }

  public int calSubtotal() {
    int total = 0;
    for (Object obj : lstCartMedia) {
      CartMedia cm = (CartMedia) obj;
      total += cm.getPrice() * cm.getQuantity();
    }
    return total;
  }

  public void checkAvailabilityOfProduct() throws SQLException {
    boolean allAvai = true;
    for (Object object : lstCartMedia) {
      CartMedia cartMedia = (CartMedia) object;
      int requiredQuantity = cartMedia.getQuantity();
      int availQuantity = cartMedia.getMedia().getQuantity();
      if (requiredQuantity > availQuantity) {
        allAvai = false;
      }
    }
    if (!allAvai) {
      throw new MediaNotAvailableException("Some media not available");
    }
  }

  public CartMedia checkMediaInCart(Media media) {
    for (CartMedia cartMedia : lstCartMedia) {
      if (cartMedia.getMedia().getId() == media.getId()) {
        return cartMedia;
      }
    }
    return null;
  }


  public List<CartMedia> getListSupportCartMedia() {
    List<CartMedia> listSupportCartMedia = new ArrayList<>();
    for (Object obj : lstCartMedia) {
      CartMedia cm = (CartMedia) obj;
      if (cm.getMedia().isSupportRushOrder()) {
        listSupportCartMedia.add(cm);
      }
    }
    return listSupportCartMedia;
  }

  public List<CartMedia> getListNormalCartMedia() {
    List<CartMedia> listNormalCartMedia = new ArrayList<>();
    for (Object obj : lstCartMedia) {
      CartMedia cm = (CartMedia) obj;
      if (!cm.getMedia().isSupportRushOrder()) {
        listNormalCartMedia.add(cm);
      }
    }
    return listNormalCartMedia;
  }

}
